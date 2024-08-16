package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Status;
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
    public BookingResponse addBooking(@RequestBody final BookingCreate booking,
                                      @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        booking.setBookerId(userId);
        BookingResponse bookingResponse = bookingService.addBooking(booking);
        log.info("Booking added: {}", bookingResponse);
        return bookingResponse;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse decideRent(@PathVariable final long bookingId, @RequestParam final boolean approved,
                                      @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        BookingResponse bookingResponse = bookingService.decideRent(userId, bookingId, approved);
        log.info("Owner make decision: {}", bookingResponse);
        return bookingResponse;
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable final long bookingId,
                                      @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        BookingResponse bookingResponse = bookingService.getBooking(userId, bookingId);
        log.info("Booking found: {}", bookingResponse);
        return bookingResponse;
    }

    @GetMapping
    public List<BookingResponse> getBookingsByStatus(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        Status status = Status.from(state).get();
        List<BookingResponse> bookingResponseList = bookingService.findUserBookingByStatus(userId, status);
        log.info("Bookings found: {}", bookingResponseList);
        return bookingResponseList;
    }

    @GetMapping("/owner")
    public List<BookingResponse> getOwnerBookingsByStatus(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                                          @RequestParam(defaultValue = "ALL") final String state) {
        Status status = Status.from(state).get();
        List<BookingResponse> bookingResponseList = bookingService.findOwnerBookingByStatus(userId, status);
        log.info("Bookings found {}", bookingResponseList);
        return bookingResponseList;
    }


}