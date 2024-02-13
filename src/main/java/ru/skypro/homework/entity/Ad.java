package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Класс, представляющий объявление.
 */
@Entity(name = "ads")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ad {
    /**
     * Уникальный идентификатор объявления.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    /**
     * Цена объявления.
     */
    private Integer price;

    /**
     * Заголовок объявления.
     */
    private String title;

    /**
     * Описание в объявлении.
     */
    private String description;

    /**
     * Изображение объявления.
     */
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    /**
     * Пользователь, разместивший объявление.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Список комментариев к объявлению.
     */
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}

