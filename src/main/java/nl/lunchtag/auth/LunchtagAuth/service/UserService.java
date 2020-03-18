package nl.lunchtag.auth.LunchtagAuth.service;

import nl.lunchtag.auth.LunchtagAuth.entity.User;
import nl.lunchtag.auth.LunchtagAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    public User createOrUpdate(User user) {
        return this.userRepository.save(user);
    }
}
