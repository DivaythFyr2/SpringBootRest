package maven.example.com.springbootresthomework.service;

import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.dto.UserDTO;
import maven.example.com.springbootresthomework.entity.User;
import maven.example.com.springbootresthomework.mapper.UserMapper;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    // Тестовые данные
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 999L;

    private static final User TEST_USER = new User(VALID_USER_ID, "Mikola", 27, 80, 183, null, null);
    private static final UserDTO TEST_USER_DTO = new UserDTO("Mikola", 27, 80, 183);

    @BeforeEach
    void setUp() {
        lenient().when(userMapper.toDTO(TEST_USER)).thenReturn(TEST_USER_DTO);
        lenient().when(userMapper.toEntity(TEST_USER_DTO)).thenReturn(TEST_USER);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(TEST_USER));

        List<UserDTO> expectedUsers = userService.getAllUsers();

        assertFalse(expectedUsers.isEmpty());
        assertEquals(1, expectedUsers.size());
        assertEquals(expectedUsers.get(0).getName(), TEST_USER_DTO.getName());
        assertEquals(expectedUsers.get(0).getAge(), TEST_USER_DTO.getAge());

        verify(userMapper, times(1)).toDTO(TEST_USER);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(TEST_USER));

        UserDTO expectedUser = userService.getUserById(VALID_USER_ID);

        assertNotNull(expectedUser);
        assertEquals(TEST_USER_DTO.getName(), expectedUser.getName());
        assertEquals(TEST_USER_DTO.getAge(), expectedUser.getAge());

        verify(userRepository, times(1)).findById(VALID_USER_ID);
        verify(userMapper, times(1)).toDTO(TEST_USER);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.getUserById(INVALID_USER_ID));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(INVALID_USER_ID);
    }

    @Test
    void createUser_ShouldCreateUser() {
        userService.createUser(TEST_USER_DTO);

        verify(userRepository, times(1)).save(TEST_USER);
        verify(userMapper, times(1)).toEntity(TEST_USER_DTO);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(TEST_USER));

        userService.updateUser(VALID_USER_ID, TEST_USER_DTO);

        verify(userMapper, times(1)).updateEntity(TEST_USER, TEST_USER_DTO);
        verify(userRepository, times(1)).save(TEST_USER);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.updateUser(INVALID_USER_ID, TEST_USER_DTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(INVALID_USER_ID);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(TEST_USER));

        userService.deleteUser(VALID_USER_ID);

        verify(userRepository, times(1)).findById(VALID_USER_ID);
        verify(userRepository, times(1)).delete(TEST_USER);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.deleteUser(INVALID_USER_ID));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(INVALID_USER_ID);
    }
}