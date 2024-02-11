package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

/**
 * Интерфейс для работы с пользователями.
 */
public interface UserService {

    /**
     * Сохраняет пользователя.
     *
     * @param user Объект User для сохранения.
     * @return Сохраненный объект User.
     */
    User save(User user);

    ru.skypro.homework.dto.users.User findDTOUserByEmail(String email);

    /**
     * Находит пользователя по его email.
     *
     * @param email Email пользователя.
     * @return Объект User, представляющий найденного пользователя.
     */
    User findUserByEmail(String email);

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user  Объект User с обновленными данными.
     * @param email Email пользователя.
     * @return Обновленный объект User.
     */
    UpdateUser update(UpdateUser user, String email);

    /**
     * Обновляет изображение аватара пользователя.
     *
     * @param multipartFile Мультипарт-файл изображения для обновления.
     * @param email         Email пользователя.
     * @return URL обновленного изображения аватара пользователя.
     */
    String updateImage(MultipartFile multipartFile, String email);
}

