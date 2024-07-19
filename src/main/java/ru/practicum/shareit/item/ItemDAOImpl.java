package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDtoToUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Mr.White
 * Класс реализация интерфейса ItemDAO для работы с временных хранилищем
 */
@Repository
@RequiredArgsConstructor
public class ItemDAOImpl implements ItemDAO {
    private final List<Item> items;
    private Long counter = 1L;

    /**
     * Метод сохраняет item
     *
     * @param item  - тело запроса
     * @param owner - владелец переданного item, к которому надо привязать item перед сохранением
     * @return сохраненный item
     */
    @Override
    public Item createItem(Item item, User owner) {
        item.setId(counter);
        item.setOwner(owner);
        counter++;
        items.add(item);
        return item;
    }

    /**
     * Метод удаляет item из хранилища
     *
     * @param itemId - ID item который надо удалить
     */
    @Override
    public void deleteItem(long itemId) {
        Item itemToDelete = getItemById(itemId).get();
        items.remove(itemToDelete);
    }

    /**
     * Метод извлекает по item из хранилища
     *
     * @param id - ID item который надо извлечь
     */
    @Override
    public Optional<Item> getItemById(long id) {
        return items.stream().filter(item -> item.getId() == id).findFirst();
    }

    /**
     * @return Список всех items
     */
    @Override
    public List<Item> getItems() {
        return items;
    }

    /**
     * Метод обновляет item в хранилище
     *
     * @param itemDto - тело запроса типа ItemDtoToUpdate содержащий поля для обновления
     * @param itemId  - ID item который нужно обновить
     * @return обновленный item
     */
    @Override
    public Item updateItem(ItemDtoToUpdate itemDto, long itemId) {
        Item item = getItemById(itemId).get();
        items.remove(item);
        item.setName(itemDto.getName() != null && !itemDto.getName().isEmpty() ? itemDto.getName() : item.getName());
        item.setDescription(itemDto.getDescription() != null && !itemDto.getDescription().isEmpty() ?
                itemDto.getDescription() : item.getDescription());
        item.setRequest(itemDto.getRequest());
        item.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable());
        items.add(item);
        return item;

    }

}
