package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final FileStorageServiceImpl fileStorageService;
    private final AdMapper adMapper;

    /**
     * Получает список всех объявлений.
     *
     * @return Список всех объявлений.
     */
    @Override
    public Ads getAllAds() {
        log.info("Получаем и возвращаем список всех объявлений");
        List<ru.skypro.homework.entity.Ad> allAds = adRepository.findAll();
         List <ru.skypro.homework.dto.ads.Ad> results = allAds.stream()
                .map(adMapper::toDtoAd)
                .collect(Collectors.toList());
        return new Ads(results.size(), results);
    }

    /**
     * Создает новое объявление.
     *
     * @param properties      Объект CreateOrUpdateAd с информацией об объявлении.
     * @param multipartFile Мультипарт-файл изображения для объявления.
     * @param email Email пользователя, создающего объявление.
     * @return Созданное объявление.
     */
    @Override
    public CreateOrUpdateAd createAd(CreateOrUpdateAd properties, MultipartFile multipartFile, String email) {
        log.info("Создаем и возвращаем объект DTO объявление пользователя {}", email);
        Ad entityAd = adMapper.toEntityAd(properties);
        entityAd.setUser(userService.findUserByEmail(email));
        entityAd.setImage(imageService.saveMultipartFile(multipartFile));
        return adMapper.toDtoCreateOrUpdateAd(adRepository.save(entityAd));
    }

    /**
     * Получает информацию об объявлении по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @return Объект Ad, представляющий информацию об объявлении.
     */
    @Override
    public ExtendedAd getAdInfo(Integer id) {
        log.info("Находим и возвращаем информацию объявления по id {}", id);
        return adMapper.toDtoExtendedAd(adRepository.findById(id).orElse(null));
    }

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @return true, если объявление успешно удалено, false в противном случае.
     */
    @Override
    public boolean deleteAd(Integer id) {
        log.info("Удаляем объявление по id {} и возвращаем boolean значение", id);
        return adRepository.findById(id)
                .map(ad -> {
                    fileStorageService.deleteFile(ad.getImage().getPath());
                    adRepository.delete(ad);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Обновляет информацию об объявлении.
     *
     * @param createOrUpdateAd Объект Ad с обновленными данными.
     * @param id       Идентификатор объявления.
     * @return Обновленное объявление.
     */
    @Override
    public ru.skypro.homework.dto.ads.Ad updateAd(CreateOrUpdateAd createOrUpdateAd, Integer id) {
        log.info("Обновляем объявление по id {} и возвращаем объект DTO", id);
        return adRepository.findById(id)
                .map(ad -> {
                    ad.setPrice(createOrUpdateAd.getPrice());
                    ad.setTitle(createOrUpdateAd.getTitle());
                    ad.setDescription(createOrUpdateAd.getDescription());
                    return adMapper.toDtoAd(adRepository.save(ad));
                })
                .orElse(null);
    }

    /**
     * Находит объявление по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @return Объект Ad, представляющий найденное объявление.
     */
    @Override
    public Ad findAdById(Integer id) {
        log.info("Находим и возвращаем объявление по id {}", id);
        return adRepository.findById(id).orElse(null);
    }

    /**
     * Получает список объявлений авторизованного пользователя.
     *
     * @param email Email авторизованного пользователя.
     * @return Список объявлений авторизованного пользователя.
     */
    @Override
    public Ads getAuthUserAds(String email) {
        log.info("Находим и возвращаем список объявлений авторизованного пользователя {}", email);
        List<Ad> results = userService.findUserByEmail(email).getAds();
        List<ru.skypro.homework.dto.ads.Ad> userAds = results.stream()
                .map(adMapper::toDtoAd)
                .collect(Collectors.toList());
        return new Ads(results.size(), userAds);
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id            Идентификатор объявления.
     * @param multipartFile Мультипарт-файл с новым изображением.
     * @return URL обновленного изображения.
     */
    @Override
    public String updateImage(Integer id, MultipartFile multipartFile) {
        log.info("Обновляем картинку объявления {}", id);
        String newImage = fileStorageService.saveFile(multipartFile);
        Ad ad = findAdById(id);
        Image image = null;
        if (ad != null && ad.getImage() != null) {
            image = ad.getImage();
            fileStorageService.deleteFile(image.getPath());
            image.setPath(newImage);
            log.info("Сохраняем и возвращаем ссылку в проекте на новую картинку {}", image.getPath());
            return imageService.save(image).getPath();
        }
        log.info("Картинка не найдена");
        return null;
    }

    /**
     * Проверяет, является ли пользователь автором объявления.
     *
     * @param email Email пользователя.
     * @param adPk  Идентификатор объявления.
     * @return true, если пользователь является автором объявления, false в противном случае.
     */
    public boolean isAuthorAd(String email, Integer adPk) {
        log.info("Проверяем, является ли пользователь {} автором объявления {}", email, adPk);
        User user = userService.findUserByEmail(email);
        Ad ad = adRepository.findById(adPk).orElse(null);
        log.info("Выполняем проверку и возвращаем boolean значение");
        return user != null && ad != null && ad.getUser().getId().equals(user.getId());
    }


}
