package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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

    Item updateItem(ItemUpdate itemDto, long itemId);
}
