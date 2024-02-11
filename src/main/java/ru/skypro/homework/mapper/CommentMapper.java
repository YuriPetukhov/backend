package ru.skypro.homework.mapper;

import org.mapstruct.Mapping;
import ru.skypro.homework.entity.Comment;
import org.mapstruct.Mapper;

/**
 * Интерфейс, отвечающий за преобразование объектов комментариев (Comment) между различными типами:
 * - между объектами DTO и сущностями (Entity);
 * - между объектами создания или обновления комментария (CreateOrUpdateComment) и сущностями (Entity) комментария.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразует сущность (Entity) комментария в объект DTO комментария.
     *
     * @param comment Сущность (Entity) комментария.
     * @return Объект DTO комментария.
     */
    @Mapping(source = "user.image.path", target = "authorImage")
    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    ru.skypro.homework.dto.comments.Comment toDtoComment(Comment comment);

    /**
     * Преобразует объект создания или обновления комментария (CreateOrUpdateComment) в сущность (Entity) комментария.
     *
     * @param dto Объект создания или обновления комментария.
     * @return Сущность (Entity) комментария.
     */
    Comment toEntityComment(ru.skypro.homework.dto.comments.CreateOrUpdateComment dto);

}
