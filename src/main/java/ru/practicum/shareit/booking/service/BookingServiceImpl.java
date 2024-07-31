package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Condition;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ChangeBookingStatusTwice;
import ru.practicum.shareit.exception.ItemNotAvailable;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingResponse addBooking(BookingCreate bookingDto) {
        User booker = userRepository.findById(bookingDto.getBookerId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + bookingDto.getBookerId() + " not found"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item with id " + booker.getId() + " not found"));
        if (!item.getAvailable()) {
            throw new ItemNotAvailable("Item " + item + " is already booked");
        }
        if (item.getOwner().equals(booker)) {
            throw new NoSuchElementException("Owner of item " + item + " cannot booking this");
        }
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto, booker, item));// <------
        log.info("Booking created: {}", booking);
        return BookingMapper.toBookingResponse(booking);
    }

    @Transactional
    @Override
    public BookingResponse decideRent(long userId, long bookingId, boolean approved) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "User with id " + userId + " not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking with id " + bookingId + " not found"));
        Item item = booking.getItem();
        if (!owner.equals(item.getOwner()))
            throw new NoSuchElementException("User: " + owner + " is not owner of booking " + booking);
        Status status = approved ? Status.APPROVED : Status.REJECTED;
        if (booking.getBookingStatus().equals(status))
            throw new ChangeBookingStatusTwice("You cannot change state double");

        if (approved) {
            booking.setBookingStatus(Status.APPROVED);
        } else {
            booking.setBookingStatus(Status.REJECTED);
        }

        bookingRepository.save(booking);
        return BookingMapper.toBookingResponse(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingResponse getBooking(long userId, long bookingId) {
        User watcher = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking with id " + bookingId + " not found"));
        User booker = booking.getBooker();
        User owner = booking.getItem().getOwner();
        if (booker.equals(watcher) || owner.equals(watcher)) return BookingMapper.toBookingResponse(booking);
        else throw new NoSuchElementException("User: " + owner + " is not owner and not booker of booking " + booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingResponse> findUserBookingByCondition(long userId, Condition state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        List<BookingResponse> bookingResponseList = switch (state) {
            case ALL -> bookingRepository.findAllBookingByBookerId(userId)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case WAITING -> bookingRepository
                    .findAllByBookerIdAndBookingStatusOrderByStartDesc(userId, Status.WAITING)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case REJECTED -> bookingRepository
                    .findAllByBookerIdAndBookingStatusOrderByStartDesc(userId, Status.REJECTED)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case APPROVED -> bookingRepository
                    .findAllByBookerIdAndBookingStatusOrderByStartDesc(userId, Status.APPROVED)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case CURRENT -> bookingRepository
                    .findAllByBookerIdAndStartIsLessThanAndEndIsGreaterThanOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now())
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case FUTURE -> bookingRepository
                    .findAllByBookerIdAndStartIsGreaterThanOrderByStartDesc(userId, LocalDateTime.now())
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case PAST -> bookingRepository
                    .findAllByBookerIdAndEndIsLessThanOrderByStartDesc(userId, LocalDateTime.now())
                    .stream().map(BookingMapper::toBookingResponse).toList();
        };
        log.info("Find user booking by condition \"{}\" is {}", state, bookingResponseList);
        return bookingResponseList;
    }

    @Override
    public List<BookingResponse> findOwnerBookingByCondition(long userId, Condition condition) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        List<BookingResponse> bookingResponseList = switch (condition) {
            case ALL -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case APPROVED -> bookingRepository
                    .findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(userId, Status.APPROVED)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case WAITING -> bookingRepository
                    .findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(userId, Status.WAITING)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case REJECTED -> bookingRepository
                    .findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(userId, Status.REJECTED)
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case CURRENT -> bookingRepository
                    .findAllByItemOwnerIdAndStartIsLessThanAndEndIsGreaterThanOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now())
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case FUTURE -> bookingRepository
                    .findAllByItemOwnerIdAndStartIsGreaterThanOrderByStartDesc(userId, LocalDateTime.now())
                    .stream().map(BookingMapper::toBookingResponse).toList();
            case PAST -> bookingRepository
                    .findAllByItemOwnerIdAndEndIsLessThanOrderByStartDesc(userId, LocalDateTime.now())
                    .stream().map(BookingMapper::toBookingResponse).toList();
        };
        log.info("Find owner booking by condition \"{}\" is {}", condition, bookingResponseList);
        return bookingResponseList;
    }
}
