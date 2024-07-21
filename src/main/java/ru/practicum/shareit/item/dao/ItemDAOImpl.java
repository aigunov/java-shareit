package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author Mr.White
 * Класс реализация интерфейса ItemDAO для работы с временных хранилищем
 */
@Repository
@RequiredArgsConstructor
public class ItemDAOImpl implements ItemDAO {
    private final HashMap<Long, Item> items = new HashMap<>();
    private Long counter = 0L;

    /**
     * Метод сохраняет item
     *
     * @param item  - тело запроса
     * @param owner - владелец переданного item, к которому надо привязать item перед сохранением
     * @return сохраненный item
     */
    @Override
    public Item createItem(Item item, User owner) {
        item.setId(++counter);
        item.setOwner(owner);
        items.put(counter, item);
        return item;
    }

    /**
     * Метод удаляет item из хранилища
     *
     * @param itemId - ID item который надо удалить
     */
    @Override
    public void deleteItem(long itemId) {
        items.remove(itemId);
    }

    /**
     * Метод извлекает по item из хранилища
     *
     * @param id - ID item который надо извлечь
     */
    @Override
    public Optional<Item> getItemById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    /**
     * @return Список всех items
     */
    @Override
    public List<Item> getItems() {
        List<Item> itemsList = new ArrayList<>(items.values());
        return itemsList;
    }

    /**
     * Метод обновляет item в хранилище
     *
     * @param itemDto - тело запроса типа ItemDtoToUpdate содержащий поля для обновления
     * @param itemId  - ID item который нужно обновить
     * @return обновленный item
     */
    @Override
    public Item updateItem(ItemUpdate itemDto, long itemId) {
        Item item = getItemById(itemId).get();
        item.setName(itemDto.getName() != null && !itemDto.getName().isEmpty() ? itemDto.getName() : item.getName());
        item.setDescription(itemDto.getDescription() != null && !itemDto.getDescription().isEmpty() ?
                itemDto.getDescription() : item.getDescription());
        item.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable());
        return item;

    }

}
