package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.FileStorageService;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;

/**
 * Сервис для работы с изображениями.
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileStorageService fileStorageService;

    /**
     * Сохраняет файл изображения и создает запись в базе данных.
     *
     * @param multipartFile Файл изображения для сохранения.
     * @return Сохраненный объект Image.
     * @throws IllegalArgumentException Если передано пустое изображение.
     */
    @Override
    public Image saveMultipartFile(MultipartFile multipartFile) {
        log.info("Сохраняем и возвращаем изображение");
        Image image = new Image();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String savedFilePath = fileStorageService.saveFile(multipartFile);
            image.setPath(savedFilePath);
            return imageRepository.save(image);
        } else {
            throw new IllegalArgumentException("Пустое изображение не может быть сохранено!");
        }
    }

    /**
     * Сохраняет объект Image в базе данных.
     *
     * @param image Объект Image для сохранения.
     * @return Сохраненный объект Image.
     */
    @Override
    public Image save(Image image) {
        log.info("Сохраняем и возвращаем картинку");
        return imageRepository.save(image);
    }

    /**
     * Возвращает массив байтов изображения по указанному пути.
     *
     * @param path Путь к изображению.
     * @return Массив байтов изображения или пустой массив, если изображение не найдено.
     */
    @Override
    public byte[] getImageByPath(String path) {
        log.info("Находим и возвращаем массив байтов картинки");
        return fileStorageService.getFileBytes(path);
    }
}

