package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.UserAuthentication;

/**
 * Репозиторий для работы с аутентификацией пользователей (UserAuthentication).
 */
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Integer> {

}


