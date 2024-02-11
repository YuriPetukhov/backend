package ru.skypro.homework.controller;

import ru.skypro.homework.dto.security.Login;
import ru.skypro.homework.dto.security.Register;
import ru.skypro.homework.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер аутентификации и регистрации пользователей.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Обрабатывает запрос на вход пользователя в систему.
     *
     * @param login данные пользователя для входа.
     * @return ответ с кодом состояния в зависимости от результата входа.
     */
    @PostMapping("/login")
    @Tag(name = "Авторизация")
    public ResponseEntity<?> login(@RequestBody(required = false) Login login) {
        log.info("Поступил запрос на вход от пользователя " + login.getUsername());
        return authService.login(login) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Обрабатывает запрос на регистрацию нового пользователя.
     *
     * @param register данные нового пользователя для регистрации.
     * @return ответ с кодом состояния в зависимости от результата регистрации.
     */
    @PostMapping("/register")
    @Tag(name = "Регистрация")
    public ResponseEntity<?> register(@RequestBody(required = false) Register register) {
        log.info("Поступил запрос на регистрацию");
        return authService.register(register) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

