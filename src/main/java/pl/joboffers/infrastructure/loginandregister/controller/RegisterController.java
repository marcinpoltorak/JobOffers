package pl.joboffers.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;

@RestController
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> register(@RequestBody RegisterUserDto registerUser){
        String encodedPassword = bCryptPasswordEncoder.encode(registerUser.password());
        RegistrationResultDto registerResult = loginAndRegisterFacade.register(new RegisterUserDto(registerUser.username(), encodedPassword));
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResult);
    }
}
