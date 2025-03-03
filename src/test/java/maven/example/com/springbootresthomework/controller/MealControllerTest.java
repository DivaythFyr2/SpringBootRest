package maven.example.com.springbootresthomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.MealDTO;
import maven.example.com.springbootresthomework.exception.GlobalExceptionHandler;
import maven.example.com.springbootresthomework.service.MealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MealControllerTest {

    @Mock
    private MealService mealService;

    @InjectMocks
    private MealController mealController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //Тестовые данные
    private static final String URI_TEMPLATE_MEALS = "/meals";
    private static final String URI_TEMPLATE_MEALS_USER_USER_ID = "/meals/user/{userId}";
    private static final String URI_TEMPLATE_MEALS_ID = "/meals/{id}";

    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_MEAL_ID = 10L;
    private static final Long INVALID_ID = 999L;
    private static final MealDTO TEST_MEAL_DTO = new MealDTO("Pasta", 500, VALID_USER_ID);
    private static final List<MealDTO> TEST_MEAL_LIST = List.of(TEST_MEAL_DTO);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mealController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllMeals_ShouldReturnAllMeals() throws Exception {
        when(mealService.getAllMeals()).thenReturn(TEST_MEAL_LIST);

        mockMvc.perform(get(URI_TEMPLATE_MEALS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is(TEST_MEAL_DTO.getName())));

        verify(mealService, times(1)).getAllMeals();
    }

    @Test
    void getMealsByUserId_WhenUserExists_ShouldReturnMeals() throws Exception {
        when(mealService.getMealsByUserId(VALID_USER_ID)).thenReturn(TEST_MEAL_LIST);

        mockMvc.perform(get(URI_TEMPLATE_MEALS_USER_USER_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is(TEST_MEAL_DTO.getName())));

        verify(mealService, times(1)).getMealsByUserId(VALID_USER_ID);
    }

    @Test
    void getMealsByUserId_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(mealService.getMealsByUserId(INVALID_ID)).thenThrow(new EntityNotFoundException(MessageConstants.USER_NOT_FOUND));

        mockMvc.perform(get(URI_TEMPLATE_MEALS_USER_USER_ID, INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.USER_NOT_FOUND));

        verify(mealService, times(1)).getMealsByUserId(INVALID_ID);
    }

    @Test
    void getMealById_WhenMealExists_ShouldReturnMeal() throws Exception {
        when(mealService.getMealById(VALID_MEAL_ID)).thenReturn(TEST_MEAL_DTO);

        mockMvc.perform(get(URI_TEMPLATE_MEALS_ID, VALID_MEAL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(TEST_MEAL_DTO.getName())))
                .andExpect(jsonPath("$.calories", is(TEST_MEAL_DTO.getCalories())));

        verify(mealService, times(1)).getMealById(VALID_MEAL_ID);
    }

    @Test
    void getMealById_WhenMealDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(mealService.getMealById(INVALID_ID)).thenThrow(new EntityNotFoundException(MessageConstants.MEAL_NOT_FOUND));

        mockMvc.perform(get(URI_TEMPLATE_MEALS_ID, INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.MEAL_NOT_FOUND));

        verify(mealService, times(1)).getMealById(INVALID_ID);
    }

    @Test
    void createMeal_ShouldReturnCreated() throws Exception {
        doNothing().when(mealService).createMeal(VALID_USER_ID, TEST_MEAL_DTO);

        mockMvc.perform(post(URI_TEMPLATE_MEALS_USER_USER_ID, VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_MEAL_DTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(MessageConstants.MEAL_CREATED));

        verify(mealService, times(1)).createMeal(VALID_USER_ID, TEST_MEAL_DTO);
    }

    @Test
    void createMeal_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.USER_NOT_FOUND))
                .when(mealService).createMeal(eq(INVALID_ID), any(MealDTO.class));

        mockMvc.perform(post(URI_TEMPLATE_MEALS_USER_USER_ID, INVALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_MEAL_DTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.USER_NOT_FOUND));

        verify(mealService, times(1)).createMeal(eq(INVALID_ID), any(MealDTO.class));
    }

    @Test
    void updateMeal_ShouldReturnOk() throws Exception {
        doNothing().when(mealService).updateMeal(VALID_MEAL_ID, TEST_MEAL_DTO);

        mockMvc.perform(put(URI_TEMPLATE_MEALS_ID, VALID_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_MEAL_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MessageConstants.MEAL_UPDATED));

        verify(mealService, times(1)).updateMeal(VALID_MEAL_ID, TEST_MEAL_DTO);
    }

    @Test
    void updateMeal_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.MEAL_NOT_FOUND))
                .when(mealService).updateMeal(INVALID_ID, TEST_MEAL_DTO);

        mockMvc.perform(put(URI_TEMPLATE_MEALS_ID, INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_MEAL_DTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.MEAL_NOT_FOUND));

        verify(mealService, times(1)).updateMeal(INVALID_ID, TEST_MEAL_DTO);
    }

    @Test
    void deleteMeal_ShouldReturnOk() throws Exception {
        doNothing().when(mealService).deleteMeal(VALID_MEAL_ID);

        mockMvc.perform(delete(URI_TEMPLATE_MEALS_ID, VALID_MEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MessageConstants.MEAL_DELETED));

        verify(mealService, times(1)).deleteMeal(VALID_MEAL_ID);
    }

    @Test
    void deleteMeal_WhenMealDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.MEAL_NOT_FOUND))
                .when(mealService).deleteMeal(INVALID_ID);

        mockMvc.perform(delete(URI_TEMPLATE_MEALS_ID, INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.MEAL_NOT_FOUND));

        verify(mealService, times(1)).deleteMeal(INVALID_ID);
    }
}