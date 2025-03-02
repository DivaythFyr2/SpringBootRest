package maven.example.com.springbootresthomework.mapper;

import maven.example.com.springbootresthomework.dto.WorkoutDTO;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.entity.Workout;
import maven.example.com.springbootresthomework.util.CalorieCalculator;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между Workout и WorkoutDTO.
 */
@Component
public class WorkoutMapper {

    private final CalorieCalculator calorieCalculator;

    public WorkoutMapper(CalorieCalculator calorieCalculator) {
        this.calorieCalculator = calorieCalculator;
    }

    /**
     * Преобразует Workout в WorkoutDTO.
     *
     * @param workout сущность тренировки
     * @return DTO тренировки
     */
    public WorkoutDTO toDTO(Workout workout) {
        if (workout == null) {
            return null;
        }
        return new WorkoutDTO(
                workout.getName(),
                workout.getDuration());
    }

    /**
     * Преобразует WorkoutDTO в Workout (используя CalorieCalculator для расчёта калорий).
     *
     * @param workoutDTO DTO тренировки
     * @param user       пользователь
     * @return сущность тренировки
     */
    public Workout toEntity(WorkoutDTO workoutDTO, User user) {
        if (workoutDTO == null) {
            return null;
        }

        int caloriesBurned = calorieCalculator.calculateCaloriesBurned(workoutDTO.getName(), workoutDTO.getDuration());

        return new Workout(
                null,
                workoutDTO.getName(),
                workoutDTO.getDuration(),
                caloriesBurned,
                user);
    }

    /**
     * Обновляет существующего пользователя данными из DTO.
     *
     * @param workout   Существующая тренировка
     * @param workoutDTO Новые данные
     */
    public void updateEntity(Workout workout, WorkoutDTO workoutDTO) {
        if (workoutDTO == null) {
            return;
        }
        workout.setName(workoutDTO.getName());
        workout.setDuration(workoutDTO.getDuration());

        int caloriesBurned = calorieCalculator.calculateCaloriesBurned(workoutDTO.getName(), workoutDTO.getDuration());
        workout.setCaloriesBurned(caloriesBurned);
    }
}
