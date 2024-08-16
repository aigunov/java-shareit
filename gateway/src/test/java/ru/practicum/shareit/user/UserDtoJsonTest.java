package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserUpdate;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoJsonTest {
    private final JacksonTester<UserCreate> jsonCreate;
    private final JacksonTester<UserUpdate> jsonUpdate;

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
}
