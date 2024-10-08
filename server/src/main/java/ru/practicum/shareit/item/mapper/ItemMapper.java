package ru.practicum.shareit.item.mapper;

import io.micrometer.common.lang.NonNull;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public class ItemMapper {
    /**
     * Item -> ItemResponse
     */
    @NonNull
    public static ItemResponse toItemDto(final Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    @NonNull
    public static ItemResponse toItemDto(final Item item, final List<CommentResponse> comments, final ItemResponse.BookingInfo nextBooking, final ItemResponse.BookingInfo lastBooking) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .nextBooking(nextBooking)
                .lastBooking(lastBooking)
                .build();
    }

    /**
     * ItemCreate -> Item
     */
    @NonNull
    public static Item toItem(final ItemCreate itemDto, final User owner, final Optional<Request> request) {
        return Item.builder()
                .name(itemDto.getName())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .owner(owner)
                .request(request.orElse(null))
                .build();
    }

    /**
     * Item <- ItemUpdateDto
     */
    @NonNull
    public static Item updateItemFromUpdateDto(final Item item, final ItemUpdate itemDto) {
        item.setName(itemDto.getName() != null && !itemDto.getName().isEmpty() ? itemDto.getName() : item.getName());
        item.setDescription(itemDto.getDescription() != null && !itemDto.getDescription().isEmpty() ?
                itemDto.getDescription() : item.getDescription());
        item.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable());
        return item;
    }

    /**
     * Item -> ItemBookingInfo
     */
    @NonNull
    public static BookingResponse.ItemBookingInfo toItemBookingInfo(final Item item) {
        return BookingResponse.ItemBookingInfo.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }


}
