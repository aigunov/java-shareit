package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.White
 * DTO для user при создании
 * поскольку в отличие от обновления полей
 * создание нового user требует всех данных
 * необходимо создать разные DTO для user
 */
@Builder
@Data
public class UserDtoCreate {
    private String name;
    @NotEmpty
    @Email
    private String email;
}
