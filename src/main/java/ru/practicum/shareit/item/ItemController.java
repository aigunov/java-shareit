package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDtoToCreate;
import ru.practicum.shareit.item.dto.ItemDtoToUpdate;
import ru.practicum.shareit.item.model.Item;

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
    public Item addItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                        @Valid @RequestBody final ItemDtoToCreate itemDto) {
        Item item = itemService.createItem(itemDto, userId);
        log.info("Item created: {}", itemDto);
        return item;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updateItem(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                           @Valid @RequestBody final ItemDtoToUpdate itemDto, @PathVariable final long itemId) {
        Item item = itemService.updateItem(itemDto, itemId, userId);
        log.info("Item updated: {}", itemDto);
        return item;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item getItem(@PathVariable final long itemId) {
        Item item = itemService.getItemById(itemId);
        log.info("Item: {}", item);
        return item;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getItemsOfUser(@RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        List<Item> items = itemService.getUserItems(userId);
        log.info("User's items: {}", items);
        return items;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> searchItems(@RequestParam final String text) {
        List<Item> foundItems = itemService.search(text);
        log.info("Found items: {}", foundItems);
        return foundItems;
    }

}
