package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ItemNotAvailable;
import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
class BookingServiceImplTest {
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final UserService userService;    private static final LocalDateTime NOW = LocalDateTime.now(), NEXT_DAY = NOW.plusDays(1);
    @Autowired
    private final ItemService itemService;
    private BookingCreate bookingCreate;
    private ItemCreate itemCreate;
    private UserCreate ownerCreate, bookerCreate;

    @BeforeEach
    void setUp() {
        itemCreate = ItemCreate.builder()
                .available(true)
                .description("item description")
                .name("item")
                .build();

        bookerCreate = UserCreate.builder()
                .name("Mr.White")
                .email("mrwhtie@hotmail.ru")
                .build();

        ownerCreate = UserCreate.builder()
                .name("john sina")
                .email("hisnameisJOHNSINA@hotmail.ru")
                .build();

        bookingCreate = BookingCreate.builder()
                .start(NOW)
                .end(NEXT_DAY)
                .build();
    }

    @Test
    void addBooking_whenAllDataValid_thenReturnBooking() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());

        var bookingResponse = bookingService.addBooking(bookingCreate);

        Assertions.assertEquals(bookingCreate.getStart(), bookingResponse.getStart());
        Assertions.assertEquals(bookingCreate.getEnd(), bookingResponse.getEnd());
        Assertions.assertEquals(bookingCreate.getBookerId(), bookingResponse.getBooker().getId());
        Assertions.assertEquals(bookingCreate.getItemId(), bookingResponse.getItem().id());
    }

    @Test
    void addBooking_whenBookerIsOwner_thenThrowNoSuchElementException() {
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(ownerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingService.addBooking(bookingCreate)
        );
    }

    @Test
    void addBooking_whenItemNotAvailable_thenThrowItemNotAvailable() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        itemCreate.setAvailable(false);
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());

        Assertions.assertThrows(
                ItemNotAvailable.class,
                () -> bookingService.addBooking(bookingCreate)
        );
    }

    @Test
    void decideRent_whenBookingAvailableApprovedBooking_thenReturnDecision() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());
        var bookingResponse = bookingService.addBooking(bookingCreate);

        var decisionBooking = bookingService.decideRent(ownerResponse.getId(), bookingResponse.getId(), true);

        Assertions.assertEquals(bookingResponse.getId(), decisionBooking.getId());
        Assertions.assertEquals(bookingResponse.getItem(), decisionBooking.getItem());
        Assertions.assertEquals(Status.APPROVED, decisionBooking.getStatus());
    }

    @Test
    void decideRent_whenBookingAvailableRejectBooking_thenReturnDecision() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());
        var bookingResponse = bookingService.addBooking(bookingCreate);

        var decisionBooking = bookingService.decideRent(ownerResponse.getId(), bookingResponse.getId(), false);

        Assertions.assertEquals(bookingResponse.getId(), decisionBooking.getId());
        Assertions.assertEquals(bookingResponse.getItem(), decisionBooking.getItem());
        Assertions.assertEquals(Status.REJECTED, decisionBooking.getStatus());
    }

    @Test
    void decideRent_whenBookingNotExists_thenThrowNoSuchElementException() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingService.decideRent(ownerResponse.getId(), 1L, true)
        );
    }

    @Test
    void getBooking_whenOwnerInvokeMethod_thenReturnBooking() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());
        var bookingResponse = bookingService.addBooking(bookingCreate);

        var getBookingResponse = bookingService.getBooking(ownerResponse.getId(), bookingResponse.getId());

        Assertions.assertEquals(bookingResponse.getStart(), getBookingResponse.getStart());
        Assertions.assertEquals(bookingResponse.getEnd(), getBookingResponse.getEnd());
        Assertions.assertEquals(bookingResponse.getBooker().getId(), getBookingResponse.getBooker().getId());
        Assertions.assertEquals(bookingResponse.getItem().id(), getBookingResponse.getItem().id());
    }

    @Test
    void getBooking_whenBookerInvokeMethod_thenReturnBooking() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());
        var bookingResponse = bookingService.addBooking(bookingCreate);

        var getBookingResponse = bookingService.getBooking(bookerResponse.getId(), bookingResponse.getId());

        Assertions.assertEquals(bookingResponse.getStart(), getBookingResponse.getStart());
        Assertions.assertEquals(bookingResponse.getEnd(), getBookingResponse.getEnd());
        Assertions.assertEquals(bookingResponse.getBooker().getId(), getBookingResponse.getBooker().getId());
        Assertions.assertEquals(bookingResponse.getItem().id(), getBookingResponse.getItem().id());
    }

    @Test
    void getBooking_whenBookingNotExists_thenThrowNoSuchElementException() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getBooking(ownerResponse.getId(), itemResponse.getId())
        );
    }

    @Test
    void getBooking_whenMethodInvokeNotOwnerOrBooker_thenThrowNoSuchElementException() {
        var bookerResponse = userService.addUser(bookerCreate);
        var anotherUserResponse = userService.addUser(
                bookerCreate.builder()
                        .email("aaaaaa@bbb.ru")
                        .name("loh")
                        .build()
        );
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());
        var bookingResponse = bookingService.addBooking(bookingCreate);

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getBooking(anotherUserResponse.getId(), bookingResponse.getId())
        );
    }

    @Test
    void findUserBookingByStatus_whenStatusIsALL_thenReturnAllUserBookings() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        var anotherItemResponse = itemService.createItem(
                itemCreate.toBuilder()
                        .ownerId(ownerResponse.getId())
                        .name("bruh item")
                        .description("great bruh")
                        .available(true)
                        .build()
        );
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());
        bookingService.addBooking(bookingCreate);
        bookingService.addBooking(
                bookingCreate.toBuilder()
                        .start(NOW)
                        .end(NEXT_DAY)
                        .itemId(anotherItemResponse.getId())
                        .bookerId(bookerResponse.getId())
                        .build()
        );

        var bookings = bookingService.findUserBookingByStatus(bookerResponse.getId(), Status.ALL);

        Assertions.assertEquals(2, bookings.size());
        Assertions.assertEquals(bookerResponse.getId(), bookings.get(0).getBooker().getId());
        Assertions.assertEquals(bookerResponse.getName(), bookings.get(0).getBooker().getName());
        Assertions.assertEquals(bookerResponse.getEmail(), bookings.get(0).getBooker().getEmail());
    }

    @Test
    void findOwnerBookingByStatus() {
        var bookerResponse = userService.addUser(bookerCreate);
        var ownerResponse = userService.addUser(ownerCreate);
        itemCreate.setOwnerId(ownerResponse.getId());
        var itemResponse = itemService.createItem(itemCreate);
        var anotherItemResponse = itemService.createItem(
                itemCreate.toBuilder()
                        .ownerId(ownerResponse.getId())
                        .name("bruh item")
                        .description("great bruh")
                        .available(true)
                        .build()
        );
        bookingCreate.setBookerId(bookerResponse.getId());
        bookingCreate.setItemId(itemResponse.getId());
        var oneBookingResponse = bookingService.addBooking(bookingCreate);
        var twoBookingResponse = bookingService.addBooking(
                bookingCreate.toBuilder()
                        .start(NOW)
                        .end(NEXT_DAY)
                        .itemId(anotherItemResponse.getId())
                        .bookerId(bookerResponse.getId())
                        .build()
        );

        var bookings = bookingService.findOwnerBookingByStatus(ownerResponse.getId(), Status.ALL);

        Assertions.assertEquals(2, bookings.size());
        Assertions.assertEquals(List.of(oneBookingResponse, twoBookingResponse), bookings);
    }




}