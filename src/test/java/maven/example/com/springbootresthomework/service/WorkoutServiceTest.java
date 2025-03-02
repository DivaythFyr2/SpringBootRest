package maven.example.com.springbootresthomework.service;

import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.dto.WorkoutDTO;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.entity.Workout;
import maven.example.com.springbootresthomework.mapper.WorkoutMapper;
import maven.example.com.springbootresthomework.repository.UserRepository;
import maven.example.com.springbootresthomework.repository.WorkoutRepository;
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
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutMapper workoutMapper;

    @InjectMocks
    private WorkoutService workoutService;

    //Тестовые данные
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_WORKOUT_ID = 1L;
    private static final Long INVALID_ID = 999L;

    private static final User TEST_USER = new User(VALID_USER_ID, "Mikola", 27, 80, 183, null, null);
    private static final Workout TEST_WORKOUT = new Workout(VALID_WORKOUT_ID, "Running", 45, 450, TEST_USER);
    private static final WorkoutDTO TEST_WORKOUT_DTO = new WorkoutDTO("Running", 45);

    @BeforeEach
    void setUp() {
        lenient().when(workoutMapper.toDTO(TEST_WORKOUT)).thenReturn(TEST_WORKOUT_DTO);
        lenient().when(workoutMapper.toEntity(TEST_WORKOUT_DTO, TEST_USER)).thenReturn(TEST_WORKOUT);
        lenient().when(userRepository.existsById(VALID_USER_ID)).thenReturn(true);
    }

    @Test
    void getAllWorkouts_ShouldReturnAllWorkouts() {
        when(workoutRepository.findAll()).thenReturn(List.of(TEST_WORKOUT));

        List<WorkoutDTO> expectedWorkouts = workoutService.getAllWorkouts();

        assertFalse(expectedWorkouts.isEmpty());
        assertEquals(1, expectedWorkouts.size());
        assertEquals(TEST_WORKOUT_DTO.getName(), expectedWorkouts.get(0).getName());
        assertEquals(TEST_WORKOUT_DTO.getDuration(), expectedWorkouts.get(0).getDuration());

        verify(workoutMapper, times(1)).toDTO(TEST_WORKOUT);
    }

    @Test
    void getWorkoutById_WhenWorkoutExists_ShouldReturnWorkoutDTO() {
        when(workoutRepository.findById(VALID_WORKOUT_ID)).thenReturn(Optional.of(TEST_WORKOUT));

        WorkoutDTO expectedWorkout = workoutService.getWorkoutById(VALID_WORKOUT_ID);

        assertNotNull(expectedWorkout);
        assertEquals(TEST_WORKOUT_DTO.getName(), expectedWorkout.getName());
        assertEquals(TEST_WORKOUT_DTO.getDuration(), expectedWorkout.getDuration());

        verify(workoutRepository, times(1)).findById(VALID_WORKOUT_ID);
        verify(workoutMapper, times(1)).toDTO(TEST_WORKOUT);
    }

    @Test
    void getWorkoutById_WhenWorkoutDoesNotExist_ShouldThrowException() {
        when(workoutRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                workoutService.getWorkoutById(INVALID_ID));

        assertEquals("Workout not found", exception.getMessage());
        verify(workoutRepository, times(1)).findById(INVALID_ID);
    }

    @Test
    void getWorkoutsByUserId_WhenUserExists_ShouldReturnWorkouts() {
        when(workoutRepository.findByUserId(VALID_USER_ID)).thenReturn(List.of(TEST_WORKOUT));

        List<WorkoutDTO> expectedWorkouts = workoutService.getWorkoutsByUserId(VALID_USER_ID);

        assertFalse(expectedWorkouts.isEmpty());
        assertEquals(1, expectedWorkouts.size());
        assertEquals(TEST_WORKOUT_DTO.getName(), expectedWorkouts.get(0).getName());
        assertEquals(TEST_WORKOUT_DTO.getDuration(), expectedWorkouts.get(0).getDuration());

        verify(userRepository, times(1)).existsById(VALID_USER_ID);
        verify(workoutMapper, times(1)).toDTO(TEST_WORKOUT);
    }

    @Test
    void getWorkoutsByUserId_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.existsById(INVALID_ID)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                workoutService.getWorkoutsByUserId(INVALID_ID));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(INVALID_ID);
    }

    @Test
    void createWorkout_ShouldCreateWorkout() {
        when(userRepository.findById(VALID_WORKOUT_ID)).thenReturn(Optional.of(TEST_USER));

        workoutService.createWorkout(VALID_USER_ID, TEST_WORKOUT_DTO);

        verify(workoutRepository, times(1)).save(TEST_WORKOUT);
        verify(workoutMapper, times(1)).toEntity(TEST_WORKOUT_DTO, TEST_USER);
    }

    @Test
    void createWorkout_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                workoutService.createWorkout(INVALID_ID, TEST_WORKOUT_DTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(INVALID_ID);
    }

    @Test
    void updateWorkout_ShouldUpdateWorkout() {
        when(workoutRepository.findById(VALID_WORKOUT_ID)).thenReturn(Optional.of(TEST_WORKOUT));

        workoutService.updateWorkout(VALID_WORKOUT_ID, TEST_WORKOUT_DTO);

        verify(workoutMapper, times(1)).updateEntity(TEST_WORKOUT, TEST_WORKOUT_DTO);
        verify(workoutRepository, times(1)).save(TEST_WORKOUT);
    }

    @Test
    void updateWorkout_WhenWorkoutDoesNotExist_ShouldThrowException() {
        when(workoutRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                workoutService.updateWorkout(INVALID_ID, TEST_WORKOUT_DTO));

        assertEquals("Workout not found", exception.getMessage());
        verify(workoutRepository, times(1)).findById(INVALID_ID);
    }

    @Test
    void deleteWorkout_ShouldDeleteWorkout() {
        when(workoutRepository.findById(VALID_WORKOUT_ID)).thenReturn(Optional.of(TEST_WORKOUT));

        workoutService.deleteWorkout(VALID_WORKOUT_ID);

        verify(workoutRepository, times(1)).delete(TEST_WORKOUT);
    }

    @Test
    void deleteWorkout_WhenWorkoutDoesNotExist_ShouldThrowException() {
        when(workoutRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                workoutService.deleteWorkout(INVALID_ID));

        assertEquals("Workout not found", exception.getMessage());
        verify(workoutRepository, times(1)).findById(INVALID_ID);
    }
}