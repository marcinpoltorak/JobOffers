package pl.joboffers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class JobOffersSpringBootApplication {

    public static void main(String[] args){
        SpringApplication.run(JobOffersSpringBootApplication.class, args);
    }
}
