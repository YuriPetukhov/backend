package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;

/**
 * Репозиторий для работы с объявлениями (AdDTO).
 */
public interface AdRepository extends JpaRepository<Ad, Integer> {
}
