package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.authentication.CustomGrantedAuthority;
import ru.skypro.homework.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Сервис для получения информации о пользователе из базы данных Postgres.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает информацию о пользователе по его электронной почте.
     *
     * @param email Электронная почта пользователя.
     * @return Объект UserDetails с информацией о пользователе.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Получаем пользователя из базы по его {}", email);
        return userRepository.findUserByEmail(email)
                .map(user -> User.builder()
                        .username(user.getEmail())
                        .password(user.getUserAuthentication().getPasswordHash())
                        .roles(user.getRole().name())
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }
}

