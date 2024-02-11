package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.entity.UserAuthentication;
import ru.skypro.homework.repository.UserAuthenticationRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.testData.TestData;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestData.class)
class UserAuthenticationServiceImplTest {
    @Mock
    private UserAuthenticationRepository userAuthenticationRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserAuthenticationServiceImpl userAuthenticationService;
    private UserAuthentication userAuthentication;
    private User user;
    @BeforeEach
    void init() {
        userAuthentication = TestData.generateRandomUserAuthentication();
        user = TestData.randomTestDataUser();
    }

    @AfterEach
    void cleanup() {
        userAuthenticationRepository.deleteAll();
    }

    @Test
    @DisplayName("Тестирование сохранения пароля - успешный случай")
    void save() {
        // Создание тестовых данных
        userAuthentication.setUser(user);
        user.setUserAuthentication(userAuthentication);

        // Выполняем метод сохранения пароля
        userAuthenticationService.save(userAuthentication);

        // Проверка вызова метода сохранения пароля
        verify(userAuthenticationRepository).save(userAuthentication);
    }

    @Test
    @DisplayName("Тестирование сохранения пароля - неуспешный случай")
    void save_unsuccessful() {
        // Установка ожидаемого поведения
        doThrow(RuntimeException.class).when(userAuthenticationRepository).save(any(UserAuthentication.class));

        // Выполнение метода сохранения пароля
        assertThrows(RuntimeException.class, () -> userAuthenticationService.save(userAuthentication));

        // Проверка вызова метода сохранения пароля
        verify(userAuthenticationRepository).save(userAuthentication);
    }

    @Test
    @DisplayName("Тестирование изменения пароля - успешный случай")
    void changePassword() {
        // Создание тестовых данных
        userAuthentication.setUser(user);
        user.setUserAuthentication(userAuthentication);

        // Установка ожидаемого поведения
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        when(userService.findUserByEmail(anyString())).thenReturn(user);

        // Выполнение метода изменения пароля
        Optional<String> result = userAuthenticationService.changePassword(user.getEmail(), userAuthentication.getPasswordHash(), "newPassword");

        // Проверка результата
        assertTrue(result.isPresent());
        assertEquals("newPassword", result.get());

        // Проверка вызовов методов
        verify(userService).findUserByEmail(user.getEmail());
        verify(userAuthenticationRepository).save(userAuthentication);
    }

    @Test
    @DisplayName("Тестирование изменения пароля - неуспешный случай при несовпадении паролей")
    void changePassword_unsuccessful() {
        // Создание тестовых данных
        userAuthentication.setUser(user);
        user.setUserAuthentication(userAuthentication);

        // Установка ожидаемого поведения
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        when(userService.findUserByEmail(anyString())).thenReturn(user);

        // Выполнение метода изменения пароля
        Optional<String> result = userAuthenticationService.changePassword(user.getEmail(), userAuthentication.getPasswordHash(), "newPassword");

        // Проверка результата
        assertFalse(result.isPresent());

        // Проверка вызовов методов
        verify(userService).findUserByEmail(user.getEmail());
        verify(userAuthenticationRepository, never()).save(any(UserAuthentication.class));
    }
}