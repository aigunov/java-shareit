package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.White
 * POJO класс User
 */
@Data
@Builder
public class User {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
}

