package maven.example.com.springbootresthomework.repository;

import maven.example.com.springbootresthomework.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с пользователями.
 * Наследует JpaRepository и предоставляет стандартные методы работы с БД.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
