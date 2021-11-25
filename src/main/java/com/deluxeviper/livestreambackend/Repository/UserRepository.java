package com.deluxeviper.livestreambackend.Repository;

import com.deluxeviper.livestreambackend.Models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByEmail(String email);

    @Tailable
    Flux<User> findWithTailableCursorBy();
}
