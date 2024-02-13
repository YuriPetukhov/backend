package ru.skypro.homework.repository;

import ru.skypro.homework.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями (UserDTO).
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Находит пользователя (UserDTO) по адресу электронной почты (email).
     *
     * @param email Адрес электронной почты, по которому производится поиск.
     * @return Найденный пользователь (UserDTO) в виде объекта Optional, который может содержать null, если пользователь не найден.
     */
    Optional<User> findUserByEmail(String email);

}

