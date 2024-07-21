package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdate;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse addItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                @Valid @RequestBody final ItemCreate itemDto) {
        log.info("Request body: {}", itemDto);
        ItemResponse item = itemService.createItem(itemDto, userId);
        log.info("Item created: {}", itemDto);
        return item;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse updateItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                   @Valid @RequestBody final ItemUpdate itemDto, @PathVariable final long itemId) {
        log.info("Update Item Request Body: {}", itemDto);
        ItemResponse item = itemService.updateItem(itemDto, itemId, userId);
        log.info("Item updated: {}", itemDto);
        return item;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse getItem(@PathVariable final long itemId) {
        log.info("Get Item Request Body: {}", itemId);
        ItemResponse item = itemService.getItemById(itemId);
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

}
