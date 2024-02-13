package ru.skypro.homework.dto.comments;

import lombok.Data;
/**
 * Класс DTO для создания или обновления комментария.
 */
@Data
public class CreateOrUpdateComment {
    /**
     * Текст комментария.
     */
    private String text;
}
