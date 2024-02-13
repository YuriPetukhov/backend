package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.UserDTO;
import ru.skypro.homework.entity.User;

/**
 * Интерфейс для работы с пользователями.
 */
public interface UserService {

    /**
     * Сохраняет пользователя.
     *
     * @param user Объект UserDTO для сохранения.
     * @return Сохраненный объект UserDTO.
     */
    User save(User user);

    UserDTO findDTOUserByEmail(String email);

    /**
     * Находит пользователя по его email.
     *
     * @param email Email пользователя.
     * @return Объект UserDTO, представляющий найденного пользователя.
     */
    User findUserByEmail(String email);

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user  Объект UserDTO с обновленными данными.
     * @param email Email пользователя.
     * @return Обновленный объект UserDTO.
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

