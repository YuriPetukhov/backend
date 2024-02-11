package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр, добавляющий заголовок Access-Control-Allow-Credentials в HTTP-ответы.
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Применяет фильтр к запросу, добавляя заголовок Access-Control-Allow-Credentials в HTTP-ответ.
     *
     * @param httpServletRequest Запрос HTTP.
     * @param httpServletResponse Ответ HTTP.
     * @param filterChain Цепочка фильтров.
     * @throws ServletException Если возникает исключение при обработке запроса.
     * @throws IOException Если возникает ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

