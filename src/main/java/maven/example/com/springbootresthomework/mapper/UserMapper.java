package maven.example.com.springbootresthomework.mapper;

import maven.example.com.springbootresthomework.dto.UserDTO;
import maven.example.com.springbootresthomework.entity.User;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между User и UserDTO.
 */
@Component
public class UserMapper {

    /**
     * Преобразует сущность User в DTO.
     *
     * @param user объект сущности пользователя
     * @return DTO пользователя или null, если входное значение null
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getName(),
                user.getAge(),
                user.getWeight(),
                user.getHeight());
    }

    /**
     * Преобразует DTO пользователя в сущность User.
     * Связанные сущности (Workouts, Meals) не заполняются и остаются null.
     *
     * @param userDTO объект DTO пользователя
     * @return сущность User или null, если входное значение null
     */
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return new User(
                null,
                userDTO.getName(),
                userDTO.getAge(),
                userDTO.getWeight(),
                userDTO.getHeight(),
                null,
                null);
    }

    /**
     * Обновляет существующего пользователя данными из DTO.
     *
     * @param user    Существующий пользователь
     * @param userDTO Новые данные
     */
   public void updateEntity(User user, UserDTO userDTO) {
       if (userDTO == null) {
           return;
       }
       user.setName(userDTO.getName());
       user.setAge(userDTO.getAge());
       user.setWeight(userDTO.getWeight());
       user.setHeight(userDTO.getHeight());
   }
}
