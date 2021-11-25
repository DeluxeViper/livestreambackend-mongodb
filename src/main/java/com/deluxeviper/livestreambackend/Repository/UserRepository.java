package com.deluxeviper.livestreambackend.Repository;

import com.deluxeviper.livestreambackend.Models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByEmail(String email);

    Flux<User> findAllByIsLoggedInIsTrue();

    @Tailable
    Flux<User> findWithTailableCursorBy();
}
