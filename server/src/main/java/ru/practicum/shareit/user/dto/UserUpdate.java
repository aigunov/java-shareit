package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.White
 * DTO для user при обновлении
 * поскольку в отличие от обновления полей
 * создание нового user требует всех данных
 * необходимо создать разные DTO для user
 */
@AllArgsConstructor
@Data
@Builder
public class UserUpdate {
    private long id;
    private String name;
    private String email;
}
