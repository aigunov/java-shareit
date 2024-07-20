package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * @author Mr.White
 * POJO класс Item
 */
@Data
@Builder
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
}
