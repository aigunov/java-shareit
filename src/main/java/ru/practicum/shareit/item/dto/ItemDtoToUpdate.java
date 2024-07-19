package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.coyote.Request;

/**
 * @author Mr.White
 * DTO для Item при обновлении
 * поскольку в отличие от обновления полей
 * создание нового item требует всех данных
 * необходимо создать разные DTO для item
 */
@Builder
@Data
public class ItemDtoToUpdate {
    private String name;
    private String description;
    private Boolean available;
    private Request request;
}
