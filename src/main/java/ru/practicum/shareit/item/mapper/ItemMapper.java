package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    /**
     * Item -> ItemResponse
     */
    public static ItemResponse toItemDto(final Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    /**
     * ItemCreate -> Item
     */
    public static Item toItem(final ItemCreate itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .build();
    }

}
