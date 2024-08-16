package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreate;
import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.dto.ItemUpdate;


/**
 * @author Mr.White
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public final class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                          @Valid @RequestBody final ItemCreate itemDto) {
        log.info("Request body: {}", itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                             @Valid @RequestBody final ItemUpdate itemDto, @PathVariable final long itemId) {
        log.info("Update Item Request Body: {}", itemDto);
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@PathVariable final long itemId,
                                          @RequestHeader(value = "X-Sharer-User-Id", required = false) final Long userId) {
        log.info("Get Item Request Body: {}", itemId);
        if (userId == null) {
            return itemClient.getItemById(itemId);
        }
        return itemClient.getOwnerItemById(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemsOfUser(@RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        log.info("Get Items Of User Request Body: {}", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItems(@RequestParam final String text) {
        log.info("Search Item Request Text: {}", text);
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> postComment(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId,
                                              @PathVariable final long itemId, @RequestBody CommentCreate comment) {
        log.info("User id: {} itemId: {} comment: {}", userId, itemId, comment);
        comment = comment.toBuilder()
                .authorId(userId)
                .itemId(itemId)
                .text(comment.getText())
                .build();
        return itemClient.postComment(userId, itemId, comment);
    }

}
