package com.deluxeviper.livestreambackend;

import com.deluxeviper.livestreambackend.Models.LocationInfo;
import com.deluxeviper.livestreambackend.Models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class LivestreambackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LivestreambackendApplication.class, args);
    }

//	@Bean
//	CommandLineRunner runner(UserRepository repository, MongoTemplate mongoTemplate) {
////		return args -> {
//////			LocationInfo info = new LocationInfo(43.7778, -79.3679);
//////			User user = new User(
//////					"Joe",
//////					"Doe",
//////					"joe@gmail.com",
//////					info,
//////					"url"
//////			);
//////
//////			Query query = new Query();
//////			query.addCriteria(Criteria.where("email").is("joe@gmail.com"));
//////
//////			List<User> users = mongoTemplate.find(query, User.class);
//////			System.out.println(users.toString());
//////			repository.insert(user);
////
////		};
//	}
}
