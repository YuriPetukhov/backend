package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.security.Login;
import ru.skypro.homework.dto.security.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.entity.UserAuthentication;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserAuthenticationRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import javax.transaction.Transactional;

/**
 * Сервис аутентификации и авторизации пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final PostgresUserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserMapper userMapper;

    /**
     * Проверяет правильность введенных учетных данных пользователя при попытке входа.
     *
     * @param login Имя (email) и пароль пользователя.
     * @return true, если введенные учетные данные верны, false в противном случае.
     */
    @Override
    public boolean login(Login login) {
        log.info("Вызываем метод login и возвращаем boolean значение");
        if (userRepository.findUserByEmail(login.getUsername()).isEmpty()) {
            log.info("Такой пользователь отсутствует");
            return false;
        }

        UserDetails userDetails = manager.loadUserByUsername(login.getUsername());
        return encoder.matches(login.getPassword(), userDetails.getPassword());
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param register Объект UserDTO с информацией о новом пользователе.
     * @return true, если регистрация прошла успешно, false в противном случае.
     */
    @Override
    public boolean register(Register register) {
        log.info("Вызываем метод register и возвращаем boolean значение");
        if (userRepository.findUserByEmail(register.getUsername()).isPresent()) {
            log.info("Такой пользователь существует");
            return false;
        }
        User user = userMapper.toEntityUser(register);
        userRepository.save(user);
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPasswordHash(encoder.encode(register.getPassword()));
        userAuthentication.setUser(user);
        userAuthenticationRepository.save(userAuthentication);
        user.setUserAuthentication(userAuthentication);
        userRepository.save(user);

        return true;
    }

}
