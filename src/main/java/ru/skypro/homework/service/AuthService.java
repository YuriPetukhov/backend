package ru.skypro.homework.service;

import ru.skypro.homework.dto.security.Login;
import ru.skypro.homework.dto.security.Register;

/**
 * Интерфейс для аутентификации и авторизации.
 */
public interface AuthService {

    /**
     * Выполняет вход пользователя.
     *
     * @param login Имя и пароль пользователя.
     * @return true, если вход успешен, иначе false.
     */
    boolean login(Login login);

    /**
     * Регистрирует нового пользователя.
     *
     * @param register Объект Register, содержащий данные для регистрации пользователя.
     * @return true, если регистрация успешна, иначе false.
     */
    boolean register(Register register);
}
