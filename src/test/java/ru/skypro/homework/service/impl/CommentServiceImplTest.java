package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.control.MappingControl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.testData.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestData.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private AdService adService;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private CommentServiceImpl commentService;

    private Ad ad1;
    private Ad ad2;
    private Comment com1;
    private CreateOrUpdateComment com1DTO;
    private Comment com2;
    private User user;
    private User user2;
    private long createdAt;

    @BeforeEach
    void init() {
        ad1 = TestData.randomTestDataAd();
        ad2 = TestData.randomTestDataAd();
        com1 = TestData.generateRandomComment();
        com1DTO = TestData.generateRandomCommentDTO();
        com2 = TestData.generateRandomComment();
        user = TestData.randomTestDataUser();
        user2 = TestData.randomTestDataUser();
        createdAt = System.currentTimeMillis();
    }

    @AfterEach
    void cleanup() {
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("Тестирование получения всех комментариев - успешный случай")
    void getAllAdComments() {
        // Создание тестовых данных
        List<Comment> comments = List.of(com1, com2);

        // Установка ожидаемого поведения
        when(commentRepository.findAllByAdPk(any(Integer.class))).thenReturn(comments);

        // Вызываем метод поиска всех объявлений
        Comments allCom = commentService.getAllAdComments(ad1.getPk());

        // Проверка, что комментарий был найден
        assertTrue(allCom.getCount() >= 2);
    }

    @Test
    @DisplayName("Тестирование создания комментария - успешный случай")
    void createComment() {
        // Создание тестовых данных
        com1.setCreatedAt(createdAt);
        com1.setUser(user);
        ru.skypro.homework.dto.comments.Comment commentDTO = new ru.skypro.homework.dto.comments.Comment();
        commentDTO.setAuthorFirstName(com1.getUser().getFirstName());
        commentDTO.setText(com1DTO.getText());
        commentDTO.setCreatedAt(com1.getCreatedAt());

        // Установка ожидаемого поведения
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        when(commentMapper.toEntityComment(any(CreateOrUpdateComment.class))).thenReturn(com1);
        when(adService.findAdById(any(Integer.class))).thenReturn(ad1);
        when(commentMapper.toDtoComment(any(Comment.class))).thenReturn(commentDTO);
        when(commentRepository.save(any(Comment.class))).thenReturn(com1);

        // Вызываем метод создания комментария
        commentDTO = commentService.createComment(com1DTO, ad1.getPk(), user.getEmail());

        // Проверка, что комментарий был успешно создан
        assertNotNull(commentDTO);
        assertThat(commentDTO.getText()).isEqualTo(com1DTO.getText());
        assertThat(commentDTO.getAuthorFirstName()).isEqualTo(com1.getUser().getFirstName());
        assertThat(commentDTO.getCreatedAt()).isEqualTo(createdAt);
    }
    @Test
    @DisplayName("Тестирование создания комментария - неуспешный случай")
    void createComment_unsuccessful() {
        // Вызываем метод создания комментария
        ru.skypro.homework.dto.comments.Comment savedComment = commentService.createComment(com1DTO, ad1.getPk(), user.getEmail());

        // Проверка, что комментарий не был создан
        assertNull(savedComment);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }
    @Test
    @DisplayName("Тестирование удаления комментария по id - успешный случай")
    void deleteComment() {
        // Создаем тестовые данные
        List<Comment> comments = new ArrayList<>();
        comments.add(com1);
        ad1.setComments(comments);

        // Устанавливаем ожидаемое поведение
        when(adService.findAdById(any(Integer.class))).thenReturn(ad1);
        doNothing().when(commentRepository).deleteById(any(Integer.class));

        // Вызываем метод удаления комментария
        boolean isDeleted = commentService.deleteComment(ad1.getPk(), com1.getPk());

        // Проверяем, что комментарий был удален успешно
        assertTrue(isDeleted);
        verify(commentRepository, times(1)).deleteById(any(Integer.class));
    }

    @Test
    @DisplayName("Тестирование удаления комментария по id - неуспешный случай")
    void deleteComment_unsuccessful() {
        // Вызываем метод удаления комментария
        boolean isDeleted = commentService.deleteComment(ad1.getPk(), com1.getPk());

        // Проверяем, что комментарий не был удален
        assertFalse(isDeleted);
        verify(commentRepository, times(0)).deleteById(any(Integer.class));
    }

    @Test
    @DisplayName("Тестирование обновления комментария по id - успешный случай")
    void updateComment() {
        // Создаем тестовые данные
        com1.setAd(ad1);
        com1.setUser(user);
        ru.skypro.homework.dto.comments.Comment updatedComment = new ru.skypro.homework.dto.comments.Comment();
        updatedComment.setAuthorFirstName(com1.getUser().getFirstName());
        updatedComment.setText(com1DTO.getText());
        updatedComment.setCreatedAt(com1.getCreatedAt());

        // Устанавливаем ожидаемое поведение
        when(commentRepository.findById(any(Integer.class))).thenReturn(Optional.of(com1));
        when(adService.findAdById(any(Integer.class))).thenReturn(ad1);
        when(commentMapper.toDtoComment(any(Comment.class))).thenReturn(updatedComment);
        when(commentRepository.save(any(Comment.class))).thenReturn(com1);

        // Вызываем метод обновления комментария
        updatedComment = commentService.updateComment(com1DTO, ad1.getPk(), com1.getPk(), user.getEmail());

        // Проверяем, что комментарий был обновлен успешно
        assertNotNull(updatedComment);
        assertEquals(updatedComment.getText(), com1DTO.getText());
        verify(commentRepository, times(1)).findById(com1.getPk());
        verify(commentRepository, times(1)).save(com1);
    }
    @Test
    @DisplayName("Тестирование обновления комментария по id - неуспешный случай")
    void updateComment_unsuccessful() {
        // Создаем тестовые данные
        ru.skypro.homework.dto.comments.Comment updatedComment = null;
        com1.setAd(ad1);
        com2.setAd(ad2);

        // Устанавливаем ожидаемое поведение
        when(commentRepository.findById(any(Integer.class))).thenReturn(Optional.of(com1));
        when(adService.findAdById(any(Integer.class))).thenReturn(ad2);

        // Вызываем метод обновления комментария
        updatedComment = commentService.updateComment(com1DTO, ad1.getPk(), com1.getPk(), user.getEmail());

        // Проверяем, что комментарий не был обновлен
        assertNull(updatedComment);
        verify(commentRepository, times(1)).findById(com1.getPk());
        verify(commentRepository, times(0)).save(com1);
    }

    @Test
    @DisplayName("Тестирование, является ли пользователь автором комментария - успешный случай")
    void isAuthorComment() {
        // Создание тестовых данных
        Optional<Comment> optionalComment = Optional.of(com1);
        com1.setUser(user);
        com1.setAd(ad1);

        // Установка ожидаемого поведения
        when(userService.findUserByEmail(any(String.class))).thenReturn(user);
        when(commentRepository.findById(any(Integer.class))).thenReturn(optionalComment);

        // Вызываем метод проверка авторства пользователя
        boolean result = commentService.isAuthorComment(user.getEmail(), com1.getPk());

        // Проверка, что метод завершен успешно
        assertTrue(result);
    }

    @Test
    @DisplayName("Тестирование, является ли пользователь автором комментария - неуспешный случай")
    void isAuthorComment_unsuccessful() {
        // Создание тестовых данных
        Optional<Comment> optionalComment = Optional.of(com1);
        com1.setUser(user2);
        com1.setAd(ad1);

        // Установка ожидаемого поведения
        when(userService.findUserByEmail(any(String.class))).thenReturn(user);
        when(commentRepository.findById(any(Integer.class))).thenReturn(optionalComment);

        // Вызываем метод проверка авторства пользователя
        boolean result = commentService.isAuthorComment(user.getEmail(), com1.getPk());

        // Проверка, что метод завершен неуспешно
        assertFalse(result);
    }
}