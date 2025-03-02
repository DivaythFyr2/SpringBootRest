package maven.example.com.springbootresthomework.service;


import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.WorkoutDTO;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.entity.Workout;
import maven.example.com.springbootresthomework.mapper.WorkoutMapper;
import maven.example.com.springbootresthomework.repository.UserRepository;
import maven.example.com.springbootresthomework.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с тренировками.
 */
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.workoutMapper = workoutMapper;
    }

    /**
     * Получить список всех тренировок.
     *
     * @return список тренировок
     */
    public List<WorkoutDTO> getAllWorkouts() {
        return workoutRepository.findAll()
                .stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить тренировку по ID.
     *
     * @param workoutId идентификатор тренировки
     * @return найденная тренировка
     * @throws EntityNotFoundException если тренировка не найдена
     */
    public WorkoutDTO getWorkoutById(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.WORKOUT_NOT_FOUND));

        return workoutMapper.toDTO(workout);
    }

    /**
     * Получить список тренировок пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список тренировок пользователя
     */
    public List<WorkoutDTO> getWorkoutsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(MessageConstants.USER_NOT_FOUND);
        }

        return workoutRepository.findByUserId(userId)
                .stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Создать новую тренировку для пользователя.
     * Автоматически рассчитывает `caloriesBurned` в зависимости от типа тренировки.
     *
     * @param userId     идентификатор пользователя
     * @param workoutDTO данные тренировки
     */
    public void createWorkout(Long userId, WorkoutDTO workoutDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.USER_NOT_FOUND));

        Workout workout = workoutMapper.toEntity(workoutDTO, user);
        workoutRepository.save(workout);
    }

    /**
     * Обновить данные тренировки.
     *
     * @param workoutId  идентификатор тренировки
     * @param workoutDTO новые данные тренировки
     * @throws EntityNotFoundException если тренировка не найдена
     */
    public void updateWorkout(Long workoutId, WorkoutDTO workoutDTO) {
        Workout existingWorkout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.WORKOUT_NOT_FOUND));

        workoutMapper.updateEntity(existingWorkout, workoutDTO);
        workoutRepository.save(existingWorkout);
    }

    /**
     * Удалить тренировку по ID.
     *
     * @param workoutId идентификатор тренировки
     * @throws EntityNotFoundException если тренировка не найдена
     */
    public void deleteWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.WORKOUT_NOT_FOUND));

        workoutRepository.delete(workout);
    }
}
