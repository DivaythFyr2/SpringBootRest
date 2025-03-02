package maven.example.com.springbootresthomework.service;

import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.dto.MealDTO;
import maven.example.com.springbootresthomework.entity.Meal;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.mapper.MealMapper;
import maven.example.com.springbootresthomework.repository.MealRepository;
import maven.example.com.springbootresthomework.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealMapper mealMapper;

    @InjectMocks
    private MealService mealService;

    // Тестовые данные
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_MEAL_ID = 10L;
    private static final Long INVALID_ID = 999L;

    private static final User TEST_USER = new User(VALID_USER_ID, "Alice", 25, 65, 170, null, null);
    private static final Meal TEST_MEAL = new Meal(VALID_MEAL_ID, "Pasta", 500, TEST_USER);
    private static final MealDTO TEST_MEAL_DTO = new MealDTO("Pasta", 500, VALID_USER_ID);

    @BeforeEach
    void setUp() {
        lenient().when(mealMapper.toDTO(TEST_MEAL)).thenReturn(TEST_MEAL_DTO);
        lenient().when(mealMapper.toEntity(TEST_MEAL_DTO, TEST_USER)).thenReturn(TEST_MEAL);
        lenient().when(userRepository.existsById(VALID_USER_ID)).thenReturn(true);
    }

    @Test
    void getAllMeals_ShouldReturnAllMeals() {
        when(mealRepository.findAll()).thenReturn(List.of(TEST_MEAL));

        List<MealDTO> expectedMeals = mealService.getAllMeals();

        assertFalse(expectedMeals.isEmpty());
        assertEquals(1, expectedMeals.size());
        assertEquals(TEST_MEAL_DTO.getName(), expectedMeals.get(0).getName());
        assertEquals(TEST_MEAL_DTO.getCalories(), expectedMeals.get(0).getCalories());

        verify(mealMapper, times(1)).toDTO(TEST_MEAL);
    }

    @Test
    void getMealsByUserId_WhenUserExist_ShouldReturnMeals() {
        when(mealRepository.findByUserId(VALID_USER_ID)).thenReturn(List.of(TEST_MEAL));

        List<MealDTO> expectedMeals = mealService.getMealsByUserId(VALID_USER_ID);

        assertFalse(expectedMeals.isEmpty());
        assertEquals(1, expectedMeals.size());
        assertEquals(TEST_MEAL_DTO.getName(), expectedMeals.get(0).getName());
        assertEquals(TEST_MEAL_DTO.getCalories(), expectedMeals.get(0).getCalories());

        verify(userRepository, times(1)).existsById(VALID_USER_ID);
        verify(mealRepository, times(1)).findByUserId(VALID_USER_ID);
    }

    @Test
    void getMealsByUserId_WhenUserDoesNotExist_ShouldReturnThrowException() {
        when(userRepository.existsById(INVALID_ID)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mealService.getMealsByUserId(INVALID_ID));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(INVALID_ID);
    }

    @Test
    void getMealById_WhenMealExist_ShouldReturnMealDTO() {
        when(mealRepository.findById(VALID_MEAL_ID)).thenReturn(Optional.of(TEST_MEAL));

        MealDTO expectedMeal = mealService.getMealById(VALID_MEAL_ID);

        assertNotNull(expectedMeal);
        assertEquals(TEST_MEAL_DTO.getName(), expectedMeal.getName());
        verify(mealRepository, times(1)).findById(VALID_MEAL_ID);
    }

    @Test
    void getMealById_WhenMealDoesNotExist_ShouldReturnThrowException() {
        when(mealRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mealService.getMealById(INVALID_ID));

        assertEquals("Meal not found", exception.getMessage());
        verify(mealRepository, times(1)).findById(INVALID_ID);
    }

    @Test
    void createMeal_ShouldSaveMeal() {
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(TEST_USER));

        mealService.createMeal(VALID_USER_ID, TEST_MEAL_DTO);

        verify(mealRepository, times(1)).save(TEST_MEAL);
        verify(mealMapper, times(1)).toEntity(TEST_MEAL_DTO, TEST_USER);
    }

    @Test
    void createMeal_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mealService.createMeal(INVALID_ID, TEST_MEAL_DTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(INVALID_ID);
    }

    @Test
    void updateMeal_ShouldUpdateMeal() {
        when(mealRepository.findById(VALID_MEAL_ID)).thenReturn(Optional.of(TEST_MEAL));

        mealService.updateMeal(VALID_MEAL_ID, TEST_MEAL_DTO);

        verify(mealMapper, times(1)).updateEntity(TEST_MEAL, TEST_MEAL_DTO);
        verify(mealRepository, times(1)).save(TEST_MEAL);
    }

    @Test
    void updateMeal_WhenMealDoesNotExist_ShouldThrowException() {
        when(mealRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mealService.updateMeal(INVALID_ID, TEST_MEAL_DTO));

        assertEquals("Meal not found", exception.getMessage());
        verify(mealRepository, times(1)).findById(INVALID_ID);
    }

    @Test
    void deleteMeal_ShouldDeleteMeal() {
        when(mealRepository.findById(VALID_MEAL_ID)).thenReturn(Optional.of(TEST_MEAL));

        mealService.deleteMeal(VALID_MEAL_ID);

        verify(mealRepository, times(1)).delete(TEST_MEAL);
    }

    @Test
    void deleteMeal_WhenMealDoesNotExist_ShouldThrowException() {
        when(mealRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mealService.deleteMeal(INVALID_ID));

        assertEquals("Meal not found", exception.getMessage());
        verify(mealRepository, times(1)).findById(INVALID_ID);
    }
}