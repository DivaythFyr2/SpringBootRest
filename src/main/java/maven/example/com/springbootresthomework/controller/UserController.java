package maven.example.com.springbootresthomework.controller;

import jakarta.validation.Valid;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.UserDTO;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 * Обрабатывает CRUD-операции.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить список всех пользователей.
     *
     * @return Список пользователей
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Получить пользователя по ID.
     *
     * @param id Идентификатор пользователя
     * @return Данные пользователя или 404, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Создать нового пользователя.
     *
     * @param userDTO Данные пользователя
     * @return Сообщение о создании
     */
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageConstants.USER_CREATED);
    }

    /**
     * Обновить данные пользователя по ID.
     *
     * @param id      Идентификатор пользователя
     * @param userDTO Новые данные пользователя
     * @return Сообщение об обновлении или 404, если не найден
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        return ResponseEntity.ok(MessageConstants.USER_UPDATED);
    }

    /**
     * Удалить пользователя по ID.
     *
     * @param id Идентификатор пользователя
     * @return Сообщение об удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(MessageConstants.USER_DELETED);
    }
}
