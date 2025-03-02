package maven.example.com.springbootresthomework.repository;

import maven.example.com.springbootresthomework.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    /**
     * Получить все приёмы пищи пользователя.
     * @param userId ID пользователя
     * @return Список приёмов пищи
     */
    List<Meal> findByUserId(Long userId);
}
