package ru.skypro.homework.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Класс, представляющий изображение.
 */
@Entity(name = "images")
@NoArgsConstructor
@Data
public class Image {
    /**
     * Уникальный идентификатор изображения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Путь к изображению.
     */
    private String path;

    /**
     * Переопределение метода equals для сравнения изображений.
     *
     * @param o Объект для сравнения.
     * @return true, если изображения равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        Image image = (Image) o;
        return Objects.equals(path, image.path);
    }

    /**
     * Переопределение метода hashCode для вычисления хэш-кода изображения.
     *
     * @return Хэш-код изображения.
     */
    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}

