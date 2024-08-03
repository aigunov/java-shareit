package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ItemNotAvailable;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mr.White
 * Сервисная логика Item
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    /**
     * Сохраняет item при это проверяя, существует ли владелец в базе пользователей
     *
     * @param itemDto - тело запроса
     * @return Item сохраненный в базе данных
     */
    @Transactional
    @Override
    public ItemResponse createItem(final ItemCreate itemDto) {
        User owner = userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException("Такого пользователя не существует"));
        Item item = ItemMapper.toItem(itemDto, owner);
        ItemResponse itemResponse = ItemMapper.toItemDto(itemRepository.save(item));
        log.info("Item created: {}", item);
        return itemResponse;
    }

    /**
     * Обновляет поля Item в базе, выполняет проверка наличия владельца в базе пользователей
     * также проверяет принадлежит item пользователю-владельце чей ID передан в запросе
     *
     * @param itemDto - тело запроса
     * @return item обновленный в базе
     */
    @Transactional
    @Override
    public ItemResponse updateItem(final ItemUpdate itemDto) {
        userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException("Такого пользователя не существует"));

        Item item = itemRepository.findByIdAndOwnerId(itemDto.getId(), itemDto.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException(String.format("User with id = %s " +
                        "don't have item with = %s", itemDto.getOwnerId(), itemDto.getId())));

        ItemMapper.updateItemFromUpdateDto(item, itemDto);
        item = itemRepository.save(item);
        log.info("Item updated: {}", item);
        return ItemMapper.toItemDto(item);
    }

    /**
     * Метод извлекает item из базы
     *
     * @param itemId - ID item'а который нужно извлечь
     * @return извлеченный item
     */
    @Transactional(readOnly = true)
    @Override
    public ItemResponse getItemById(final long itemId) {
        ItemResponse item = ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Такого пользователя не существует")));
        List<CommentResponse> comments = commentRepository.getCommentsByItemId(itemId).stream()
                .map(CommentMapper::toCommentResponse).toList();
        item.setComments(comments);
        log.info("Item received: {}", item);
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponse getOwnerItemById(long itemId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        ItemResponse.BookingInfo nextBooking = BookingMapper
                .toBookingInfo(bookingRepository
                        .findTop1BookingByItem_IdAndStartAfterAndBookingStatusOrderByEndAsc(userId, now, Status.APPROVED));
        ItemResponse.BookingInfo previousBooking = BookingMapper
                .toBookingInfo(bookingRepository
                        .findTop1BookingByItem_IdAndStartBeforeAndBookingStatusOrderByEndDesc(userId, now, Status.APPROVED));
        List<CommentResponse> comments = commentRepository.getCommentsByItemId(itemId).stream()
                .map(CommentMapper::toCommentResponse).toList();
        ItemResponse item = ItemMapper.toItemDto(itemRepository.findById(itemId)
                        .orElseThrow(() -> new NoSuchElementException("Такого пользователя не существует")),
                comments, nextBooking, previousBooking);
        log.info("Item owner received: {}", item);
        return item;
    }

    /**
     * Метод возвращает список всех item'ом принадлежащих пользователю
     *
     * @param userId - ID владельца-пользователя
     * @return список item пользователя
     */
    @Transactional(readOnly = true)
    @Override
    public List<ItemResponse> getUserItems(final long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<ItemResponse> items = itemRepository.findAllByOwnerIdOrderByIdAsc(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
        for (ItemResponse item : items) {
            item.setLastBooking(
                    BookingMapper.toBookingInfo(
                            bookingRepository.findTop1BookingByItem_IdAndStartBeforeAndBookingStatusOrderByEndDesc(userId, now, Status.APPROVED)
                    )
            );
            item.setNextBooking(
                    BookingMapper.toBookingInfo(
                            bookingRepository.findTop1BookingByItem_IdAndStartAfterAndBookingStatusOrderByEndAsc(userId, now, Status.APPROVED)
                    )
            );

            item.setComments(commentRepository.getCommentsByItemId(item.getId()).stream()
                    .map(CommentMapper::toCommentResponse).toList());

        }
        log.info("Items of User: {}", items);
        return items;
    }


    /**
     * Метод для поиска items в названии или описании которых встречается передайнный текст
     *
     * @param text - request parameter с текстом по которому нужно производить поиск
     * @return список всех подходящих item
     */
    @Transactional(readOnly = true)
    @Override
    public List<ItemResponse> search(final String text) {
        List<ItemResponse> items = itemRepository
                .search(("%" + text + "%").toLowerCase())
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
        log.info("Found {} items: ", items.size(), items);
        return !text.isEmpty() ? items : List.of();
    }

    /**
     * @param userId
     * @param itemId
     * @param commentCreate
     * @return
     */
    @Transactional
    @Override
    public CommentResponse postComment(long userId, long itemId, CommentCreate commentCreate) {
        User user = userRepository.findById(commentCreate.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException("User author doesn't exist"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item item doesn't exist"));
        bookingRepository.searchForBookerIdAndItemId(userId, itemId, LocalDateTime.now(), String.valueOf(Status.APPROVED))
                .orElseThrow(() -> new ItemNotAvailable(String.format("User {%s} never made reservation on Item {%s}", user, item)));
        Comment comment = commentRepository.save(CommentMapper.toComment(commentCreate, user, item));
        log.info("Comment: {}", comment);
        return CommentMapper.toCommentResponse(comment);
    }

}
