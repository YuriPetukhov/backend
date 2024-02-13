package ru.skypro.homework.dto.security;

import lombok.Data;
/**
 * Класс DTO для аутентификации пользователя.
 */

@Data
public class Login {
    /**
     * Электронная почта пользователя.
     */
    private String username;
    /**
     * Пароль пользователя.
     */
    private String password;
}
