package ru.practicum.shareit.user.dto;


import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoJsonTest {
    private final JacksonTester<UserCreate> jsonCreate;
    private final JacksonTester<UserUpdate> jsonUpdate;
    private final JacksonTester<UserResponse> jsonResponse;

    @Test
    void userCreateTest() throws Exception {
        var user = new UserCreate("adolf", "adolf@hotmail.ru");
        var result = jsonCreate.write(user);

        Assertions.assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("adolf");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("adolf@hotmail.ru");
    }

    @Test
    void userUpdateTest() throws Exception {
        var update = new UserUpdate(1L, "antony", "antony@hotmail.ru");
        var result = jsonUpdate.write(update);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("antony");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("antony@hotmail.ru");
    }

    @Test
    void userResponseTest() throws Exception {
        var response = new UserResponse(1L, "micha", "micha@hotmail.ru");
        var result = jsonResponse.write(response);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("micha");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("micha@hotmail.ru");
    }

}
