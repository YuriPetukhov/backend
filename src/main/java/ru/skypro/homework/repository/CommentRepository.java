package ru.skypro.homework.repository;

import ru.skypro.homework.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с комментариями (CommentDTO).
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Находит все комментарии (Comment) по идентификатору объявления (pk).
     *
     * @param pk Идентификатор объявления, для которого производится поиск комментариев.
     * @return Список найденных комментариев (Comment).
     */
    List<Comment> findAllByAdPk(Integer pk);
}

