package maven.example.com.springbootresthomework.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * DTO для передачи данных о приёме пищи.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealDTO {

    @NotBlank(message = "Meal cannot be empty")
    private String name;

    @Min(value = 10, message = "Calories must be greater than 10")
    private int calories;

    private Long userId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MealDTO mealDTO = (MealDTO) obj;
        return calories == mealDTO.calories &&
                Objects.equals(name, mealDTO.name) &&
                Objects.equals(userId, mealDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, calories, userId);
    }
}
