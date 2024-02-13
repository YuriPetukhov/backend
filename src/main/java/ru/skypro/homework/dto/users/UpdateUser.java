package ru.skypro.homework.dto.users;

import lombok.Data;

import javax.validation.constraints.Pattern;
/**
 * Класс DTO для обновления пользователя.
 */
@Data
public class UpdateUser {
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
}
