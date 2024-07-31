package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
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