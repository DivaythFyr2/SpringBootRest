package maven.example.com.springbootresthomework.controller;

import jakarta.validation.Valid;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.WorkoutDTO;
import maven.example.com.springbootresthomework.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления тренировками.
 * Обрабатывает CRUD-операции.
 */
@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    /**
     * Получить список всех тренировок.
     *
     * @return список тренировок
     */
    @GetMapping
    public ResponseEntity<List<WorkoutDTO>> getAllWorkouts() {
        return ResponseEntity.ok(workoutService.getAllWorkouts());
    }

    /**
     * Получить тренировку по ID.
     *
     * @param id идентификатор тренировки
     * @return данные о тренировке или 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> getWorkoutById(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.getWorkoutById(id));
    }

    /**
     * Получить список тренировок пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список тренировок пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(workoutService.getWorkoutsByUserId(userId));
    }

    /**
     * Создать новую тренировку для пользователя.
     * Калории рассчитываются автоматически.
     *
     * @param userId     идентификатор пользователя
     * @param workoutDTO данные тренировки (название и продолжительность)
     * @return сообщение об успешном создании
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<String> createWorkout(@PathVariable Long userId, @Valid @RequestBody WorkoutDTO workoutDTO) {
        workoutService.createWorkout(userId, workoutDTO);
        return ResponseEntity.ok()
                .body(MessageConstants.WORKOUT_CREATED);
    }

    /**
     * Обновить данные тренировки.
     *
     * @param id         идентификатор тренировки
     * @param workoutDTO новые данные тренировки
     * @return сообщение об обновлении
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateWorkout(@PathVariable Long id, @Valid @RequestBody WorkoutDTO workoutDTO) {
        workoutService.updateWorkout(id, workoutDTO);
        return ResponseEntity.ok()
                .body(MessageConstants.WORKOUT_UPDATED);
    }

    /**
     * Удалить тренировку по ID.
     *
     * @param id идентификатор тренировки
     * @return сообщение об удалении
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.ok()
                .body(MessageConstants.WORKOUT_DELETED);
    }
}
