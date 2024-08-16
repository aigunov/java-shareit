package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private UserCreate userCreate;
    private UserUpdate userUpdate;
    private UserResponse userResponseCreate, userResponseUpdate;
    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        userCreate = UserCreate.builder()
                .name("John")
                .email("john@hotmail.ru")
                .build();

        userResponseCreate = UserResponse.builder()
                .id(1L)
                .name("John")
                .email("john@hotmail.ru")
                .build();

        userUpdate = UserUpdate.builder()
                .id(1L)
                .name("New John")
                .email("newJohn@hotmail.ru")
                .build();

        userResponseUpdate = UserResponse.builder()
                .id(1L)
                .name("New John")
                .email("newJohn@hotmail.ru")
                .build();
    }

    @Test
    void addUser_whenInvoke_thenReturnNewUser() throws Exception {
        when(service.addUser(userCreate)).thenReturn(userResponseCreate);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsBytes(userCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(userResponseCreate.getId()), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(userResponseCreate.getName())))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseCreate.getEmail())));

        verify(service, times(1)).addUser(userCreate);
    }

    @Test
    void updateUser_whenInvoke_thenReturnUpdatedUser() throws Exception {
        when(service.updateUser(userUpdate)).thenReturn(userResponseUpdate);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsBytes(userUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(userResponseUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(userResponseUpdate.getName())))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseUpdate.getEmail())));

        verify(service, times(1)).updateUser(userUpdate);
    }

    @Test
    void getUser() throws Exception {
        when(service.getUser(1L)).thenReturn(userResponseCreate);

        mvc.perform(get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(userResponseCreate.getId()), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(userResponseCreate.getName())))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseCreate.getEmail())));


        verify(service, times(1)).getUser(1L);
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteUser(1L);
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserResponse> userResponses = List.of(
                new UserResponse(1L, "John Doe", "john.doe@example.com"),
                new UserResponse(2L, "Jane Doe", "jane.doe@example.com")
        );

        when(service.getAllUsers()).thenReturn(userResponses);

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/users")
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(userResponses)));
    }
}