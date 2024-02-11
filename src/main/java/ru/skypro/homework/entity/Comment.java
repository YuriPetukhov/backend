package ru.skypro.homework.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс, представляющий комментарий к объявлению.
 */
@Entity(name = "comments")
@NoArgsConstructor
@Data
public class Comment {
    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    /**
     * Время создания комментария.
     */
    private Long createdAt;

    /**
     * Текст комментария.
     */
    private String text;

    /**
     * Пользователь, оставивший комментарий.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Объявление, к которому относится комментарий.
     */
    @ManyToOne
    @JoinColumn(name = "ad_pk")
    private Ad ad;
}

