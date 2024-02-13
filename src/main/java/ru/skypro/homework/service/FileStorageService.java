package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Интерфейс для работы с файловым хранилищем.
 */
public interface FileStorageService {

    /**
     * Сохраняет файл в хранилище.
     *
     * @param file Файл для сохранения.
     * @return Путь к сохраненному файлу.
     */
    String saveFile(MultipartFile file);

    /**
     * Удаляет файл из хранилища.
     *
     * @param filePath Путь к удаляемому файлу.
     */
    void deleteFile(String filePath);

    /**
     * Возвращает байты файла по указанному пути.
     *
     * @param filePath Путь к файлу.
     * @return Байты файла.
     */
    byte[] getFileBytes(String filePath);
}

