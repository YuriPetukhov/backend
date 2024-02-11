package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с комментариями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdService adService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    /**
     * Получает список всех комментариев для объявления с указанным идентификатором.
     *
     * @param id Идентификатор объявления.
     * @return Список всех комментариев для указанного объявления.
     */
    @Override
    public Comments getAllAdComments(Integer id) {
        log.info("Получаем и возвращаем список всех комментариев");
        List<Comment> comments = commentRepository.findAllByAdPk(id);
        List<ru.skypro.homework.dto.comments.Comment> results = comments.stream()
                .map(commentMapper::toDtoComment)
                .collect(Collectors.toList());
        return new Comments(results.size(), results);
    }

    /**
     * Создает новый комментарий для указанного объявления и пользователя.
     *
     * @param text  Объект CreateOrUpdateComment с информацией о новом комментарии.
     * @param pk    Идентификатор объявления.
     * @param email Email пользователя, создающего комментарий.
     * @return Созданный комментарий или null, если объявление не найдено.
     */
    @Override
    public ru.skypro.homework.dto.comments.Comment createComment(CreateOrUpdateComment text, Integer pk, String email) {
        log.info("Создаем комментарий пользователя {} и возвращаем объект DTO", email);
        Ad ad = adService.findAdById(pk);
        User user = userService.findUserByEmail(email);
        if (ad != null && user != null) {
            Comment newComment = commentMapper.toEntityComment(text);
            newComment.setUser(user);
            newComment.setAd(ad);
            newComment.setCreatedAt(System.currentTimeMillis());
            return commentMapper.toDtoComment(commentRepository.save(newComment));
        } else {
            return null;
        }
    }

    /**
     * Удаляет комментарий по указанному идентификатору.
     *
     * @param adId      Идентификатор объявления, к которому относится комментарий.
     * @param commentId Идентификатор комментария.
     * @return true, если комментарий успешно удален, false в противном случае.
     */
    @Override
    public boolean deleteComment(Integer adId, Integer commentId) {
        log.info("Удаляем комментарий по id {} и возвращаем boolean значение", commentId);
        return Optional.ofNullable(adService.findAdById(adId))
                .map(ad -> {
                    commentRepository.deleteById(commentId);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Обновляет комментарий по указанному идентификатору.
     *
     * @param createOrUpdateComment Объект CommentDTO с обновленной информацией о комментарии.
     * @param adId                  Идентификатор объявления, к которому относится комментарий.
     * @param commentId             Идентификатор комментария.
     * @param email                 Email пользователя, обновляющего комментарий.
     * @return Обновленный комментарий или null, если комментарий не найден или не относится к указанному объявлению.
     */
    @Override
    public ru.skypro.homework.dto.comments.Comment updateComment(CreateOrUpdateComment createOrUpdateComment, Integer adId, Integer commentId, String email) {
        log.info("Обновляем комментарий по id {} пользователя {} и возвращаем объект DTO", commentId, email);
        return commentRepository.findById(commentId)
                .filter(comment -> comment.getAd().equals(adService.findAdById(adId)))
                .map(comment -> {
                    comment.setText(createOrUpdateComment.getText());
                    return commentMapper.toDtoComment(commentRepository.save(comment));
                })
                .orElseGet(() -> {
                    return null;
                });
    }

    /**
     * Проверяет, является ли указанный пользователь автором комментария.
     *
     * @param email     Email пользователя.
     * @param commentPk Идентификатор комментария.
     * @return true, если пользователь является автором комментария, false в противном случае.
     */
    public boolean isAuthorComment(String email, Integer commentPk) {
        log.info("Проверяем, является ли {} автором комментария {} и возвращаем boolean значение", email, commentPk);
        return commentRepository.findById(commentPk)
                .map(comment -> {
                    return comment.getUser().equals(userService.findUserByEmail(email));
                })
                .orElse(false);
    }

}

