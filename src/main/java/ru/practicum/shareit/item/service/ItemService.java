package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

/**
 * @author Mr.White
 * Интерфейс для сервисной логики Item
 */
public interface ItemService {
    ItemDtoResponse createItem(ItemDtoCreate itemDto, long userId);

    ItemDtoResponse updateItem(ItemDtoUpdate itemDto, long itemId, long userId);

    ItemDtoResponse getItemById(long id);

    List<ItemDtoResponse> getUserItems(long userId);

    List<ItemDtoResponse> search(String text);
}
