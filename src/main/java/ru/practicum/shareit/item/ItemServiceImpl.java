package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadUserIdToUpdate;
import ru.practicum.shareit.exception.NoSuchItemException;
import ru.practicum.shareit.exception.NoSuchUserException;
import ru.practicum.shareit.item.dto.ItemDtoToCreate;
import ru.practicum.shareit.item.dto.ItemDtoToUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDAO;
import ru.practicum.shareit.utils.Mapper;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Mr.White
 * Сервисная логика Item
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class ItemServiceImpl implements ItemService {
    private final ItemDAO itemDAO;
    private final UserDAO userDAO;

    /**
     * Сохраняет item при это проверяя, существует ли владелец в базе пользователей
     *
     * @param itemDto - тело запроса
     * @param userId  - заголовок HTTP запроса, ID владельца
     * @return Item сохраненный в базе данных
     */
    @Override
    public Item createItem(final ItemDtoToCreate itemDto, final long userId) {
        User owner = userDAO.getUser(userId).orElseThrow(() -> new NoSuchUserException("Такого пользователя не существует"));
        Item item = Mapper.toItem(itemDto);
        itemDAO.createItem(item, owner);
        log.info("Item created: {}", item);
        return item;
    }

    /**
     * Обновляет поля Item в базе, выполняет проверка наличия владельца в базе пользователей
     * также проверяет принадлежит item пользователю-владельце чей ID передан в запросе
     *
     * @param itemDto - тело запроса
     * @param itemId  - ID item'а чьи поля требуется обновить
     * @param userId  - ID предполагаемого владельца пользователя
     * @return item обновленный в базе
     */
    @Override
    public Item updateItem(final ItemDtoToUpdate itemDto, final long itemId, final long userId) {
        userDAO.getUser(userId).orElseThrow(() -> new NoSuchUserException("Такого пользователя не существует"));
        Item item = itemDAO.getItemById(itemId)
                .orElseThrow(() -> new NoSuchItemException("Такого предмета не существует"));
        if (item.getOwner().getId() != userId) throw new BadUserIdToUpdate("Предмет не принадлежит этому пользователю");
        item = itemDAO.updateItem(itemDto, itemId);
        log.info("Item updated: {}", item);
        return item;
    }

    /**
     * Метод извлекает item из базы
     *
     * @param itemId - ID item'а который нужно извлечь
     * @return извлеченный item
     */
    @Override
    public Item getItemById(final long itemId) {
        Item item = itemDAO.getItemById(itemId)
                .orElseThrow(() -> new NoSuchItemException("Такого пользователя не существует"));
        log.info("Item received: {}", item);
        return item;
    }

    /**
     * Метод возвращает список всех item'ом принадлежащих пользователю
     *
     * @param userId - ID владельца-пользователя
     * @return список item пользователя
     */
    @Override
    public List<Item> getUserItems(final long userId) {
        List<Item> items = itemDAO.getItems().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .sorted(Comparator.comparing(Item::getName)).toList();
        log.info("Items of User: {}", items);
        return items;
    }

    /**
     * Метод для поиска items в названии или описании которых встречается передайнный текст
     *
     * @param text - request parameter с текстом по которому нужно производить поиск
     * @return список всех подходящих item
     */
    @Override
    public List<Item> search(final String text) {
        String regex = ".*" + Pattern.quote(text.toLowerCase()) + ".*";
        List<Item> items = itemDAO.getItems().stream()
                .filter(item -> (Pattern.compile(regex).matcher(item.getName().toLowerCase()).matches()
                        || Pattern.compile(regex).matcher(item.getDescription().toLowerCase()).matches())
                        && item.getAvailable())
                .collect(Collectors.toList());
        log.info("Found {} items: ", items.size(), items);
        return !text.isEmpty() ? items : List.of();
    }

}
