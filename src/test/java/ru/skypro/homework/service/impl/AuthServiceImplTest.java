package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import ru.skypro.homework.dto.security.Login;
import ru.skypro.homework.dto.security.Register;
import ru.skypro.homework.entity.UserAuthentication;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserAuthenticationRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.testData.TestData;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestData.class)
class AuthServiceImplTest {
    @Mock
    private PostgresUserDetailsService manager;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserAuthenticationRepository userAuthenticationRepository;
    @InjectMocks
    private AuthServiceImpl authService;
    private Login login;
    private Register register;

    @Test
    @DisplayName("Тестирование метода login - успешный случай")
    void testLogin_success() {
        //  Создаем объект пользователя для входа
        login = TestData.randomTestLogin();
        String email = login.getUsername();
        String password = login.getPassword();
        ru.skypro.homework.entity.User user = new ru.skypro.homework.entity.User();
        user.setEmail(email);
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPasswordHash(password);
        userAuthentication.setUser(user);
        user.setUserAuthentication(userAuthentication);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(email, userAuthentication.getPasswordHash(), Collections.emptyList());

        // Устанавливаем ожидаемое поведение
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(manager.loadUserByUsername(login.getUsername())).thenReturn(userDetails);
        when(encoder.matches(password, user.getUserAuthentication().getPasswordHash())).thenReturn(true);

        // Вызываем метод login
        boolean result = authService.login(login);

        // Проверяем, что пользователь успешно вошел
        assertTrue(result);
    }

    @Test
    @DisplayName("Тестирование метода login - неуспешный случай (несуществующий пользователь)")
    void testLogin_userNotFound() {
        //  Создаем объект пользователя для входа
        login = TestData.randomTestLogin();
        String email = login.getUsername();
        String password = login.getPassword();
        ru.skypro.homework.entity.User user = new ru.skypro.homework.entity.User();
        user.setEmail(email);
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPasswordHash(password);
        userAuthentication.setUser(user);
        user.setUserAuthentication(userAuthentication);

        // Устанавливаем ожидаемое поведение
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        // Вызываем метод login
        boolean result = authService.login(login);

        // Проверяем, что пользователь не смог войти
        assertFalse(result);
    }

    @Test
    @DisplayName("Тестирование метода login - неуспешный случай (неверный пароль)")
    void testLogin_incorrectPassword() {
        //  Создаем объект пользователя для входа
        login = TestData.randomTestLogin();
        String email = login.getUsername();
        String incorrectPassword = login.getPassword();
        ru.skypro.homework.entity.User user = new ru.skypro.homework.entity.User();
        user.setEmail(email);
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPasswordHash("password");
        userAuthentication.setUser(user);
        user.setUserAuthentication(userAuthentication);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(email, userAuthentication.getPasswordHash(), Collections.emptyList());

        // Устанавливаем ожидаемое поведение
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(manager.loadUserByUsername(login.getUsername())).thenReturn(userDetails);
        when(encoder.matches(incorrectPassword, userDetails.getPassword())).thenReturn(false);

        // Вызываем метод login
        boolean result = authService.login(login);

        // Проверяем, что метод возвращает false при неверном пароле
        assertFalse(result);
    }

    @Test
    @DisplayName("Тестирование метода register - успешный случай")
    void testRegister_success() {
        // Создаем объект пользователя для регистрации
        register = TestData.randomTestRegister();
        ru.skypro.homework.entity.User user = new ru.skypro.homework.entity.User();
        user.setEmail(register.getUsername());
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPasswordHash(register.getPassword());
        user.setUserAuthentication(userAuthentication);

        // Устанавливаем ожидаемое поведение
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toEntityUser(any(Register.class))).thenReturn(user);
        when(userRepository.save(any(ru.skypro.homework.entity.User.class))).thenReturn(new ru.skypro.homework.entity.User());

        // Вызываем метод register
        boolean result = authService.register(register);

        // Проверяем, что регистрация прошла успешно
        assertTrue(result);
    }

    @Test
    @DisplayName("Тестирование метода register - пользователь уже существует")
    void testRegister_userExists() {
        // Создаем объект пользователя для регистрации
        register = TestData.randomTestRegister();
        ru.skypro.homework.entity.User user = new ru.skypro.homework.entity.User();
        user.setEmail(register.getUsername());
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPasswordHash(register.getPassword());
        user.setUserAuthentication(userAuthentication);

        // Устанавливаем ожидаемое поведение
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        // Вызываем метод register
        boolean result = authService.register(register);

        // Проверяем, что пользователь уже существует
        assertFalse(result);
    }
}