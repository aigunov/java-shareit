package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestCreate;
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
class RequestServiceImplTest {
    @Autowired
    private final RequestService requestService;
    @Autowired
    private final ItemService itemService;    private static final LocalDateTime NOW = LocalDateTime.now(), NEXT_DAY = NOW.plusDays(1);
    @Autowired
    private final UserService userService;
    @Autowired
    private final BookingService bookingService;
    private UserCreate userCreate;
    private BookingCreate bookingCreate;
    private ItemCreate itemCreate;
    private RequestCreate requestCreate;

    @BeforeEach
    void setUp() {
        itemCreate = ItemCreate.builder()
                .available(true)
                .description("item description")
                .name("item")
                .build();

        userCreate = UserCreate.builder()
                .email("requester@hotmail.ru")
                .name("requester")
                .build();

        bookingCreate = BookingCreate.builder()
                .start(NOW)
                .end(NEXT_DAY)
                .build();

        requestCreate = RequestCreate.builder()
                .description("please i need this stuff")
                .build();
    }

    @Test
    void createRequest_whenRequestCreate_thenReturnCreatedRequest() {
        var userResponse = userService.addUser(userCreate);
        requestCreate.setRequesterId(userResponse.getId());
        var requestResponse = requestService.createRequest(requestCreate);

        Assertions.assertEquals(requestCreate.getDescription(), requestResponse.getDescription());
    }

    @Test
    void createRequest_whenUserNotExist_thenThrowNoSuchElementException() {
        requestCreate.setRequesterId(1L);

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> requestService.createRequest(requestCreate)
        );
    }

    @Test
    void getUserRequests_whenInvokeMethod_thenReturnRequests() {
        var userResponse = userService.addUser(userCreate);
        requestCreate.setRequesterId(userResponse.getId());
        var requestResponse = requestService.createRequest(requestCreate);
        var anotherRequestResponse = requestService.createRequest(
                requestCreate.toBuilder()
                        .description("bruh")
                        .requesterId(userResponse.getId())
                        .build()
        );

        var request = requestService.getUserRequests(userResponse.getId());

        Assertions.assertTrue(request.size() == 2);
        Assertions.assertEquals(List.of(anotherRequestResponse, requestResponse), request);
    }

    @Test
    void getRequestById_when() {
        var userResponse = userService.addUser(userCreate);
        requestCreate.setRequesterId(userResponse.getId());
        var requestResponse = requestService.createRequest(requestCreate);

        var getRequestResponse = requestService.getRequestById(requestResponse.getId(), userResponse.getId());

        Assertions.assertEquals(requestResponse.getDescription(), getRequestResponse.getDescription());
        Assertions.assertEquals(requestResponse.getId(), getRequestResponse.getId());
    }

    @Test
    void getAllRequests() {
        var userResponse = userService.addUser(userCreate);
        requestCreate.setRequesterId(userResponse.getId());
        var requestResponse = requestService.createRequest(requestCreate);
        var anotherRequestResponse = requestService.createRequest(
                requestCreate.toBuilder()
                        .description("bruh")
                        .requesterId(userResponse.getId())
                        .build()
        );
        var oneMoreRequestResponse = requestService.createRequest(
                requestCreate.toBuilder()
                        .description("asdfasdfa")
                        .requesterId(userResponse.getId())
                        .build()
        );

        var requests = requestService.getAllRequests(PageRequest.of(0, 100));

        Assertions.assertTrue(requests.size() == 3);
        Assertions.assertEquals(List.of(requestResponse, anotherRequestResponse, oneMoreRequestResponse), requests);
    }




}