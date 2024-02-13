package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserAuthentication;
import ru.skypro.homework.repository.UserAuthenticationRepository;
import ru.skypro.homework.service.UserAuthenticationService;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

/**
 * Сервис для работы с аутентификацией пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder encoder;
    private final UserService userService;

    /**
     * Сохраняет информацию об аутентификации пользователя.
     *
     * @param userAuthentication Объект UserAuthentication для сохранения.
     */
    @Override
    public void save(UserAuthentication userAuthentication) {
        log.info("Сохраняем пароль пользователя");
        userAuthenticationRepository.save(userAuthentication);
    }

    /**
     * Меняет пароль пользователя.
     *
     * @param name              Имя пользователя.
     * @param currentPassword   Текущий пароль пользователя.
     * @param newPassword       Новый пароль пользователя.
     * @return Новый пароль пользователя, если изменение прошло успешно, или пустой Optional, если текущий пароль неверен.
     */
    @Override
    public Optional<String> changePassword(String name, String currentPassword, String newPassword) {
        log.info("Меняем пароль пользователя {} и возвращаем объект Optional", name);
        UserAuthentication userAuthentication = userService.findUserByEmail(name).getUserAuthentication();
        if (encoder.matches(currentPassword, userAuthentication.getPasswordHash())) {
            userAuthentication.setPasswordHash(encoder.encode(newPassword));
            save(userAuthentication);
            return Optional.of(newPassword);
        } else {
            return Optional.empty();
        }
    }
}

