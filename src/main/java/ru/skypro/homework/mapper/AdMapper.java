package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entity.Ad;

/**
 * Интерфейс, отвечающий за преобразование объектов связанных с объявлениями (Ad) между различными типами:
 * - между объектами DTO и сущностями (Entity);
 * - между сущностями (Entity) и расширенными объектами DTO (ExtendedAd);
 * - между объектами изображений (Image) и объектами DTO для изображений (ImageDTO).
 */
@Mapper(componentModel = "spring")
public interface AdMapper {
    /**
     * Преобразует сущность (Entity) объявления в объект DTO объявления.
     *
     * @param ad Сущность (Entity) объявления.
     * @return Объект DTO объявления.
     */
    @Mapping(source = "image.path", target = "image")
    @Mapping(source = "user.id", target = "author")
    ru.skypro.homework.dto.ads.Ad toDtoAd(Ad ad);

    /**
     * Преобразует объект создания или обновления объявления (CreateOrUpdateAd) в сущность (Entity) объявления.
     *
     * @param dto Объект создания или обновления объявления.
     * @return Сущность (Entity) объявления.
     */

    Ad toEntityAd(CreateOrUpdateAd dto);

    /**
     * Преобразует сущность (Entity) объявления в объект создания или обновления объявления (CreateOrUpdateAd).
     *
     * @param ad Сущность (Entity) объявления.
     * @return Объект создания или обновления объявления.
     */
    CreateOrUpdateAd toDtoCreateOrUpdateAd(Ad ad);


    /**
     * Преобразует сущность (Entity) объявления в расширенный объект DTO объявления (ExtendedAd).
     *
     * @param ad Сущность (Entity) объявления.
     * @return Расширенный объект DTO объявления.
     */
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "image.path", target = "image")
    @Mapping(source = "user.phone", target = "phone")
    ExtendedAd toDtoExtendedAd(Ad ad);

}
