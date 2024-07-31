package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentResponse {
    private long id;
    @NotNull
    @NotEmpty
    private String text;
    @NotNull
    @NotEmpty
    private String authorName;
    @NotNull
    @NotEmpty
    private LocalDateTime created;
}