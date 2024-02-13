package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;

/**
 * Интерфейс для работы с изображениями.
 */
public interface ImageService {

    /**
     * Сохраняет мультипарт-файл изображения.
     *
     * @param multipartFile Мультипарт-файл изображения для сохранения.
     * @return Объект Image, представляющий сохраненное изображение.
     */
    Image saveMultipartFile(MultipartFile multipartFile);

    /**
     * Сохраняет изображение.
     *
     * @param image Объект Image для сохранения.
     * @return Сохраненный объект Image.
     */
    Image save(Image image);

    /**
     * Получает байты изображения по указанному пути.
     *
     * @param path Путь к изображению.
     * @return Байты изображения.
     */
    byte[] getImageByPath(String path);
}

