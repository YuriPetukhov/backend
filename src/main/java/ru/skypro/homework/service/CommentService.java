package ru.skypro.homework.service;

import ru.skypro.homework.dto.comments.CommentDTO;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

/**
 * Интерфейс для работы с комментариями к объявлениям.
 */
public interface CommentService {

    /**
     * Получает список всех комментариев к объявлению.
     *
     * @param id Идентификатор объявления.
     * @return Список всех комментариев к объявлению.
     */
    Comments getAllAdComments(Integer id);

    /**
     * Создает новый комментарий к объявлению.
     *
     * @param text Объект CreateOrUpdateComment, содержащий данные для создания комментария.
     * @param pk            Идентификатор объявления, к которому создается комментарий.
     * @param email         Email пользователя, создающего комментарий.
     * @return Созданный комментарий.
     */
    CommentDTO createComment(CreateOrUpdateComment text, Integer pk, String email);

    /**
     * Удаляет комментарий к объявлению.
     *
     * @param adId      Идентификатор объявления, к которому относится комментарий.
     * @param commentId Идентификатор удаляемого комментария.
     * @return true, если комментарий успешно удален, иначе false.
     */
    boolean deleteComment(Integer adId, Integer commentId);

    /**
     * Обновляет комментарий к объявлению.
     *
     * @param text Объект CreateOrUpdateComment, содержащий обновленные данные для комментария.
     * @param adId          Идентификатор объявления, к которому относится комментарий.
     * @param commentId     Идентификатор обновляемого комментария.
     * @param email         Email пользователя, обновляющего комментарий.
     * @return Обновленный комментарий.
     */
    CommentDTO updateComment(CreateOrUpdateComment text, Integer adId, Integer commentId, String email);
}
