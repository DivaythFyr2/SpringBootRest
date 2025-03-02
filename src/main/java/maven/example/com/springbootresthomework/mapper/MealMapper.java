package maven.example.com.springbootresthomework.mapper;

import maven.example.com.springbootresthomework.dto.MealDTO;
import maven.example.com.springbootresthomework.dto.WorkoutDTO;
import maven.example.com.springbootresthomework.entity.Meal;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.entity.Workout;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между Meal и MealDTO.
 */
@Component
public class MealMapper {

    /**
     * Преобразует сущность Meal в DTO.
     *
     * @param meal объект Meal
     * @return объект MealDTO без ID и списка пользователей
     */

    public MealDTO toDTO(Meal meal) {
       return new MealDTO(
               meal.getName(),
               meal.getCalories(),
               meal.getUser().getId());
    }

    /**
     * Преобразует MealDTO в Meal (без User, его нужно установить в сервисе).
     *
     * @param mealDTO MealDTO
     * @return Meal entity
     */
    public Meal toEntity(MealDTO mealDTO, User user) {
        if(mealDTO == null) {
            return null;
        }
        return new Meal(null,
                mealDTO.getName(),
                mealDTO.getCalories(),
                user);
    }

    /**
     * Обновляет существующего пользователя данными из DTO.
     *
     * @param meal   Существующая тренировка
     * @param mealDTO Новые данные
     */
    public void updateEntity(Meal meal, MealDTO mealDTO) {
        if (meal == null) {
            return;
        }
        meal.setName(mealDTO.getName());
        meal.setCalories(mealDTO.getCalories());

    }
}
