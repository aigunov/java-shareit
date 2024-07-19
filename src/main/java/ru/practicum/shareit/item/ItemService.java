package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDtoToCreate;
import ru.practicum.shareit.item.dto.ItemDtoToUpdate;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author Mr.White
 * Интерфейс для сервисной логики Item
 */
public interface ItemService {
    Item createItem(ItemDtoToCreate itemDto, long userId);

    Item updateItem(ItemDtoToUpdate itemDto, long itemId, long userId);

    Item getItemById(long id);

    List<Item> getUserItems(long userId);

    List<Item> search(String text);
}
