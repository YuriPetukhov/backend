package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.security.NewPassword;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.service.UserAuthenticationService;
import ru.skypro.homework.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * Контроллер для работы с пользователями.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping(value = "/users")
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;
    private final UserAuthenticationService userAuthenticationService;

    /**
     * Обновляет пароль пользователя.
     *
     * @param newPassword Новый пароль пользователя.
     * @param authentication Данные авторизации пользователя.
     * @return ответ с кодом состояния в зависимости от результата обновления пароля.
     */
    @PostMapping(value = "/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Object> updatePassword(@RequestBody NewPassword newPassword,
                                                 Authentication authentication) {
        log.info("Отправляем данные в сервис для обновления пароля");
        Optional<String> changePasswordResult = userAuthenticationService.changePassword(
                authentication.getName(),
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword()
        );
        log.info("Создаем ответ в зависимости от результатов обновления");
        return changePasswordResult.map(result ->
                ResponseEntity.ok().build()
        ).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Получает информацию об авторизованном пользователе.
     *
     * @param authentication Данные авторизации пользователя.
     * @return ответ с информацией об авторизованном пользователе.
     */
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public ResponseEntity<User> getUser(Authentication authentication) {
        log.info("Отправляем параметры в сервис для получения информации пользователя {}", authentication.getName());
        User userDTO = userService.findDTOUserByEmail(authentication.getName());
        return userDTO != null ?
                ResponseEntity.ok(userDTO) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обновляет информацию об авторизованном пользователе.
     *
     * @param updateUser Данные для обновления пользователя.
     * @param authentication Данные авторизации пользователя.
     * @return ответ с обновленной информацией об авторизованном пользователе.
     */
    @PatchMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public ResponseEntity<UpdateUser> updateInfo(
            @RequestBody(required = false) UpdateUser updateUser,
            Authentication authentication) {
        log.info("Отправляем параметры в сервис для обновления пользователя {}", authentication.getName());
        UpdateUser updatedUserDTO = userService.update(updateUser, authentication.getName());
        return updatedUserDTO != null ?
                ResponseEntity.ok(updatedUserDTO) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обновляет картинку пользователя.
     *
     * @param image Новая картинка пользователя.
     * @param authentication Данные авторизации пользователя.
     * @return ответ с новым путем к картинке пользователя.
     */
    @PatchMapping(value = "/me/image",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление картинки пользователя")
    public ResponseEntity<String> updateImage(@RequestPart("image") MultipartFile image,
                                              Authentication authentication) {
        log.info("Отправляем параметры в сервис для замены старой картинки пользователя {}", authentication.getName());
        String newImage = userService.updateImage(image, authentication.getName());
        return newImage != null ?
                ResponseEntity.ok(newImage) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

