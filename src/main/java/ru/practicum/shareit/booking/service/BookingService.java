package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Condition;

import java.util.List;

public interface BookingService {
    BookingResponse addBooking(BookingCreate booking);

    BookingResponse decideRent(long userId, long bookingId, boolean approved);

    BookingResponse getBooking(long userId, long bookingId);

    List<BookingResponse> findUserBookingByCondition(long userId, Condition condition);

    List<BookingResponse> findOwnerBookingByCondition(long userId, Condition condition);
}
