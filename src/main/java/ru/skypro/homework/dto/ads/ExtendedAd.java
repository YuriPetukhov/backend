package ru.skypro.homework.dto.ads;

import lombok.Data;
/**
 * Класс DTO для расширенного набора данных объявления.
 */
@Data
public class ExtendedAd {
    /**
     * Уникальный идентификатор объявления.
     */
    private Integer pk;
    /**
     * Имя автора объявления.
     */
    private String authorFirstName;
    /**
     * Фамилия автора объявления.
     */
    private String authorLastName;
    /**
     * Текст в объявлении.
     */
    private String description;
    /**
     * Электронный адрес автора объявления.
     */
    private String email;
    /**
     * Путь к картинке объявления.
     */
    private String image;
    /**
     * Номер телефона автора объявления.
     */
    private String phone;
    /**
     * Цена в объявлении.
     */
    private Integer price;
    /**
     * Заголовок объявления.
     */
    private String title;
}
