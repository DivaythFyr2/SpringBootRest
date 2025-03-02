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
public class UserDTO {

    @NotBlank(message =  "Name cannot be empty")
    private String name;

    @Min(value = 14, message = "Age must be greater than or equal to 14")
    private int age;

    @Min(value = 40, message = "Weight must be greater than or equal to 40")
    private double weight;

    @Min(value = 80, message = "Height must be greater than or equal to 80")
    private double height;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserDTO userDTO = (UserDTO) obj;
        return age == userDTO.age &&
                Double.compare(userDTO.weight, weight) == 0 &&
                Double.compare(userDTO.height, height) == 0 &&
                Objects.equals(name, userDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, weight, height);
    }
}
