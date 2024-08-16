package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.White
 * DTO для user при создании
 * поскольку в отличие от обновления полей
 * создание нового user требует всех данных
 * необходимо создать разные DTO для user
 */
@AllArgsConstructor
@Builder
@Data
public class UserCreate {
    private String name;
    @NotBlank
    @Email
    private String email;
}
