package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.security.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.entity.UserAuthentication;
import ru.skypro.homework.repository.UserAuthenticationRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.testData.TestData;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    private User testUser;
    private UpdateUser updateUser;
    private UserAuthentication testUserAuthentication;
    private String testUserEmail;
    private String currentPassword;

    @BeforeEach
    public void setup() {

        User testUser = TestData.randomTestDataUser();
        testUserEmail = testUser.getEmail();
        userRepository.save(testUser);

        UserAuthentication testUserAuthentication = TestData.generateRandomUserAuthentication();
        testUserAuthentication.setUser(testUser);
        currentPassword = testUserAuthentication.getPasswordHash();
        testUserAuthentication.setPasswordHash(encoder.encode(currentPassword));
        userAuthenticationRepository.save(testUserAuthentication);
    }

    @AfterEach
    void cleanup() {
        userAuthenticationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(userController).isNotNull();
    }

    @Test
    @DisplayName("Тестирование изменения пароля - успешный случай")
    public void testUpdatePassword() {
        // Создание тестовых данных
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword(currentPassword);
        newPassword.setNewPassword("newPassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NewPassword> request = new HttpEntity<>(newPassword, headers);

        // Отправляем запрос на изменение пароля
        ResponseEntity<Object> response = testRestTemplate.withBasicAuth(testUserEmail, currentPassword)
                .postForEntity("/users/set_password", request, Object.class);

        // Проверка успешности изменения пароля
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User updatedUser = userService.findUserByEmail(testUserEmail);
        assertNotNull(updatedUser);
        assertTrue(encoder.matches("newPassword", updatedUser.getUserAuthentication().getPasswordHash()));
    }

    @Test
    @DisplayName("Тестирование изменение пароля - неуспешный случай, так как не указан старый пароль")
    public void testUpdatePassword_unsuccessful() {
        // Создание тестовых данных
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("");
        newPassword.setNewPassword("newPassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NewPassword> request = new HttpEntity<>(newPassword, headers);

        // Отправляем запрос на изменение пароля
        ResponseEntity<Object> response = testRestTemplate.withBasicAuth(testUserEmail, currentPassword)
                .postForEntity("/users/set_password", request, Object.class);

        // Проверка неуспешности изменения пароля
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("Тестирование получения информации об авторизованном пользователе - успешный случай")
    void testGetUser() {
        // Создание тестовых данных
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(testUserEmail, currentPassword);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Отправляем запрос на получение информации о пользователе
        ResponseEntity<Object> response = testRestTemplate.exchange("/users/me", HttpMethod.GET, request, Object.class);

        // Проверка успешности получения информации о пользователе
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Тестирование получения информации об авторизованном пользователе - неуспешный случай")
    void testGetUser_unsuccessful() {
        // Создание тестовых данных
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(testUserEmail, "different_password");

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Отправляем запрос на получение информации о пользователе
        ResponseEntity<Object> response = testRestTemplate.exchange("/users/me", HttpMethod.GET, request, Object.class);

        // Проверка неуспешности получения информации о пользователе
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}