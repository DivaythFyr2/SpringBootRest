package maven.example.com.springbootresthomework.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDTO {

    @NotBlank(message = "Workout name cannot be empty")
    private String name;

    @Min(value = 1, message = "Duration must be at least 1 minutes")
    private int duration;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorkoutDTO that = (WorkoutDTO) obj;
        return duration == that.duration &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration);
    }
}
