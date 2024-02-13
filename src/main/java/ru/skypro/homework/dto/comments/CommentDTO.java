package ru.skypro.homework.dto.comments;

import lombok.Data;
/**
 * Класс DTO для комментария.
 */
@Data
public class CommentDTO {
    /**
     * Уникальный идентификатор автора комментария.
     */
    private Integer author;
    /**
     * Путь к картинке автора комментария.
     */
    private String authorImage;
    /**
     * Имя автора комментария.
     */
    private String authorFirstName;
    /**
     * Дата создания комментария.
     */
    private Long createdAt;
    /**
     * Уникальный идентификатор комментария.
     */
    private Integer pk;
    /**
     * Текст комментария.
     */
    private String text;
}
