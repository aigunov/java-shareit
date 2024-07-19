package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.White
 * DTO для user при обновлении
 * поскольку в отличие от обновления полей
 * создание нового user требует всех данных
 * необходимо создать разные DTO для user
 */
@Data
@Builder
public class UserDtoToUpdate {
    private String name;
    private String email;
}
