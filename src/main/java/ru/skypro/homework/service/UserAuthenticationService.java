package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserAuthentication;

import java.util.Optional;

/**
 * Интерфейс для аутентификации пользователя.
 */
public interface UserAuthenticationService {

    /**
     * Сохраняет информацию об аутентификации пользователя.
     *
     * @param userAuthentication Объект UserAuthentication, содержащий информацию об аутентификации.
     */
    void save(UserAuthentication userAuthentication);

    /**
     * Изменяет пароль пользователя.
     *
     * @param name            Имя пользователя.
     * @param currentPassword Текущий пароль пользователя.
     * @param newPassword     Новый пароль пользователя.
     * @return Optional-объект, содержащий новый токен доступа, если изменение пароля прошло успешно, или пустой Optional-объект, если изменение пароля не удалось.
     */
    Optional<String> changePassword(String name, String currentPassword, String newPassword);
}