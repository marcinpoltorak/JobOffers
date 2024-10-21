package pl.joboffers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.joboffers.domain.offer.OfferFetchable;

@SpringBootApplication
public class JobOffersSpringBootApplication {

    public static void main(String[] args){
        SpringApplication.run(JobOffersSpringBootApplication.class, args);
    }
}
