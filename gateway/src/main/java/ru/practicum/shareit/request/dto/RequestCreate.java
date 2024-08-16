package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreate {
    private final LocalDateTime created = LocalDateTime.now();
    private long requesterId;
    @NotBlank
    private String description;
}
