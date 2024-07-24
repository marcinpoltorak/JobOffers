package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.List;

public class OfferFacadeTestConfiguration {
    private final InMemoryOfferFetcher inMemoryOfferFetcher;
    private final InMemoryOfferRepository inMemoryOfferRepository;

    public OfferFacadeTestConfiguration() {
        this.inMemoryOfferFetcher = new InMemoryOfferFetcher(
                List.of(
                        new JobOfferResponse("1afgj", "asdasd", "5000", "1"),
                        new JobOfferResponse("2adfhj", "asddjh", "5700", "2"),
                        new JobOfferResponse("3agdf", "asdkjh", "5400", "3"),
                        new JobOfferResponse("4aasd", "asdpok", "7100", "4"),
                        new JobOfferResponse("14asda", "asdoui", "7000", "5"),
                        new JobOfferResponse("14assgf", "asdogsdi", "6000", "6")
                )
        );
        this.inMemoryOfferRepository = new InMemoryOfferRepository();
    }

    public OfferFacadeTestConfiguration(List<JobOfferResponse> jobOfferResponses) {
        this.inMemoryOfferFetcher = new InMemoryOfferFetcher(jobOfferResponses);
        this.inMemoryOfferRepository = new InMemoryOfferRepository();
    }

    OfferFacade offerFacadeForTests(){
        return new OfferFacade(inMemoryOfferRepository, new OfferService(inMemoryOfferFetcher, inMemoryOfferRepository));
    }
}
