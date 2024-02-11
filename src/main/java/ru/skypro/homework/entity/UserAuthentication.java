package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс, представляющий аутентификацию пользователя.
 */
@Entity(name = "authentications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthentication {
    /**
     * Уникальный идентификатор аутентификации пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Хэш пароля пользователя.
     */
    @Column(nullable = false)
    private String passwordHash;

    /**
     * Пользователь, связанный с этой аутентификацией.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Переопределение метода equals для сравнения аутентификаций пользователей.
     *
     * @param o Объект для сравнения.
     * @return true, если аутентификации равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAuthentication)) return false;
        UserAuthentication that = (UserAuthentication) o;
        return Objects.equals(passwordHash, that.passwordHash);
    }

    /**
     * Переопределение метода hashCode для вычисления хэш-кода аутентификации пользователя.
     *
     * @return Хэш-код аутентификации пользователя.
     */
    @Override
    public int hashCode() {
        return Objects.hash(passwordHash.hashCode());
    }
}

