package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skypro.homework.logging.LoggingAspect;

import static org.springframework.security.config.Customizer.withDefaults;
/**
 * Конфигурация безопасности приложения.
 * Этот класс определяет настройки безопасности HTTP и доступа к методам приложения.
 * Он также инициализирует обработчик выражений без префикса ролей по умолчанию
 * и кодировщик паролей BCrypt.
 */
@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
@Import(LoggingAspect.class)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration implements WebMvcConfigurer {

    // Белый список URL, доступных без аутентификации
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/images/**",
            "/register"
    };

    /**
     * Конфигурация цепочки фильтров безопасности HTTP.
     * @param http HttpSecurity объект для настройки безопасности HTTP.
     * @return Цепочка фильтров безопасности.
     * @throws Exception Исключение, если возникла ошибка при настройке безопасности.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads")
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Создание кодировщика паролей BCrypt.
     * @return Кодировщик паролей BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
