package ru.practicum.shareit.item.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.apache.coyote.Request;
import ru.practicum.shareit.user.User;

/**
 * @author Mr.White
 * POJO класс Item
 */
@Data
@Builder
public class Item {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    @Valid
    private User owner;
    private Request request;
}
