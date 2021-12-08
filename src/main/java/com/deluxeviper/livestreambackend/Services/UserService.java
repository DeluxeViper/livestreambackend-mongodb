package com.deluxeviper.livestreambackend.Services;

import com.deluxeviper.livestreambackend.Models.LocationInfo;
import com.deluxeviper.livestreambackend.Models.User;
import com.deluxeviper.livestreambackend.Repository.RoleRepository;
import com.deluxeviper.livestreambackend.Repository.UserRepository;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll().collectList().block();
//                .flatMap(user -> {
//                    List<Role> rolesToBeFound = new ArrayList<>();
//                    user.getRoles().forEach(role -> {
//                        roleRepository.findByName(role.getName()).ifPresent(rolesToBeFound::add);
//                    });
//
//                });
    }

    public List<User> getAllLoggedInUsers() {
        return userRepository.findAllByIsLoggedInIsTrue().collectList().block();
    }

    public Boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).hasElement().block();
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email).blockOptional();
        return user.orElse(null);
    }

    public void addUser(User user) {
        userRepository.save(user).block();

    }

    public User updateUser(User user) {
        Optional<User> optUser = userRepository.findByEmail(user.getEmail()).blockOptional();
        if (optUser.isPresent()) {
            User foundUser = optUser.get();
            foundUser.setIsStreaming(user.getIsStreaming());
            foundUser.setLocationInfo(user.getLocationInfo());
            foundUser.setIsLoggedIn(user.getIsLoggedIn());
            foundUser.setEmail(user.getEmail());
            foundUser.setStreamUrl(user.getStreamUrl());

            return userRepository.save(foundUser).block();
        }

        return null;
    }

    public void setUserLoggedIn(String email, Boolean isLoggedIn) {
        Optional<User> user = userRepository.findByEmail(email).blockOptional();
        if (user.isPresent()) {
            user.get().setIsLoggedIn(isLoggedIn);
            userRepository.save(user.get()).block();
        }
    }

    public User updateLocation(String userId, LocationInfo locationInfo) {
        Optional<User> optUser = userRepository.findById(userId).blockOptional();
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setLocationInfo(locationInfo);
            userRepository.save(user).block();

            return user;
        }

        return null;
    }

    public User isStreaming(String userId, Boolean isStreaming) {
        Optional<User> optUser = userRepository.findById(userId).blockOptional();
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setIsStreaming(isStreaming);
            userRepository.save(user).block();

            return user;
        }

        return null;
    }

    public Flux<Object> subscribeToUsersCollection() {
        MongoDatabase database = reactiveMongoTemplate.getMongoDatabase().block();
        assert database != null;
        MongoCollection<Document> collection = database.getCollection("user");

        // TODO: Refactor this to listen for changes for only users that are logged in
        return Flux.from(collection.watch(Arrays.asList(Aggregates.match(Filters.in("operationType", Arrays.asList("insert", "update", "replace", "delete")))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)).map(ChangeStreamDocument::getFullDocument);
    }

    // Retrieve user (location) info every interval (2 seconds)
    //        return userRepository.findAll().flatMap(user -> {
//            Flux<Long> interval = Flux.interval(Duration.ofSeconds(2));
//
//
//            Flux<LocationInfo> userEventFlux =
//                    Flux.fromStream(Stream.generate(() -> {
//                        if (user.getLocationInfo() == null) {
//                            return new LocationInfo(0.0, 0.0);
//                        } else {
//                            return user.getLocationInfo();
//                        }
//                    }));
//
//            return Flux.zip(interval, userEventFlux).map(Tuple2::getT2);
//        });

    // Retrieve all user info unlimited (every 2 seconds)
    //        return userRepository.findWithTailableCursorBy().delayElements(Duration.ofSeconds(2));

}
