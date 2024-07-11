package pl.joboffers.domain.loginandregister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LoginRepositoryTestImpl implements LoginRepository{

    private Map<String, User> usersDatabase = new HashMap<>();
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersDatabase.get(username));
    }

    @Override
    public User save(User user) {
        UUID id = UUID.randomUUID();
        User savedUser = new User(
                id.toString(),
                user.username(),
                user.password()
        );
        usersDatabase.put(savedUser.username(), savedUser);
        return savedUser;
    }
}
