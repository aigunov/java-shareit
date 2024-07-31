package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemBookingInfo;
import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdate;
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

    /**
     * Item <- ItemUpdateDto
     */
    public static Item updateItemFromUpdateDto(final Item item, final ItemUpdate itemDto) {
        item.setName(itemDto.getName() != null && !itemDto.getName().isEmpty() ? itemDto.getName() : item.getName());
        item.setDescription(itemDto.getDescription() != null && !itemDto.getDescription().isEmpty() ?
                itemDto.getDescription() : item.getDescription());
        item.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable());
        return item;
    }

    /**
     * Item -> ItemBookingInfo
     */
    public static ItemBookingInfo toItemBookingInfo(final Item item) {
        return ItemBookingInfo.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }


}
