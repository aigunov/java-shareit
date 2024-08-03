package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class CommentCreate {
    @NotBlank
    private String text;
    private long authorId;
    private long itemId;
}
