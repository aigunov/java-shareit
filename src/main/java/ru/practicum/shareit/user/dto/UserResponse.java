package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse {
    private long id;
    private String name;
    private String email;
}
