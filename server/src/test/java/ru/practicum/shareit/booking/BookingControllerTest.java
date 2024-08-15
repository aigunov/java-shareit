package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    private static final String HEADER = "X-Sharer-User-Id";
    private final long userId = 1L, bookingId = 1L, itemId = 1L;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private LocalDateTime now, nextDay;
    private MockMvc mvc;
    private BookingCreate create;
    private BookingResponse response;

    @Mock
    private BookingService service;

    @InjectMocks
    private BookingController controller;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nextDay = now.plusDays(1);

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        create = BookingCreate.builder()
                .itemId(itemId)
                .bookerId(userId)
                .start(now)
                .end(nextDay)
                .build();

        response = BookingResponse.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .item(BookingResponse.ItemBookingInfo.builder()
                        .name("item name")
                        .id(1L)
                        .build())
                .start(now)
                .end(nextDay)
                .build();
    }

    @Test
    void addBooking_whenInvokeMethod_thenReturnCreatedBooking() throws Exception {

        when(service.addBooking(any())).thenReturn(response);

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(service, times(1)).addBooking(any());

    }

    @Test
    void decideRent_whenInvokeMethod_thenReturnBookingWhichDecided() throws Exception {
        response.setStatus(Status.APPROVED);

        when(service.decideRent(anyLong(), anyLong(), anyBoolean())).thenReturn(response);

        mvc.perform(patch("/bookings/" + bookingId + "?approved=" + (Math.random() % 2 == 0.0))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.status", anyOf(is("APPROVED"), is("REJECTED"))));

        verify(service, times(1)).decideRent(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getBooking_whenInvokeMethod_thenReturnBooking() throws Exception {
        when(service.getBooking(anyLong(), anyLong())).thenReturn(response);

        mvc.perform(get("/bookings/" + bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(service, times(1)).getBooking(anyLong(), anyLong());

    }

    @Test
    void getBookingsByStatus_whenInvokeMethod_thenReturnBookingsByStatus() throws Exception {
        when(service.findUserBookingByStatus(userId, Status.ALL)).thenReturn(
                List.of(
                        new BookingResponse(1L, now, nextDay, Status.ALL, null, null),
                        new BookingResponse(2L, now, nextDay, Status.ALL, null, null),
                        new BookingResponse(3L, now, nextDay, Status.ALL, null, null),
                        new BookingResponse(4L, now, nextDay, Status.ALL, null, null)
                ));

        mvc.perform(get("/bookings?status=" + Status.ALL.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].status", is("ALL")))
                .andExpect(jsonPath("$.[1].status", is("ALL")))
                .andExpect(jsonPath("$.[2].status", is("ALL")))
                .andExpect(jsonPath("$.[3].status", is("ALL")))
                .andExpect(jsonPath("$.[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$.[1].id", is(2L), Long.class))
                .andExpect(jsonPath("$.[2].id", is(3L), Long.class))
                .andExpect(jsonPath("$.[3].id", is(4L), Long.class));
    }

    @Test
    void getOwnerBookingsByStatus() throws Exception {
        when(service.findOwnerBookingByStatus(userId, Status.ALL)).thenReturn(
                List.of(
                        new BookingResponse(1L, now, nextDay, Status.ALL, null, null),
                        new BookingResponse(2L, now, nextDay, Status.ALL, null, null),
                        new BookingResponse(3L, now, nextDay, Status.ALL, null, null),
                        new BookingResponse(4L, now, nextDay, Status.ALL, null, null)
                ));

        mvc.perform(get("/bookings/owner?status=" + Status.ALL.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].status", is("ALL")))
                .andExpect(jsonPath("$.[1].status", is("ALL")))
                .andExpect(jsonPath("$.[2].status", is("ALL")))
                .andExpect(jsonPath("$.[3].status", is("ALL")))
                .andExpect(jsonPath("$.[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$.[1].id", is(2L), Long.class))
                .andExpect(jsonPath("$.[2].id", is(3L), Long.class))
                .andExpect(jsonPath("$.[3].id", is(4L), Long.class));

    }
}