package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

import java.util.Optional;


/**
 * Контроллер для загрузки картинок.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Загрузка картинок")
public class ImageController {
    private final ImageService imageService;

    /**
     * Получает картинку по указанному пути.
     *
     * @param path Путь к картинке.
     * @return ответ с байтами картинки.
     */
    @GetMapping(value = "/images/{path}")
    @Operation(summary = "Получение картинки")
    public ResponseEntity<byte[]> getImage(@PathVariable("path") String path) {
        log.info("Отправляем параметры в сервис для получения байтов картинки");
        byte[] imageBytes = Optional.ofNullable(imageService.getImageByPath(path)).orElseGet(() -> new byte[0]);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}

