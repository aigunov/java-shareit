package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.RequestCreate;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestDtoJsonTest {
    private final JacksonTester<RequestCreate> jsonCreate;

    @Test
    void requestCreate() throws Exception {
        var request = RequestCreate.builder()
                .requesterId(1)
                .description("request")
                .build();
        var result = jsonCreate.write(request);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("request");
    }
}
