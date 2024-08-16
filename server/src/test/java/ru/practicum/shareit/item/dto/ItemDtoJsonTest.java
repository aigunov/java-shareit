package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoJsonTest {
    private final JacksonTester<ItemCreate> jsonCreate;
    private final JacksonTester<ItemUpdate> jsonUpdate;
    private final JacksonTester<ItemResponse> jsonResponse;
    private final JacksonTester<CommentCreate> jsonCommentCreate;
    private final JacksonTester<CommentResponse> jsonCommentResponse;

    @Test
    void commentCreate() throws Exception {
        var comment = new CommentCreate("wow", 1, 1);
        var result = jsonCommentCreate.write(comment);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.authorId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("wow");
    }

    @Test
    void setJsonCommentResponse() throws Exception {
        var comment = new CommentResponse(1, "wow", "name", null);
        var result = jsonCommentResponse.write(comment);

        Assertions.assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("wow");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("name");
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void itemCreateTest() throws Exception {
        var item = new ItemCreate(1, "item", "item description", true, 1);
        var result = jsonCreate.write(item);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("item description");
        Assertions.assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
    }

    @Test
    void itemUpdateTest() throws Exception {
        var item = new ItemUpdate(1, 1, "name", "description", true);
        var result = jsonUpdate.write(item);


        Assertions.assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        Assertions.assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void itemResponseTest() throws Exception {
        var item = new ItemResponse(null, null, null, 1, "name", "description", true);
        var result = jsonResponse.write(item);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        Assertions.assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
    }
}
