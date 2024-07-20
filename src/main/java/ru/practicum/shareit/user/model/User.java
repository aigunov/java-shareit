package ru.practicum.shareit.user.model;

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
    private String name;
    private String email;
}

