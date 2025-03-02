package maven.example.com.springbootresthomework.repository;

import maven.example.com.springbootresthomework.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    /**
     * Получить все тренировки пользователя.
     * @param userId ID пользователя
     * @return Список тренировок
     */
    List<Workout> findByUserId(Long userId);
}
