package ru.skypro.homework.dto.users;


import lombok.Data;
import ru.skypro.homework.enums.Role;
/**
 * Класс DTO для пользователя.
 */
@Data
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Integer id;
    /**
     * Электронная почта пользователя.
     */
    private String email;
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
    /**
     * Путь к аватару пользователя.
     */
    private String image;
}
