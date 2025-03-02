package maven.example.com.springbootresthomework.service;

import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.UserDTO;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.mapper.UserMapper;
import maven.example.com.springbootresthomework.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Сервис для работы с пользователями.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Получить список всех пользователей.
     *
     * @return Список пользователей
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить пользователя по ID.
     *
     * @param id Идентификатор пользователя
     * @return Найденный пользователь
     * @throws EntityNotFoundException если пользователь не найден
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.USER_NOT_FOUND));

        return userMapper.toDTO(user);
    }

    /**
     * Создать нового пользователя.
     *
     * @param userDTO Данные пользователя
     */
    public void createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);
    }

    /**
     * Обновить данные пользователя по ID.
     *
     * @param id      Идентификатор пользователя
     * @param userDTO Новые данные
     * @throws EntityNotFoundException если пользователь не найден
     */
    public void updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.USER_NOT_FOUND));

        userMapper.updateEntity(existingUser, userDTO);
        userRepository.save(existingUser);
    }

    /**
     * Удалить пользователя по ID.
     *
     * @param id Идентификатор пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}