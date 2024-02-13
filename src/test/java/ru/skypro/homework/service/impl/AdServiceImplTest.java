package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.testData.TestData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestData.class)
class AdServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private AdMapper adMapper;
    @Mock
    private ImageService imageService;
    @Mock
    private AdRepository adRepository;
    @Mock
    private FileStorageServiceImpl fileStorageService;
    @InjectMocks
    private AdServiceImpl adService;
    private Ad ad1;
    private Ad ad2;
    private Ad ad3;
    private CreateOrUpdateAd createOrUpdateAd;
    private User user;
    private MultipartFile multipartFile;
    private Image image;
    private ExtendedAd extendedAd;
    private AdDTO adDTO;

    @BeforeEach
    void init() {
        ad1 = TestData.randomTestDataAd();
        ad2 = TestData.randomTestDataAd();
        ad3 = TestData.randomTestDataAd();
        createOrUpdateAd = TestData.randomTestDataCreateOrUpdateAd();
        user = TestData.randomTestDataUser();
        multipartFile = TestData.createMockEmptyMultipartFile();
        image = TestData.randomTestDataAd().getImage();
        extendedAd = TestData.randomTestDataExtendedAd();
        adDTO = TestData.randomTestDataAdDTO();
    }

    @AfterEach
    void cleanup() {
        adRepository.deleteAll();
    }

    @Test
    @DisplayName("Тестирование получения всех объявлений - успешный случай")
    void testGetAllAds() {
        // Создание тестовых данных
        List<Ad> ads = List.of(ad1, ad2);

        // Установка ожидаемого поведения
        when(adRepository.findAll()).thenReturn(ads);

        // Вызываем метод поиска всех объявлений
        Ads allAds = adService.getAllAds();

        // Проверки
        assertNotNull(allAds);
        assertEquals(2, (int) allAds.getCount());

    }

    @Test
    @DisplayName("Тестирование создания объявления - успешный случай")
    void testCreateAd() {
        CreateOrUpdateAd savedAd = null;

        // Установка ожидаемого поведения
        when(imageService.saveMultipartFile(any(MultipartFile.class))).thenReturn(image);
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toEntityAd(any(CreateOrUpdateAd.class))).thenReturn(ad3);
        when(adMapper.toDtoCreateOrUpdateAd(any(Ad.class))).thenReturn(createOrUpdateAd);

        // Вызываем метод создания объявления
        savedAd = adService.createAd(createOrUpdateAd, multipartFile, user.getEmail());

        // Проверка, что объявление было успешно создано
        assertNotNull(savedAd);
        assertThat(savedAd.getTitle()).isEqualTo(createOrUpdateAd.getTitle());
        assertThat(savedAd.getDescription()).isEqualTo(createOrUpdateAd.getDescription());
        assertThat(savedAd.getPrice()).isEqualTo(createOrUpdateAd.getPrice());
        verify(adRepository).save(any(Ad.class));

    }
    @Test
    @DisplayName("Тестирование создания объявления - неуспешный случай")
    void testCreateAd_unsuccessful() {

        // Установка ожидаемого поведения
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        when(adRepository.save(any(Ad.class))).thenReturn(null);
        when(adMapper.toEntityAd(any(CreateOrUpdateAd.class))).thenReturn(ad1);

        // Вызываем метод создания объявления
        CreateOrUpdateAd savedAd = adService.createAd(createOrUpdateAd, null, user.getEmail());

        // Проверка, что объявление не было создано
        assertNull(savedAd);
        verify(adRepository).save(any(Ad.class));
    }

    @Test
    @DisplayName("Тестирование поиска информации объявления по id - успешный случай")
    void testGetAdInfo() {
        // Создание тестовых данных
        Optional<Ad> optionalAd = Optional.of(ad1);
        extendedAd.setPk(ad1.getPk());

        // Установка ожидаемого поведения
        when(adRepository.findById(any(Integer.class))).thenReturn(optionalAd);
        when(adMapper.toDtoExtendedAd(any(Ad.class))).thenReturn(extendedAd);

        // Вызываем метод поиска объявления
        extendedAd = adService.getAdInfo(ad1.getPk());

        // Проверка, что объявление было успешно найдено
        assertNotNull(extendedAd);
        assertThat(extendedAd.getPk()).isEqualTo(ad1.getPk());
    }

    @Test
    @DisplayName("Тестирование поиска информации объявления по id - неуспешный случай")
    void testGetAdInfo_unsuccessful() {
        // Установка ожидаемого поведения
        when(adRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Вызываем метод поиска объявления
        ExtendedAd userAd = adService.getAdInfo(ad1.getPk());

        // Проверка, что объявление не было найдено
        assertNull(userAd);
    }

    @Test
    @DisplayName("Тестирование удаления объявления по id - успешный случай")
    void deleteAd() {
        // Создаем тестовые данные
        Optional<Ad> optionalAd = Optional.of(ad1);

        // Устанавливаем ожидаемое поведение
        when(adRepository.findById(any(Integer.class))).thenReturn(optionalAd);
        doNothing().when(adRepository).delete(any(Ad.class));

        // Вызываем метод удаления объявления
        boolean isDeleted = adService.deleteAd(ad1.getPk());

        // Проверяем, что объявление было удалено успешно
        assertTrue(isDeleted);
        verify(adRepository, times(1)).delete(any(Ad.class));
    }

    @Test
    @DisplayName("Тестирование удаления объявления по id - неуспешный случай")
    void deleteAd_unsuccessful() {
        // Устанавливаем ожидаемое поведение
        when(adRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Вызываем метод удаления объявления
        boolean isDeleted = adService.deleteAd(ad1.getPk());

        // Проверяем, что объявление не было удалено
        assertFalse(isDeleted);
        verify(adRepository, never()).delete(any(Ad.class));
    }

    @Test
    @DisplayName("Тестирование обновления объявление по id - успешный случай")
    void updateAd() {
        // Создание тестовых данных
        Optional<Ad> optionalAd = Optional.of(ad1);
        adDTO.setPk(ad1.getPk());

        // Устанавливаем ожидаемое поведение
        when(adRepository.findById(any(Integer.class))).thenReturn(optionalAd);
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toDtoAd(any(Ad.class))).thenReturn(adDTO);

        // Вызываем метод обновления объявления
        adDTO = adService.updateAd(createOrUpdateAd, ad1.getPk());

        // Проверяем, что объявление было обновлено успешно
        assertNotNull(adDTO);
        assertEquals(ad1.getPk(), adDTO.getPk());
        verify(adRepository).save(ad1);
    }

    @Test
    @DisplayName("Тестирование обновления объявления по id - неуспешный случай")
    void updateAd_unsuccessful() {
        // Устанавливаем ожидаемое поведение
        when(adRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Вызываем метод обновления объявления
        AdDTO updatedAdDTO = adService.updateAd(adMapper.toDtoCreateOrUpdateAd(ad1), ad1.getPk());

        // Проверяем, что объявление не было обновлено
        assertNull(updatedAdDTO);
        verify(adRepository, never()).save(any(Ad.class));
    }

    @Test
    @DisplayName("Тестирование нахождения объявление по id  - успешный случай")
    void findAdById() {
        // Создание тестовых данных
        Optional<Ad> optionalAd = Optional.of(ad1);

        // Установка ожидаемого поведения
        when(adRepository.findById(any(Integer.class))).thenReturn(optionalAd);

        // Вызываем метод поиска объявления
        Ad foundAd = adService.findAdById(ad1.getPk());

        // Проверка, что объявление было успешно найдено
        assertNotNull(foundAd);
        assertThat(foundAd.getPk()).isEqualTo(ad1.getPk());
    }

    @Test
    @DisplayName("Тестирование нахождения объявление по id  - неуспешный случай")
    void findAdById_unsuccessful() {
        // Установка ожидаемого поведения
        when(adRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Вызываем метод поиска объявления
        Ad foundAd = adService.findAdById(ad1.getPk());

        // Проверка, что объявление не было найдено
        assertNull(foundAd);
        verify(adRepository).findById(ad1.getPk());
    }

    @Test
    @DisplayName("Тестирование поиска объявлений авторизованного пользователя - успешный случай")
    void getAuthUserAds() {
        // Создание тестовых данных
        List<Ad> ads = List.of(ad1, ad2);

        User user = TestData.randomTestDataUser();
        user.setAds(ads);

        // Установка ожидаемого поведения
        when(userService.findUserByEmail(any(String.class))).thenReturn(user);

        // Вызываем метод поиска всех объявлений пользователя
        Ads allAds = adService.getAuthUserAds(user.getEmail());

        // Проверка, что поиск завершен успешно
        assertTrue(allAds.getCount() >= 2);
    }

    @Test
    @DisplayName("Тестирование, является ли пользователь автором объявления - успешный случай")
    void isAuthorAd() {
        // Создание тестовых данных
        Optional<Ad> optionalAd = Optional.of(ad1);
        user.setId(1);
        ad1.setUser(user);

        // Установка ожидаемого поведения
        when(userService.findUserByEmail(any(String.class))).thenReturn(user);
        when(adRepository.findById(any(Integer.class))).thenReturn(optionalAd);

        // Вызываем метод проверка авторства пользователя
        boolean result = adService.isAuthorAd(user.getEmail(), ad1.getPk());

        // Проверка, что метод завершен успешно
        assertTrue(result);
    }


    @Test
    @DisplayName("Тестирование, является ли пользователь автором объявления - неуспешный случай")
    void isAuthorAd_unsuccessful() {
        // Создание тестовых данных
        Optional<Ad> optionalAd = Optional.of(ad1);

        // Установка ожидаемого поведения
        when(userService.findUserByEmail(any(String.class))).thenReturn(user);

        // Вызываем метод проверка авторства пользователя
        boolean result = adService.isAuthorAd(user.getEmail(), ad1.getPk());

        // Проверка, что метод завершен неуспешно
        assertFalse(result);
    }

    @Test
    @DisplayName("Тестирование обновления картинки объявления - успешный случай")
    void updateImage() {
        // Создание тестовых данных
        Optional<Ad> optionalAd = Optional.of(ad1);
        ad1.setImage(image);
        final Image[] updatedImage = new Image[1];

        // Установка ожидаемого поведения
        when(fileStorageService.saveFile(any(MultipartFile.class))).thenReturn(image.getPath());
        when(adRepository.findById(any(Integer.class))).thenReturn(optionalAd);
        when(imageService.save(any(Image.class))).thenAnswer(invocation -> {
            updatedImage[0] = invocation.getArgument(0);
            updatedImage[0].setId(1);
            return updatedImage[0];
        });

        // Вызываем метод обновления картинки
        String result = adService.updateImage(ad1.getPk(), multipartFile);

        // Проверка, что метод завершен успешно
        assertNotNull(result);
        assertEquals(result, image.getPath());
        verify(imageService).save(updatedImage[0]);
    }

    @Test
    @DisplayName("Тестирование обновления картинки объявления - случай отсутствия изображения")
    void updateImage_imageNotFound() {
        // Подготовка тестовых данных
        Ad adWithoutImage = new Ad();
        adWithoutImage.setPk(1);
        adWithoutImage.setImage(null);

        // Выполнение метода updateImage
        String result = adService.updateImage(adWithoutImage.getPk(), multipartFile);

        // Проверка, что результат равен null
        assertNull(result);

        // Проверка, что сервис сохранения изображения не был вызван
        verify(imageService, never()).save(any(Image.class));

        // Проверка, что метод удаления файла не был вызван
        verify(fileStorageService, never()).deleteFile(anyString());

    }
}