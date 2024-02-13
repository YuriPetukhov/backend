package ru.skypro.homework.authentication;

import org.springframework.security.core.GrantedAuthority;
import ru.skypro.homework.enums.Role;

/**
 * Пользовательская реализация интерфейса GrantedAuthority.
 * Этот класс представляет роль пользователя в системе безопасности.
 */
public class CustomGrantedAuthority implements GrantedAuthority {
    private final Role role;
    /**
     * Конструктор класса CustomGrantedAuthority.
     *
     * @param role роль пользователя
     */
    public CustomGrantedAuthority(Role role) {
        this.role = role;
    }
    /**
     * Возвращает строковое представление роли пользователя.
     *
     * @return строковое представление роли
     */
    @Override
    public String getAuthority() {
        return "ROLE_" + role;
    }
}
