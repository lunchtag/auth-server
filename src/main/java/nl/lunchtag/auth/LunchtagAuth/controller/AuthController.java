package nl.lunchtag.auth.LunchtagAuth.controller;

import nl.lunchtag.auth.LunchtagAuth.config.jwt.TokenProvider;
import nl.lunchtag.auth.LunchtagAuth.controller.enums.AuthResponse;
import nl.lunchtag.auth.LunchtagAuth.entity.User;
import nl.lunchtag.auth.LunchtagAuth.logic.PasswordHelper;
import nl.lunchtag.auth.LunchtagAuth.model.LoginModel;
import nl.lunchtag.auth.LunchtagAuth.model.RegisterModel;
import nl.lunchtag.auth.LunchtagAuth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final PasswordHelper passwordHelper;

    @Autowired
    public AuthController(TokenProvider tokenProvider, UserService userService, PasswordHelper passwordHelper) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.passwordHelper = passwordHelper;
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginModel loginModel) {
        Optional<User> user = userService.findUserByEmail(loginModel.getEmail());

        if(user.isEmpty()) {
            return new ResponseEntity<>(AuthResponse.WRONG_CREDENTIALS.toString(), HttpStatus.BAD_REQUEST);
        }

        if(!this.passwordHelper.isMatch(loginModel.getPassword(), user.get().getPassword())) {
            return new ResponseEntity<>(AuthResponse.WRONG_CREDENTIALS.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            Map<Object, Object> model = new LinkedHashMap<>();
            model.put("token", tokenProvider.createToken(user.get().getId(), user.get().getFirstName(), user.get().getLastName(), user.get().getRole()));
            model.put("user", user.get());
            return ok(model);
        } catch(AuthenticationException ex) {
            return new ResponseEntity<>(AuthResponse.UNEXPECTED_ERROR.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterModel registerModel) {
        if(userService.findUserByEmail(registerModel.getEmail()).isPresent()) {
            return new ResponseEntity<>(AuthResponse.USER_ALREADY_EXISTS.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            User user = new User();

            user.setEmail(registerModel.getEmail());
            user.setFirstName(registerModel.getFirstName());
            user.setLastName(registerModel.getLastName());
            user.setPassword(passwordHelper.hash(registerModel.getPassword()));

            User createdUser = userService.createOrUpdate(user);

            Map<Object, Object> model = new LinkedHashMap<>();
            model.put("token", tokenProvider.createToken(createdUser.getId(), createdUser.getFirstName(), createdUser.getLastName(), createdUser.getRole()));
            model.put("user", createdUser);
            return ok(model);
        } catch(Exception ex) {
            return new ResponseEntity<>(AuthResponse.UNEXPECTED_ERROR.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
