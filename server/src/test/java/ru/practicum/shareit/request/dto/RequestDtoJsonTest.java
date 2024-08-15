package ru.practicum.shareit.request.dto;


import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestDtoJsonTest {
    private final JacksonTester<RequestCreate> jsonCreate;
    private final JacksonTester<RequestResponse> jsonResponse;

    @Test
    void requestCreate() throws Exception {
        var request = new RequestCreate(1, "request", new User());
        var result = jsonCreate.write(request);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("request");
    }

    @Test
    void requestResponse() throws Exception {
        var request = new RequestResponse(1, "request", LocalDateTime.now(), null);
        var result = jsonResponse.write(request);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("request");
    }
}
