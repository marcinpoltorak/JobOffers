package pl.joboffers.domain.loginandregister;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginAndRegisterFacadeTest {
    LoginAndRegisterFacade loginFacade = new LoginAndRegisterFacade(new LoginRepositoryTestImpl());

    @Test
    public void should_throw_exception_when_user_not_found(){
        // given
        String username = "user";

        // when
        Throwable thrown = catchThrowable(() ->  loginFacade.findByUsername(username));

        // then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("User not found!");
    }

    @Test
    public void should_find_user_by_user_name(){
        // given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        RegistrationResultDto register = loginFacade.register(registerUserDto);

        // when
        UserDto userByName = loginFacade.findByUsername("username");

        // then
        assertThat(userByName).isEqualTo(new UserDto(register.id(), "username", "password"));
    }

    @Test
    public void should_register_user(){
        // given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");

        // when
        RegistrationResultDto register = loginFacade.register(registerUserDto);

        // then
        assertAll(
                () -> assertThat(register.created()).isTrue(),
                () -> assertThat(register.username()).isEqualTo("username")
        );
    }


}
