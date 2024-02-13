package ru.skypro.homework.dto.ads;

import lombok.Data;
/**
 * Класс DTO для объявления.
 */
@Data
public class AdDTO {
    /**
     * Уникальный идентификатор объявления.
     */
    private Integer pk;

    /**
     * Цена в объявлении.
     */
    private Integer price;

    /**
     * Заголовок объявления.
     */
    private String title;

    /**
     * Идентификатор автора объявления.
     */
    private Integer author;

    /**
     * Путь к картинке объявления.
     */
    private String image;
}

