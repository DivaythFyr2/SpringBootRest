package maven.example.com.springbootresthomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.WorkoutDTO;
import maven.example.com.springbootresthomework.exception.GlobalExceptionHandler;
import maven.example.com.springbootresthomework.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.is;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WorkoutControllerTest {

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutController workoutController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Тестовые данные
    private final static String URI_TEMPLATE_WORKOUTS = "/workouts";
    private final static String URI_TEMPLATE_WORKOUTS_ID = "/workouts/{id}";
    private final static String URI_TEMPLATE_WORKOUTS_USER_USER_ID = "/workouts/user/{userId}";

    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_WORKOUT_ID = 10L;
    private static final Long INVALID_ID = 999L;

    private static final WorkoutDTO TEST_WORKOUT_DTO = new WorkoutDTO("Running", 60);
    private static final List<WorkoutDTO> TEST_WORKOUT_LIST = List.of(TEST_WORKOUT_DTO);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(workoutController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllWorkouts_ShouldReturnAllWorkouts() throws Exception {
        when(workoutService.getAllWorkouts()).thenReturn(TEST_WORKOUT_LIST);

        mockMvc.perform(get(URI_TEMPLATE_WORKOUTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is(TEST_WORKOUT_DTO.getName())));

        verify(workoutService, times(1)).getAllWorkouts();
    }

    @Test
    void getWorkoutById_WhenWorkoutExists_ShouldReturnWorkout() throws Exception {
        when(workoutService.getWorkoutById(VALID_WORKOUT_ID)).thenReturn(TEST_WORKOUT_DTO);

        mockMvc.perform(get(URI_TEMPLATE_WORKOUTS_ID, VALID_WORKOUT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(TEST_WORKOUT_DTO.getName())))
                .andExpect(jsonPath("$.duration", is(TEST_WORKOUT_DTO.getDuration())));

        verify(workoutService, times(1)).getWorkoutById(VALID_WORKOUT_ID);
    }

    @Test
    void getWorkoutById_WhenWorkoutDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(workoutService.getWorkoutById(INVALID_ID)).thenThrow(new EntityNotFoundException(MessageConstants.WORKOUT_NOT_FOUND));

        mockMvc.perform(get(URI_TEMPLATE_WORKOUTS_ID, INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.WORKOUT_NOT_FOUND));

        verify(workoutService, times(1)).getWorkoutById(INVALID_ID);
    }

    @Test
    void getWorkoutsByUserId_WhenUserExists_ShouldReturnWorkouts() throws Exception {
        when(workoutService.getWorkoutsByUserId(VALID_USER_ID)).thenReturn(TEST_WORKOUT_LIST);

        mockMvc.perform(get(URI_TEMPLATE_WORKOUTS_USER_USER_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is(TEST_WORKOUT_DTO.getName())))
                .andExpect(jsonPath("$[0].duration", is(TEST_WORKOUT_DTO.getDuration())));

        verify(workoutService, times(1)).getWorkoutsByUserId(VALID_USER_ID);
    }

    @Test
    void createWorkout_ShouldReturnCreated() throws Exception {
        doNothing().when(workoutService).createWorkout(VALID_USER_ID, TEST_WORKOUT_DTO);

        mockMvc.perform(post(URI_TEMPLATE_WORKOUTS_USER_USER_ID, VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_WORKOUT_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MessageConstants.WORKOUT_CREATED));

        verify(workoutService, times(1)).createWorkout(VALID_USER_ID, TEST_WORKOUT_DTO);
    }

    @Test
    void createWorkout_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.USER_NOT_FOUND))
                .when(workoutService).createWorkout(eq(INVALID_ID), any(WorkoutDTO.class));

        mockMvc.perform(post(URI_TEMPLATE_WORKOUTS_USER_USER_ID, INVALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_WORKOUT_DTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.USER_NOT_FOUND));

        verify(workoutService, times(1)).createWorkout(INVALID_ID, TEST_WORKOUT_DTO);
    }

    @Test
    void updateWorkout_ShouldReturnOk() throws Exception {
        doNothing().when(workoutService).updateWorkout(VALID_WORKOUT_ID, TEST_WORKOUT_DTO);

        mockMvc.perform(put(URI_TEMPLATE_WORKOUTS_ID, VALID_WORKOUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_WORKOUT_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MessageConstants.WORKOUT_UPDATED));

        verify(workoutService, times(1)).updateWorkout(VALID_WORKOUT_ID, TEST_WORKOUT_DTO);
    }

    @Test
    void updateWorkout_WhenWorkoutDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.WORKOUT_NOT_FOUND))
                .when(workoutService).updateWorkout(INVALID_ID, TEST_WORKOUT_DTO);

        mockMvc.perform(put(URI_TEMPLATE_WORKOUTS_ID, INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_WORKOUT_DTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.WORKOUT_NOT_FOUND));

        verify(workoutService, times(1)).updateWorkout(INVALID_ID, TEST_WORKOUT_DTO);
    }

    @Test
    void deleteWorkout_ShouldReturnOk() throws Exception {
        doNothing().when(workoutService).deleteWorkout(VALID_WORKOUT_ID);

        mockMvc.perform(delete(URI_TEMPLATE_WORKOUTS_ID, VALID_WORKOUT_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MessageConstants.WORKOUT_DELETED));

        verify(workoutService, times(1)).deleteWorkout(VALID_WORKOUT_ID);
    }

    @Test
    void deleteWorkout_WhenWorkoutDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.WORKOUT_NOT_FOUND))
                .when(workoutService).deleteWorkout(INVALID_ID);

        mockMvc.perform(delete(URI_TEMPLATE_WORKOUTS_ID, INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.WORKOUT_NOT_FOUND));

        verify(workoutService, times(1)).deleteWorkout(INVALID_ID);
    }
}