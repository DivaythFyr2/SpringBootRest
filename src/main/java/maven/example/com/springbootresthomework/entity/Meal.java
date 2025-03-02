package maven.example.com.springbootresthomework.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(value = 10, message = "Calories must be greater than 10")
    private int calories;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
