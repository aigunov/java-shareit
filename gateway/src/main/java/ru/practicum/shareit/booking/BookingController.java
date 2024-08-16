package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookingCreate requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByCondition(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                                         @RequestParam(defaultValue = "ALL") final String state) {
        log.info("User {} requests a list of his rentals by {}", userId, state);
        BookingState condition = BookingState.from(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.findUserBookingByCondition(userId, condition);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookingsByCondition(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                                              @RequestParam(defaultValue = "ALL") final String state) {
        BookingState condition = BookingState.from(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get owner(id={}) bookings by {}", userId, condition);
        return bookingClient.findOwnerBookingByCondition(userId, condition);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> decideRent(@PathVariable final long bookingId,
                                             @RequestParam final boolean approved,
                                             @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
        log.info("User {} decide rent booking {} result - {}", userId, bookingId, approved);
        return bookingClient.decideRent(userId, bookingId, approved);
    }

//	@PostMapping
//	public BookingResponse addBooking(@RequestBody @Valid final BookingCreate booking,
//									  @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
//		log.info("User {} add booking {}", userId, booking);
//		booking.setBookerId(userId);
//		BookingResponse bookingResponse = bookingService.addBooking(booking);
//		log.info("Booking added: {}", bookingResponse);
//		return bookingResponse;
//	}
//	@GetMapping("/{bookingId}")
//	public BookingResponse getBooking(@PathVariable final long bookingId,
//									  @RequestHeader(value = "X-Sharer-User-Id") final long userId) {
//		log.info("User {} get booking {}", userId, bookingId);
//		BookingResponse bookingResponse = bookingService.getBooking(userId, bookingId);
//		log.info("Booking found: {}", bookingResponse);
//		return bookingResponse;
//	}
//	@GetMapping
//	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
//			@RequestParam(name = "state", defaultValue = "all") String stateParam) {
//		BookingState state = BookingState.from(stateParam)
//				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
//		log.info("Get booking with state {}, userId={}", stateParam, userId);
//		return bookingClient.getBookings(userId, state);
//	}
}
