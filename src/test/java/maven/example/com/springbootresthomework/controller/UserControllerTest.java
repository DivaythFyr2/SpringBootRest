package maven.example.com.springbootresthomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import maven.example.com.springbootresthomework.constants.MessageConstants;
import maven.example.com.springbootresthomework.dto.UserDTO;
import maven.example.com.springbootresthomework.exception.GlobalExceptionHandler;
import maven.example.com.springbootresthomework.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Тестовые данные
    private static final String URI_TEMPLATE_USERS = "/users";
    private static final String URI_TEMPLATE_USERS_ID = "/users/{id}";

    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_ID = 999L;
    private static final UserDTO TEST_USER_DTO = new UserDTO("Nikolai", 28, 79, 185);
    private static final List<UserDTO> TEST_USER_LIST = List.of(TEST_USER_DTO);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(TEST_USER_LIST);

        mockMvc.perform(get(URI_TEMPLATE_USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is(TEST_USER_DTO.getName())));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(TEST_USER_DTO);

        mockMvc.perform(get(URI_TEMPLATE_USERS_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(TEST_USER_DTO.getName())))
                .andExpect(jsonPath("$.age", is(TEST_USER_DTO.getAge())));

        verify(userService, times(1)).getUserById(VALID_USER_ID);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(userService.getUserById(INVALID_ID)).thenThrow(new EntityNotFoundException(MessageConstants.USER_NOT_FOUND));

        mockMvc.perform(get(URI_TEMPLATE_USERS_ID, INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.USER_NOT_FOUND));

        verify(userService, times(1)).getUserById(INVALID_ID);
    }

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        doNothing().when(userService).createUser(TEST_USER_DTO);

        mockMvc.perform(post(URI_TEMPLATE_USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_USER_DTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(MessageConstants.USER_CREATED));

        verify(userService, times(1)).createUser(TEST_USER_DTO);
    }

    @Test
    void updateUser_ShouldReturnOk() throws Exception {
        doNothing().when(userService).updateUser(VALID_USER_ID, TEST_USER_DTO);

        mockMvc.perform(put(URI_TEMPLATE_USERS_ID, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_USER_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MessageConstants.USER_UPDATED));

        verify(userService, times(1)).updateUser(VALID_USER_ID, TEST_USER_DTO);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.USER_NOT_FOUND))
                .when(userService).updateUser(INVALID_ID, TEST_USER_DTO);

        mockMvc.perform(put(URI_TEMPLATE_USERS_ID, INVALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_USER_DTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.USER_NOT_FOUND));

        verify(userService, times(1)).updateUser(INVALID_ID, TEST_USER_DTO);
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        doNothing().when(userService).deleteUser(VALID_USER_ID);

        mockMvc.perform(delete(URI_TEMPLATE_USERS_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MessageConstants.USER_DELETED));

        verify(userService, times(1)).deleteUser(VALID_USER_ID);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException(MessageConstants.USER_NOT_FOUND))
                .when(userService).deleteUser(INVALID_ID);

        mockMvc.perform(delete(URI_TEMPLATE_USERS_ID, INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MessageConstants.USER_NOT_FOUND));

        verify(userService, times(1)).deleteUser(INVALID_ID);
    }
}