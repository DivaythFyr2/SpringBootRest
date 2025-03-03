package maven.example.com.springbootresthomework.controller;

import jakarta.validation.Valid;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.MealDTO;
import maven.example.com.springbootresthomework.service.MealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления приёмами пищи.
 * Обрабатывает CRUD-операции.
 */
@RestController
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    /**
     * Получить список всех приёмов пищи.
     *
     * @return Список всех приёмов пищи
     */
    @GetMapping
    public ResponseEntity<List<MealDTO>> getAllMeals() {
        return ResponseEntity.ok(mealService.getAllMeals());
    }

    /**
     * Получить приём пищи по ID.
     *
     * @param id ID приёма пищи
     * @return Найденный приём пищи или 404, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<MealDTO> getMealById(@PathVariable Long id) {
        return ResponseEntity.ok(mealService.getMealById(id));
    }

    /**
     * Получить приёмы пищи конкретного пользователя.
     *
     * @param userId ID пользователя
     * @return Список приёмов пищи пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealDTO>> getMealsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.getMealsByUserId(userId));
    }

    /**
     * Создать новый приём пищи для пользователя.
     *
     * @param userId  ID пользователя
     * @param mealDTO Данные нового приёма пищи
     * @return Сообщение о создании
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<String> createMeal(@PathVariable Long userId, @Valid @RequestBody MealDTO mealDTO) {
        mealService.createMeal(userId, mealDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageConstants.MEAL_CREATED);
    }

    /**
     * Обновить приём пищи по ID.
     *
     * @param id      ID приёма пищи
     * @param mealDTO Новые данные
     * @return Сообщение об обновлении или 404, если не найден
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMeal(@PathVariable Long id, @Valid @RequestBody MealDTO mealDTO) {
        mealService.updateMeal(id, mealDTO);
        return ResponseEntity.ok(MessageConstants.MEAL_UPDATED);
    }

    /**
     * Удалить приём пищи по ID.
     *
     * @param id ID приёма пищи
     * @return Сообщение об удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.ok(MessageConstants.MEAL_DELETED);
    }
}

