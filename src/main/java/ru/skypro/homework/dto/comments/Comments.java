package ru.skypro.homework.dto.comments;

import lombok.Data;

import java.util.List;
/**
 * Класс DTO для списка комментариев.
 */
@Data
public class Comments {
    /**
     * Количество комментариев.
     */
    private Integer count;
    /**
     * Список комментариев.
     */
    private List<Comment> results;
    /**
     * Конструктор для создания объекта Comments с указанным количеством и списком комментариев.
     *
     * @param count Количество объявлений.
     * @param results Список объявлений.
     */
    public Comments(Integer count, List<Comment> results) {
        this.count = count;
        this.results = results;
    }
}
