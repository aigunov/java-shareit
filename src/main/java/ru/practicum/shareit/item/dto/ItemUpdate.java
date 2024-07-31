package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.White
 * DTO для Item при обновлении
 * поскольку в отличие от обновления полей
 * создание нового item требует всех данных
 * необходимо создать разные DTO для item
 */
@Builder(toBuilder = true)
@Data
public class ItemUpdate {
    private long id;
    private long ownerId;
    private String name;
    private String description;
    private Boolean available;
}
