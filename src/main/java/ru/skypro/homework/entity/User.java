package ru.skypro.homework.entity;

import lombok.*;
import ru.skypro.homework.enums.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий пользователя.
 */
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Электронная почта пользователя.
     */
    @Column(name = "email")
    private String email;

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Номер телефона пользователя.
     */
    private String phone;

    /**
     * Роль пользователя.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Аватар пользователя.
     */
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    /**
     * Список объявлений, созданных пользователем.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ad> ads;

    /**
     * Список комментариев, оставленных пользователем.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    /**
     * Аутентификация пользователя.
     */
    @OneToOne(mappedBy = "user")
    private UserAuthentication userAuthentication;

    /**
     * Переопределение метода equals для сравнения пользователей.
     *
     * @param o Объект для сравнения.
     * @return true, если пользователи равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    /**
     * Переопределение метода hashCode для вычисления хэш-кода пользователя.
     *
     * @return Хэш-код пользователя.
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /**
     * Переопределение метода toString для представления пользователя в виде строки.
     *
     * @return Строковое представление пользователя.
     */
    @Override
    public String toString() {
        return "UserDTO{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               '}';
    }
}

