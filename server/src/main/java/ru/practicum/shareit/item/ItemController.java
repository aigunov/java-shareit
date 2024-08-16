package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * @author Mr.White
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public final class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse addItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                @RequestBody final ItemCreate itemDto) {
        itemDto.setOwnerId(userId);
        log.info("Request body: {}", itemDto);
        ItemResponse item = itemService.createItem(itemDto);
        log.info("Item created: {}", itemDto);
        return item;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse updateItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                   @RequestBody final ItemUpdate itemDto, @PathVariable final long itemId) {
        itemDto.setOwnerId(userId);
        itemDto.setId(itemId);
        log.info("Update Item Request Body: {}", itemDto);
        ItemResponse item = itemService.updateItem(itemDto);
        log.info("Item updated: {}", itemDto);
        return item;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse getItem(@PathVariable final long itemId,
                                @RequestHeader(value = "X-Sharer-User-Id", required = false) final Long userId) {
        log.info("Get Item Request Body: {}", itemId);
        ItemResponse item = userId == null ? itemService.getItemById(itemId) : itemService.getOwnerItemById(itemId, userId);
        log.info("Item: {}", item);
        return item;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponse> getItemsOfUser(@RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        log.info("Get Items Of User Request Body: {}", userId);
        List<ItemResponse> items = itemService.getUserItems(userId);
        log.info("User's items: {}", items);
        return items;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponse> searchItems(@RequestParam final String text) {
        log.info("Search Item Request Text: {}", text);
        List<ItemResponse> foundItems = itemService.search(text);
        log.info("Found items: {}", foundItems);
        return foundItems;
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse postComment(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId,
                                       @PathVariable final long itemId, @RequestBody CommentCreate comment) {
        log.info("User id: {} itemId: {} comment: {}", userId, itemId, comment);
        comment = comment.toBuilder()
                .authorId(userId)
                .itemId(itemId)
                .text(comment.getText())
                .build();
        CommentResponse commentResponse = itemService.postComment(userId, itemId, comment);
        log.info("Comment posted: {}", commentResponse);
        return commentResponse;
    }

}
