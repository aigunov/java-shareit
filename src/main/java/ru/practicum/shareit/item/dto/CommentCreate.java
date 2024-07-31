package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class CommentCreate {
    @NotNull
    @NotEmpty
    private String text;
    private long authorId;
    private long itemId;
}
