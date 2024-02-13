package ru.skypro.homework.dto.ads;

import lombok.Data;

import java.util.List;

/**
 * Класс DTO для списка объявлений.
 */
@Data
public class Ads {
    /**
     * Количество объявлений.
     */
    private Integer count;

    /**
     * Список объявлений.
     */
    private List<AdDTO> results;

    /**
     * Конструктор для создания объекта Ads с указанным количеством и списком объявлений.
     *
     * @param count Количество объявлений.
     * @param results Список объявлений.
     */
    public Ads(Integer count, List<AdDTO> results) {
        this.count = count;
        this.results = results;
    }
}

