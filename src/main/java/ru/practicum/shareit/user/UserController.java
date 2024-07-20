package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * @author Mr.White
 * Controller for User
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public final class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse addUser(@Valid @RequestBody final UserDtoCreate userDto) {
        log.info("Create User Request Body: {}", userDto);
        UserDtoResponse user = userService.addUser(userDto);
        log.info("User created: {}", userDto);
        return user;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse updateUser(@PathVariable final Long userId, @RequestBody final UserDtoUpdate userDto) {
        log.info("Update User Request Body: {}", userDto);
        UserDtoResponse user = userService.updateUser(userDto, userId);
        log.info("User updated: {}", userDto);
        return user;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse getUser(@PathVariable final Long userId) {
        log.info("Get User Request Body: {}", userId);
        UserDtoResponse user = userService.getUser(userId);
        log.info("User found: {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable final Long userId) {
        log.info("Delete User Request Body: {}", userId);
        userService.deleteUser(userId);
        log.info("User with id {} deleted", userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDtoResponse> getAllUsers() {
        List<UserDtoResponse> userDtos = userService.getAllUsers();
        log.info("Users found: {}", userDtos);
        return userDtos;
    }
}
