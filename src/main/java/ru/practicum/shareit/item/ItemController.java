package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
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
    public ItemDtoResponse addItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                   @Valid @RequestBody final ItemDtoCreate itemDto) {
        log.info("Request body: {}", itemDto);
        ItemDtoResponse item = itemService.createItem(itemDto, userId);
        log.info("Item created: {}", itemDto);
        return item;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoResponse updateItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                      @Valid @RequestBody final ItemDtoUpdate itemDto, @PathVariable final long itemId) {
        log.info("Update Item Request Body: {}", itemDto);
        ItemDtoResponse item = itemService.updateItem(itemDto, itemId, userId);
        log.info("Item updated: {}", itemDto);
        return item;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoResponse getItem(@PathVariable final long itemId) {
        log.info("Get Item Request Body: {}", itemId);
        ItemDtoResponse item = itemService.getItemById(itemId);
        log.info("Item: {}", item);
        return item;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoResponse> getItemsOfUser(@RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        log.info("Get Items Of User Request Body: {}", userId);
        List<ItemDtoResponse> items = itemService.getUserItems(userId);
        log.info("User's items: {}", items);
        return items;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoResponse> searchItems(@RequestParam final String text) {
        log.info("Search Item Request Text: {}", text);
        List<ItemDtoResponse> foundItems = itemService.search(text);
        log.info("Found items: {}", foundItems);
        return foundItems;
    }

}
