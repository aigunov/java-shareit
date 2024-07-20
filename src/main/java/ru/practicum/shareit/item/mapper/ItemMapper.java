package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    /**
     * Item -> ItemDtoToCreate
     */
    public static ItemDtoResponse toItemDto(final Item item) {
        return ItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    /**
     * ItemDtoToCreate -> Item
     */
    public static Item toItem(final ItemDtoCreate itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .build();
    }

}
