package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoJsonTest {
    private final JacksonTester<BookingCreate> jsonCreate;
    private final JacksonTester<BookingResponse> jsonResponse;

    @Test
    void bookingCreateTest() throws Exception {
        var booking = new BookingCreate(1L, 1L, null, null);
        var result = jsonCreate.write(booking);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }

    @Test
    void bookingResponseTest() throws Exception {
        var booking = new BookingResponse(1L, null, null, Status.APPROVED, new User(), null);
        var result = jsonResponse.write(booking);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

}
