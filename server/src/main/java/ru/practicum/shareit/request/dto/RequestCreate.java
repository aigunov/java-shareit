package ru.practicum.shareit.request.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreate {
    @CreationTimestamp
    private final LocalDateTime created = LocalDateTime.now();
    private long requesterId;
    private String description;
    private User requester;
}
