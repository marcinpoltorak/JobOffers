package pl.joboffers.domain.loginandregister;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String notFound) {
        super(notFound);
    }
}
