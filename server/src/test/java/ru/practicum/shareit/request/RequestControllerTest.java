package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {
    private static final LocalDateTime now = LocalDateTime.now();
    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mvc;
    private RequestResponse requestDto;
    private UserResponse userResponseCreate;

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController controller;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        requestDto = RequestResponse.builder()
                .id(1L)
                .created(now)
                .description("Please give to me")
                .build();

        userResponseCreate = UserResponse.builder()
                .id(1L)
                .name("John")
                .email("john@hotmail.ru")
                .build();
    }

    @Test
    void createRequest_invokeMethod_thenReturnCreatedRequest() throws Exception {
        when(requestService.createRequest(any())).thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", Matchers.is(requestDto.getDescription()), String.class));

        verify(requestService, times(1)).createRequest(any());
    }

    @Test
    void getUserRequests() throws Exception {
        List<RequestResponse> requestResponses = List.of(
                new RequestResponse(1L, "Request 1", null, null),
                new RequestResponse(2L, "Request 2", null, null)
        );

        long userId = 1L;

        when(requestService.getUserRequests(userId)).thenReturn(requestResponses);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestResponses)));

        verify(requestService, times(1)).getUserRequests(userId);
    }

    @Test
    void getAllRequests() throws Exception {
        List<RequestResponse> requestResponses = List.of(
                new RequestResponse(1L, "Request 1", null, null),
                new RequestResponse(2L, "Request 2", null, null)
        );

        when(requestService.getAllRequests(any())).thenReturn(requestResponses);

        mvc.perform(get("/requests/all?from=1&size=2")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestResponses)));

        verify(requestService, times(1)).getAllRequests(any());
    }

    @Test
    void getRequest() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong())).thenReturn(requestDto);

        long userId = 1L;

        mvc.perform(get("/requests/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", Matchers.is(requestDto.getDescription())));

        verify(requestService, times(1)).getRequestById(anyLong(), anyLong());
    }
}