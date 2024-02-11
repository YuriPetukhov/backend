package ru.skypro.homework.mapper;

import ru.skypro.homework.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * Интерфейс, отвечающий за преобразование объектов пользователей (User) между различными типами:
 * - между объектами DTO и сущностями (Entity).
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует сущность (Entity) пользователя в объект DTO обновления пользователя (UpdateUser).
     *
     * @param user Сущность (Entity) пользователя.
     * @return Объект DTO обновления пользователя.
     */
    ru.skypro.homework.dto.users.UpdateUser toDtoUpdateUser(User user);

    /**
     * Преобразует сущность (Entity) пользователя в объект DTO пользователя (User).
     *
     * @param user Сущность (Entity) пользователя.
     * @return Объект DTO пользователя.
     */
    @InheritInverseConfiguration
    @Mapping(source = "image.path", target = "image")
    ru.skypro.homework.dto.users.User toDtoUser(User user);

    /**
     * Преобразует объект регистрации пользователя (Register) из объекта DTO в сущность (Entity) пользователя.
     *
     * @param dto Объект DTO регистрации пользователя.
     * @return Сущность (Entity) пользователя.
     */
    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "userAuthentication.passwordHash")
    User toEntityUser(ru.skypro.homework.dto.security.Register dto);

}

