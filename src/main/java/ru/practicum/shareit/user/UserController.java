package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;
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
    @ResponseStatus(HttpStatus.OK)
    public UserResponse addUser(@Valid @RequestBody final UserCreate userDto) {
        log.info("Create User Request Body: {}", userDto);
        UserResponse user = userService.addUser(userDto);
        log.info("User created: {}", userDto);
        return user;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@PathVariable final Long userId, @RequestBody final UserUpdate userDto) {
        userDto.setId(userId);
        log.info("Update User Request Body: {}", userDto);
        UserResponse user = userService.updateUser(userDto);
        log.info("User updated: {}", userDto);
        return user;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable final Long userId) {
        log.info("Get User Request Body: {}", userId);
        UserResponse user = userService.getUser(userId);
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
    public List<UserResponse> getAllUsers() {
        List<UserResponse> userDtos = userService.getAllUsers();
        log.info("Users found: {}", userDtos);
        return userDtos;
    }
}
