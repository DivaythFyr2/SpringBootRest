package maven.example.com.springbootresthomework.util;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Утилитный класс для расчёта сожжённых калорий.
 */
@Component
public class CalorieCalculator {

    private static final Map<String, Integer> WORKOUT_CALORIES = Map.of(
            "Running", 10,
            "Swimming", 8,
            "Cycling", 7,
            "Yoga", 4,
            "Jump Rope", 12
    );

    /**
     * Рассчитывает количество сожжённых калорий на основе типа тренировки и её продолжительности.
     *
     * @param workoutName название тренировки
     * @param duration    продолжительность (в минутах)
     * @return количество сожжённых калорий
     */
    public int calculateCaloriesBurned(String workoutName, int duration) {
        int caloriesPerMinute = WORKOUT_CALORIES.getOrDefault(workoutName, 5);
        return caloriesPerMinute * duration;
    }
}
