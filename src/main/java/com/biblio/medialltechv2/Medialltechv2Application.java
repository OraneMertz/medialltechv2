package com.biblio.medialltechv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class Medialltechv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Medialltechv2Application.class, args);
    }

}
