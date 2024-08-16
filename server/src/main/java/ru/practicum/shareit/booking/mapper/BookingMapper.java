package ru.practicum.shareit.booking.mapper;

import io.micrometer.common.lang.NonNull;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public final class BookingMapper {

    /**
     * BookingCreate --> Booking
     */
    @NonNull
    public static Booking toBooking(BookingCreate bookingCreate, User booker, Item item) {
        return Booking.builder()
                .bookingStatus(Status.WAITING)
                .booker(booker)
                .item(item)
                .start(bookingCreate.getStart())
                .end(bookingCreate.getEnd())
                .build();
    }

    /**
     * Booking --> BookingResponse
     */
    @NonNull
    public static BookingResponse toBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getBookingStatus())
                .item(ItemMapper.toItemBookingInfo(booking.getItem()))
                .booker(booking.getBooker())
                .build();
    }

    /**
     * Booking --> ItemResponse.BookingInfo
     */
    public static ItemResponse.BookingInfo toBookingInfo(Optional<Booking> booking) {
        return booking.map(value -> ItemResponse.BookingInfo.builder()
                .bookerId(value.getBooker().getId())
                .id(value.getId())
                .build()).orElse(null);
    }
}
