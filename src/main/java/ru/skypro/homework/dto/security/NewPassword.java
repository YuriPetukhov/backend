package ru.skypro.homework.dto.security;

import lombok.Data;
/**
 * Класс DTO обновления пароля пользователя.
 */

@Data
public class NewPassword {
    /**
     * Текущий пароль пользователя.
     */
    private String currentPassword;
    /**
     * Новый пароль пользователя.
     */
    private String newPassword;
}
