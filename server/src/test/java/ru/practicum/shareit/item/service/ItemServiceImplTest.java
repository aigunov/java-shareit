package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ItemNotAvailable;
import ru.practicum.shareit.item.dto.CommentCreate;
import ru.practicum.shareit.item.dto.ItemCreate;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
class ItemServiceImplTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    private ItemCreate create;

    private UserCreate userDto;

    @BeforeEach
    void setUp() {
        create = ItemCreate.builder()
                .available(true)
                .description("item description")
                .name("item")
                .build();

        userDto = UserCreate.builder()
                .name("john sina")
                .email("hisnameisJOHNSINA@hotmail.ru")
                .build();
    }

    @Test
    void createItem_whenUserExistsAndItemDtoValid_thenReturnCreatedItem() {
        UserResponse owner = userService.addUser(userDto);
        create.setOwnerId(owner.getId());
        ItemResponse response = itemService.createItem(create);

        Assertions.assertEquals(create.getName(), response.getName());
        Assertions.assertEquals(create.getDescription(), response.getDescription());
        Assertions.assertEquals(create.getAvailable(), response.getAvailable());
    }

    @Test
    void createItem_whenUserNotExists_thenThrowNoSuchElementException() {
        create.setOwnerId(1L);
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.createItem(create)
        );
    }

    @Test
    void updateItem_whenUserExistsAndItemDtoValid_thenReturnUpdatedItem() {
        UserResponse owner = userService.addUser(userDto);
        create.setOwnerId(owner.getId());
        ItemResponse response = itemService.createItem(create);
        ItemUpdate update = ItemUpdate.builder()
                .id(response.getId())
                .ownerId(owner.getId())
                .name("updated item name")
                .description("updated item description")
                .build();

        response = itemService.updateItem(update);

        Assertions.assertEquals(update.getId(), response.getId());
        Assertions.assertEquals(update.getName(), response.getName());
        Assertions.assertEquals(update.getDescription(), response.getDescription());
    }

    @Test
    void updateItem_whenItemNotExists_thenThrowNoSuchElementException() {
        UserResponse owner = userService.addUser(userDto);
        ItemUpdate update = ItemUpdate.builder()
                .name("flkasjfl")
                .description("jdlskjgl")
                .ownerId(owner.getId())
                .id(1L)
                .build();


        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.updateItem(update)
        );

    }

    @Test
    void getItemById_whenItemExists_thenReturnItem() {
        UserResponse owner = userService.addUser(userDto);
        create.setOwnerId(owner.getId());
        ItemResponse response = itemService.createItem(create);
        long itemId = response.getId();

        response = itemService.getItemById(response.getId());

        Assertions.assertEquals(create.getName(), response.getName());
        Assertions.assertEquals(create.getDescription(), response.getDescription());
        Assertions.assertEquals(itemId, response.getId());
    }

    @Test
    void getItemById_whenItemNotExists_thenThrowNoSuchElementException() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.getItemById(1L)
        );
    }

    @Test
    void getOwnerItemById_whenItemExists_thenReturnItemAndComments() {
        var firstCommentator = UserCreate.builder()
                .name("idiot")
                .email("idiotovich@hotmail.ru")
                .build();
        var owner = userService.addUser(userDto);
        create.setOwnerId(owner.getId());
        var fC = userService.addUser(firstCommentator);
        var item = itemService.createItem(create);
        var comment1 = CommentCreate.builder()
                .text("bruh")
                .authorId(fC.getId())
                .itemId(item.getId())
                .build();
        var firstCommentatorBooking = BookingCreate.builder()
                .bookerId(fC.getId())
                .itemId(item.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
        var booking = bookingService.addBooking(firstCommentatorBooking);
        bookingService.decideRent(owner.getId(), booking.getId(), true);
        var commentResponse = itemService.postComment(fC.getId(), item.getId(), comment1);

        var response = itemService.getOwnerItemById(item.getId(), owner.getId());

        Assertions.assertEquals(item.getName(), response.getName());
        Assertions.assertEquals(item.getDescription(), response.getDescription());
        Assertions.assertEquals(item.getId(), response.getId());
        Assertions.assertEquals(commentResponse, response.getComments().get(0));
    }

    @Test
    void getOwnerItemById_whenItemNotExists_thenThrowNoSuchElementException() {
        var owner = userService.addUser(userDto);
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.getItemById(1L)
        );

    }

    @Test
    void getUserItems_whenInvokeMethod_thenReturnListOfUsersItems() {
        var owner = userService.addUser(userDto);
        create.setOwnerId(owner.getId());
        var response = itemService.createItem(create);
        var itemDto = ItemCreate.builder()
                .name("wow")
                .description("not wow")
                .available(true)
                .ownerId(owner.getId())
                .build();
        var responseDto = itemService.createItem(itemDto);

        var items = itemService.getUserItems(owner.getId());

        Assertions.assertTrue(items.size() == 2);
        Assertions.assertEquals(List.of(response, responseDto), items);
        Assertions.assertEquals(response.getId(), items.get(0).getId());
        Assertions.assertEquals(response.getName(), items.get(0).getName());
        Assertions.assertEquals(response.getDescription(), items.get(0).getDescription());
        Assertions.assertEquals(responseDto.getId(), items.get(1).getId());
        Assertions.assertEquals(responseDto.getName(), items.get(1).getName());
        Assertions.assertEquals(responseDto.getDescription(), items.get(1).getDescription());
    }

    @Test
    void search_whenSearchByTextWOWIgnoreCase_thenReturnOneItem() {
        var owner = userService.addUser(userDto);
        create.setOwnerId(owner.getId());
        var item = itemService.createItem(create);
        var itemDto = ItemCreate.builder()
                .name("wow")
                .description("not wow")
                .available(true)
                .ownerId(owner.getId())
                .build();
        var responseDto = itemService.createItem(itemDto);

        var items = itemService.search("WOW");

        Assertions.assertTrue(items.size() == 1);
        Assertions.assertEquals(responseDto, items.get(0));
    }

    @Test
    void postComment_whenUserBookedItem_thenReturnPostedComment() {
        var firstCommentator = UserCreate.builder()
                .name("idiot")
                .email("idiotovich@hotmail.ru")
                .build();
        var owner = userService.addUser(userDto);
        var fC = userService.addUser(firstCommentator);
        create.setOwnerId(owner.getId());
        var item = itemService.createItem(create);
        var comment1 = CommentCreate.builder()
                .text("bruh")
                .authorId(fC.getId())
                .itemId(item.getId())
                .build();
        var firstCommentatorBooking = BookingCreate.builder()
                .bookerId(fC.getId())
                .itemId(item.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
        var booking = bookingService.addBooking(firstCommentatorBooking);
        bookingService.decideRent(owner.getId(), booking.getId(), true);

        var commentResponse = itemService.postComment(fC.getId(), item.getId(), comment1);

        Assertions.assertEquals(comment1.getText(), commentResponse.getText());
    }

    @Test
    void postComment_whenUserNotBookedItem_thenThrowItemNotAvailable() {
        var firstCommentator = UserCreate.builder()
                .name("idiot")
                .email("idiotovich@hotmail.ru")
                .build();
        var owner = userService.addUser(userDto);
        var fC = userService.addUser(firstCommentator);
        create.setOwnerId(owner.getId());
        var item = itemService.createItem(create);
        var comment1 = CommentCreate.builder()
                .text("bruh")
                .authorId(fC.getId())
                .itemId(item.getId())
                .build();

        Assertions.assertThrows(
                ItemNotAvailable.class,
                () -> itemService.postComment(fC.getId(), item.getId(), comment1)
        );
    }
}