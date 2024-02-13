package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Image;

/**
 * Репозиторий для работы с изображениями (Image).
 */
public interface ImageRepository extends JpaRepository<Image, Integer> {

}