package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@AllArgsConstructor
@Data
@Builder
public class ItemCreate {
    private long ownerId;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;

    private long requestId;
}
