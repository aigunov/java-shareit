package ru.practicum.shareit.utils;

import ru.practicum.shareit.item.dto.ItemDtoToCreate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDtoToCreate;

/**
 * @author Mr.White
 * Класс со статическими методами для трансформации DTO в MODEL и обратно
 */
public final class Mapper {
    private Mapper() {
    }

    /**
     * Item -> ItemDtoToCreate
     */
    public static ItemDtoToCreate toItemDto(final Item item) {
        return ItemDtoToCreate.builder()
                .name(item.getName())
                .description(item.getDescription())
                .request(item.getRequest())
                .available(item.getAvailable())
                .build();
    }

    /**
     * ItemDtoToCreate -> Item
     */
    public static Item toItem(final ItemDtoToCreate itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .request(itemDto.getRequest())
                .build();
    }

    /**
     * User -> UserDtoToCreate
     */
    public static UserDtoToCreate toUserDto(final User user) {
        return UserDtoToCreate.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * UserDto -> UserDtoToCreate
     */
    public static User toUser(final UserDtoToCreate userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
