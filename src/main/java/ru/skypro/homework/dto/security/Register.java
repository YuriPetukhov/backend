package ru.skypro.homework.dto.security;

import lombok.NoArgsConstructor;
import ru.skypro.homework.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;
/**
 * Класс DTO регистрации пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    /**
     * Электронная почта пользователя.
     */
    private String username;
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Имя пользователя.
     */
    private String firstName;
    /**
     * Фамилия пользователя.
     */
    private String lastName;
    /**
     * Телефон пользователя.
     */
    private String phone;
    /**
     * Роль пользователя.
     */
    private Role role;
}
