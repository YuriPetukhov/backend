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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.testData.TestData;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestData.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private FileStorageServiceImpl fileStorageService;
    @Mock
    private ImageService imageService;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    UserServiceImpl userService;
    private Ad ad1;
    private User user;
    private UpdateUser updateUser;
    private User user2;
    private Image image;
    private MultipartFile multipartFile;
    @BeforeEach
    void init() {
        ad1 = TestData.randomTestDataAd();
        user = TestData.randomTestDataUser();
        user2 = TestData.randomTestDataUser();
        image = TestData.randomTestDataAd().getImage();
        multipartFile = TestData.createMockEmptyMultipartFile();
        updateUser = TestData.randomTestDataUpdateUser();
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }
    @Test
    @DisplayName("Тестирование сохранения пользователя - успешный случай")
    void save() {
        // Установка ожидаемого поведения
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1);
            return savedUser;
        });
        // Вызываем метод сохранения пользователя
        userService.save(user);

        // Проверка, что пользователь был успешно сохранен
        verify(userRepository).save(user);
    }
    @Test
    @DisplayName("Тестирование сохранения пользователя - неуспешный случай")
    void save_unsuccessful() {
        // Установка ожидаемого поведения
        doAnswer(invocation -> {
            throw new RuntimeException("Не удалось сохранить пользователя");
        }).when(userRepository).save(any(User.class));

        // Проверка, что выбрасывается исключение при сохранении пользователя
        assertThrows(RuntimeException.class, () -> userService.save(user));
    }
    @Test
    @DisplayName("Тестирование поиска пользователя по email - успешный случай")
    void findUserByEmail() {
        // Установка ожидаемого поведения
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        // Вызываем метод поиска пользователя
        User foundUser = userService.findUserByEmail(user.getEmail());

        // Проверка, что пользователь найден успешно
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }
    @Test
    @DisplayName("Тестирование поиска пользователя по email - неуспешный случай")
    void findUserByEmail_unsuccessful() {
        // Установка ожидаемого поведения
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.empty());

        // Вызываем метод поиска пользователя
        User foundUser = userService.findUserByEmail(user.getEmail());

        // Проверка, что пользователь не найден
        assertNull(foundUser);
    }

    @Test
    @DisplayName("Тестирование обновления пользователя - успешный случай")
    void update() {
        // Создание тестовых данных
        String userEmail = user.getEmail();

        // Установка ожидаемого поведения
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(userMapper.toDtoUpdateUser(any(User.class))).thenReturn(updateUser);
        log.info("updateUser {}", updateUser);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Вызываем метод обновления пользователя
        updateUser = userService.update(updateUser, userEmail);

        // Проверка, что пользователь обновлен успешно
        assertNotNull(updateUser);
        assertEquals(user.getFirstName(), updateUser.getFirstName());
        assertEquals(user.getPhone(), updateUser.getPhone());
    }

    @Test
    @DisplayName("Тестирование обновления пользователя - неуспешный случай")
    void update_unsuccessful() {
        // Установка ожидаемого поведения
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.empty());

        // Вызываем метод обновления пользователя
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.update(updateUser, user.getEmail()));

        // Проверяем, что пробросились правильные ошибки
        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Тестирование обновления аватарки - успешный случай")
    void updateImage() {
        // Создание тестовых данных
        Optional<User> optionalUser = Optional.of(user);
        user.setImage(image);
        final Image[] updatedImage = new Image[1];

        // Установка ожидаемого поведения
        when(fileStorageService.saveFile(any(MultipartFile.class))).thenReturn(image.getPath());
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(optionalUser);
        when(imageService.save(any(Image.class))).thenAnswer(invocation -> {
            updatedImage[0] = invocation.getArgument(0);
            updatedImage[0].setId(1);
            return updatedImage[0];
        });

        // Вызываем метод обновления картинки
        String result = userService.updateImage(multipartFile, user.getEmail());

        // Проверка, что метод завершен успешно
        assertNotNull(result);
        assertEquals(result, image.getPath());
        verify(imageService).save(updatedImage[0]);
    }
    @Test
    @DisplayName("Тестирование обновления аватарки - неуспешный случай")
    void updateImage_unsuccessful() {
        // Проверка, что метод выбрасывает ошибку
        assertThrows(NullPointerException.class, () -> {
            userService.updateImage(multipartFile, ad1.getUser().getEmail());
        });

        verify(imageService, never()).save(any(Image.class));
    }
}