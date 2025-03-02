package maven.example.com.springbootresthomework.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

// Указывает, что этот класс является сущностью БД
@Entity
// Задаёт имя таблицы в БД
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    // Primary Key, автоинкрементный
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Min(value = 14, message = "Age must be greater than or equal to 14")
    private int age;

    @Min(value = 40, message = "Weight must be greater than or equal to 40")
    private double weight;

    @Min(value = 80, message = "Height must be greater than or equal to 80")
    private double height;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Workout> workouts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Meal> meals;

}
