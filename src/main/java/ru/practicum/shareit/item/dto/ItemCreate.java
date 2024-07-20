package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
@Builder
public class ItemCreate {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
}