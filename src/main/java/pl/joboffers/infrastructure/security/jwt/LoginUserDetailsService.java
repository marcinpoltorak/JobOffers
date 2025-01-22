package pl.joboffers.infrastructure.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import pl.joboffers.domain.loginandregister.dto.UserDto;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto byUsername = loginAndRegisterFacade.findByUsername(username);
        return getUser(byUsername);
    }

    private User getUser(UserDto user){
        return new User(
                user.username(),
                user.password(),
                Collections.emptyList());
    }
}
