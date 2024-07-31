package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

/**
 * @author Mr.White
 * Интерфейс для сервисной логики Item
 */
public interface ItemService {
    ItemResponse createItem(ItemCreate itemDto);

    ItemResponse updateItem(ItemUpdate itemDto);

    ItemResponse getItemById(long id);

    List<ItemResponse> getUserItems(long userId);

    List<ItemResponse> search(String text);

    CommentResponse postComment(long userId, long itemId, CommentCreate comment);

    ItemResponse getOwnerItemById(long itemId, Long userId);
}
