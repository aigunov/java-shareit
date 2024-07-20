package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdate;

import java.util.List;

/**
 * @author Mr.White
 * Интерфейс для сервисной логики Item
 */
public interface ItemService {
    ItemResponse createItem(ItemCreate itemDto, long userId);

    ItemResponse updateItem(ItemUpdate itemDto, long itemId, long userId);

    ItemResponse getItemById(long id);

    List<ItemResponse> getUserItems(long userId);

    List<ItemResponse> search(String text);
}
