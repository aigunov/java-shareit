package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.LocalDateTime;

/**
 * POJO Booking
 */
@Data
public class Booking {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus bookingStatus;

}