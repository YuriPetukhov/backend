package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.security.Register;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.UserDTO;
import ru.skypro.homework.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * Интерфейс, отвечающий за преобразование объектов пользователей (UserDTO) между различными типами:
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
    UpdateUser toDtoUpdateUser(User user);

    /**
     * Преобразует сущность (Entity) пользователя в объект DTO пользователя (UserDTO).
     *
     * @param user Сущность (Entity) пользователя.
     * @return Объект DTO пользователя.
     */
    @InheritInverseConfiguration
    @Mapping(source = "image.path", target = "image")
    UserDTO toDtoUser(User user);

    /**
     * Преобразует объект регистрации пользователя (Register) из объекта DTO в сущность (Entity) пользователя.
     *
     * @param dto Объект DTO регистрации пользователя.
     * @return Сущность (Entity) пользователя.
     */
    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "userAuthentication.passwordHash")
    User toEntityUser(Register dto);

}

