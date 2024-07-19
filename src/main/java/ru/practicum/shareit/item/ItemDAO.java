package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDtoToUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Mr.White
 * Интерфейс взаимодействия с хранилищами(временным, файловым, базой данных)
 */
public interface ItemDAO {
    Item createItem(Item item, User owner);

    void deleteItem(long itemId);

    Optional<Item> getItemById(long itemId);

    List<Item> getItems();

    Item updateItem(ItemDtoToUpdate itemDto, long itemId);
}
