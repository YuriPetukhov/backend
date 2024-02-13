package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.UserDTO;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.Optional;


/**
 * Сервис для работы с пользователями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FileStorageServiceImpl fileStorageService;
    private final ImageService imageService;
    private final UserMapper userMapper;

    /**
     * Сохраняет пользователя.
     *
     * @param user Пользователь для сохранения.
     * @return Сохраненный пользователь.
     */
    @Override
    public User save(User user) {
        log.info("Сохраняем и возвращаем пользователя");
        return userRepository.save(user);
    }

    /**
     * Находит пользователя по его электронной почте.
     *
     * @param email Электронная почта пользователя.
     * @return Найденный пользователь или null, если пользователь не найден.
     */
    @Override
    public UserDTO findDTOUserByEmail(String email) {
        log.info("Находим и возвращаем объект DTO пользователя по email {}", email);
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(userMapper::toDtoUser).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        log.info("Находим и возвращаем пользователя по email {}", email);
        return userRepository.findUserByEmail(email).orElse(null);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param user  Обновленные данные пользователя.
     * @param email Электронная почта пользователя.
     * @return Обновленный пользователь.
     * @throws IllegalArgumentException Если данные для обновления отсутствуют или пользователь не найден.
     */
    @Override
    public UpdateUser update(UpdateUser user, String email) {
        log.info("Обновляем и возвращаем обновленного пользователя {}", email);
        if (user == null) {
            throw new IllegalArgumentException("Данные для обновления отсутствуют");
        }
        User updatedUser = userRepository.findUserByEmail(email).orElse(null);
        if (updatedUser == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        if (user.getFirstName() != null) {
            updatedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            updatedUser.setLastName(user.getLastName());
        }
        if (user.getPhone() != null) {
            updatedUser.setPhone(user.getPhone());
        }
        return userMapper.toDtoUpdateUser(save(updatedUser));
    }

    /**
     * Меняет аватар пользователя.
     *
     * @param multipartFile Файл с новым аватаром.
     * @param email         Электронная почта пользователя.
     * @return Ссылка на новый аватар пользователя в проекте.
     */
    @Override
    public String updateImage(MultipartFile multipartFile, String email) {
        log.info("Меняем аватар пользователя {} и возвращаем путь картинки", email);
        String newImage = fileStorageService.saveFile(multipartFile);
        User user = findUserByEmail(email);
        Image image;
        if (user.getImage() != null) {
            log.info("Картинка была");
            image = user.getImage();
            fileStorageService.deleteFile(image.getPath());
        } else {
            log.info("Картинки не было");
            image = new Image();
            user.setImage(image);
        }
        image.setPath(newImage);
        return imageService.save(image).getPath();
    }
}
