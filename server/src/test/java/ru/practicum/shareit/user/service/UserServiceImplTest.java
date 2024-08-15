package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
class UserServiceImplTest {
    @Autowired
    private final UserService service;

    private UserCreate create;

    @BeforeEach
    void setUp() {
        create = UserCreate.builder()
                .name("john smith")
                .email("jhonsmith@hotmail.ru")
                .build();

    }

    @Test
    void addUser_whenUserValid_thenReturnCreatedUser() {
        UserResponse response = service.addUser(create);

        Assertions.assertEquals(response.getName(), create.getName());
        Assertions.assertEquals(response.getEmail(), create.getEmail());
    }

    @Test
    void addUser_whenUserExists_thenReturnDataIntegrityViolationException() {
        service.addUser(create);

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> service.addUser(new UserCreate("not jhon", "jhonsmith@hotmail.ru"))
        );
    }

    @Test
    void getUser_whenUserExists_thenReturnUser() {
        UserResponse response = service.addUser(create);

        UserResponse userDto = service.getUser(response.getId());

        Assertions.assertEquals(userDto.getName(), create.getName());
        Assertions.assertEquals(userDto.getEmail(), create.getEmail());
        Assertions.assertEquals(userDto.getId(), response.getId());
    }

    @Test
    void getUser_whenUserDoesntExist_thenThrowNoSuchElementException() {

        Exception ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> service.getUser(99L)
        );

        Assertions.assertEquals("Данный пользователь не найден", ex.getMessage());
    }

    @Rollback(value = true)
    @Test
    void getAllUsers_whenUsersInDB_returnAllUsers() {
        UserResponse dtoFirst = service.addUser(new UserCreate("not john", "smith@hotmail.ru"));
        UserResponse dtoSecond = service.addUser(new UserCreate("john sina", "hisnameisJOOHNSINAA@hotmail.ru"));

        List<UserResponse> users = service.getAllUsers();

        Assertions.assertEquals(List.of(dtoFirst, dtoSecond), users);
        Assertions.assertEquals(dtoFirst, users.get(0));
        Assertions.assertEquals(dtoSecond, users.get(1));
    }

    @Test
    void deleteUser_whenUserExists_thenReturnVoidAndDeleteUser() {
        UserResponse response = service.addUser(create);

        service.deleteUser(response.getId());

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> service.getUser(response.getId())
        );
    }

    @Test
    void deleteUser_whenUserDoesntExists_thenThrowNothing() {
        Assertions.assertDoesNotThrow(
                () -> service.deleteUser(500L)
        );
    }

    @Test
    void updateUser_whenUserExistAndUserUpdateDtoValid_thenReturnUpdatedUser() {
        UserResponse response = service.addUser(create);

        UserUpdate update = UserUpdate.builder()
                .id(response.getId())
                .name("joker batmanovich")
                .email(response.getEmail())
                .build();

        response = service.updateUser(update);

        Assertions.assertEquals(update.getEmail(), response.getEmail());
        Assertions.assertEquals(update.getName(), response.getName());
        Assertions.assertEquals(update.getId(), response.getId());

    }

    @Test
    void updateUser_whenUserNotExists_thenThrowNoSuchElementException() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> service.updateUser(new UserUpdate(404L, "tyler derden", "projectchaos@hotmail.ru"))
        );
    }

    @Test
    void updateUser_whenUserUpdateDtoEmailAlreadyExists_thenThrow() {
        UserResponse response = service.addUser(create);
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> service.updateUser(new UserUpdate(425L, response.getName(), response.getEmail()))
        );

    }
}