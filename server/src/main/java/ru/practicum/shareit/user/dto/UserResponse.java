package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String name;
    private String email;
}
