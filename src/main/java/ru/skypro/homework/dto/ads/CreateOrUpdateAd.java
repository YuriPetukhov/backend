package ru.skypro.homework.dto.ads;

import lombok.Data;

/**
 * Класс DTO для создания или обновления объявления.
 */
@Data
public class CreateOrUpdateAd {
    /**
     * Заголовок объявления.
     */
    private String title;
    /**
     * Цена в объявлении.
     */
    private Integer price;
    /**
     * Текст в объявлении.
     */
    private String description;
}
