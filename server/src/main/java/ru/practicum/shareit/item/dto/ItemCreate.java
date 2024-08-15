package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.White
 * DTO для Item при создание
 * поскольку в отличие от обновления полей
 * создание нового item требует всех данных
 * необходимо создать разные DTO для item
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemCreate {
    private long ownerId;

    private String name;
    private String description;
    private Boolean available;
    private long requestId;
}
