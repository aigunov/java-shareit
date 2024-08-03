package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Condition;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * Booking controller
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public final class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponse addBooking(@RequestBody @Valid final BookingCreate booking,
                                      @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        log.info("User {} add booking {}", userId, booking);
        booking.setBookerId(userId);
        BookingResponse bookingResponse = bookingService.addBooking(booking);
        log.info("Booking added: {}", bookingResponse);
        return bookingResponse;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse decideRent(@PathVariable final long bookingId, @RequestParam final boolean approved,
                                      @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        log.info("User {} decide rent booking {} result - {}", userId, bookingId, approved);
        BookingResponse bookingResponse = bookingService.decideRent(userId, bookingId, approved);
        log.info("Owner make decision: {}", bookingResponse);
        return bookingResponse;
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable final long bookingId,
                                      @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        log.info("User {} get booking {}", userId, bookingId);
        BookingResponse bookingResponse = bookingService.getBooking(userId, bookingId);
        log.info("Booking found: {}", bookingResponse);
        return bookingResponse;
    }

    @GetMapping
    public List<BookingResponse> getBookingsByCondition(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                                        @RequestParam(defaultValue = "ALL") final String state) {
        log.info("User {} requests a list of his rentals by {}", userId, state);
        Condition condition = Condition.valueOf(state.toUpperCase());
        List<BookingResponse> bookingResponseList = bookingService.findUserBookingByCondition(userId, condition);
        log.info("Bookings found: {}", bookingResponseList);
        return bookingResponseList;
    }

    @GetMapping("/owner")
    public List<BookingResponse> getOwnerBookingsByCondition(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                                             @RequestParam(defaultValue = "ALL") final String state) {
        Condition condition = Condition.valueOf(state.toUpperCase());
        List<BookingResponse> bookingResponseList = bookingService.findOwnerBookingByCondition(userId, condition);
        log.info("Bookings found {}", bookingResponseList);
        return bookingResponseList;
    }


}
