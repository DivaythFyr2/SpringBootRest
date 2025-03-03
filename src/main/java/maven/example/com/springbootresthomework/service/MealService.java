package maven.example.com.springbootresthomework.service;

import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.MealDTO;
import maven.example.com.springbootresthomework.entity.Meal;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.mapper.MealMapper;
import maven.example.com.springbootresthomework.repository.MealRepository;
import maven.example.com.springbootresthomework.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final MealMapper mealMapper;

    public MealService(MealRepository mealRepository, UserRepository userRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.mealMapper = mealMapper;
    }

    /**
     * Получает список всех приёмов пищи.
     *
     * @return Список всех приёмов пищи.
     */
    public List<MealDTO> getAllMeals() {
        return mealRepository.findAll()
                .stream()
                .map(mealMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить приёмы пищи конкретного пользователя.
     *
     * @param userId ID пользователя
     * @return Список приёмов пищи пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    public List<MealDTO> getMealsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(MessageConstants.USER_NOT_FOUND);
        }

        return mealRepository.findByUserId(userId)
                .stream()
                .map(mealMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить приём пищи по его ID.
     *
     * @param id ID приёма пищи
     * @return Найденный приём пищи
     * @throws EntityNotFoundException если приём пищи не найден
     */
    public MealDTO getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.MEAL_NOT_FOUND));

        return mealMapper.toDTO(meal);
    }

    /**
     * Создать новый приём пищи для пользователя.
     *
     * @param userId  ID пользователя
     * @param mealDTO Данные нового приёма пищи
     * @throws EntityNotFoundException если пользователь не найден
     */
    public void createMeal(Long userId, MealDTO mealDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.USER_NOT_FOUND));

        Meal meal = mealMapper.toEntity(mealDTO, user);
        mealRepository.save(meal);
    }

    /**
     * Обновить приём пищи по его ID.
     *
     * @param id      ID приёма пищи
     * @param mealDTO Новые данные
     * @throws EntityNotFoundException если приём пищи не найден
     */
    public void updateMeal(Long id, MealDTO mealDTO) {
        Meal existingMeal = mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.MEAL_NOT_FOUND));

        mealMapper.updateEntity(existingMeal, mealDTO);
        mealRepository.save(existingMeal);
    }

    /**
     * Удалить приём пищи по ID.
     *
     * @param id ID приёма пищи
     * @throws EntityNotFoundException если приём пищи не найден
     */
    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstants.MEAL_NOT_FOUND));

        mealRepository.delete(meal);
    }
}
