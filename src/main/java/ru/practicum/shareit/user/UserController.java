package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoToCreate;
import ru.practicum.shareit.user.dto.UserDtoToUpdate;

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
    public User createUser(@Valid @RequestBody final UserDtoToCreate userDto) {
        User user = userService.addUser(userDto);
        log.info("User created: {}", userDto);
        return user;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable final Long userId, @RequestBody final UserDtoToUpdate userDto) {
        User user = userService.updateUser(userDto, userId);
        log.info("User updated: {}", userDto);
        return user;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable final Long userId) {
        User user = userService.getUser(userId);
        log.info("User found: {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable final Long userId) {
        userService.deleteUser(userId);
        log.info("User with id {} deleted", userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        List<User> userDtos = userService.getAllUsers();
        log.info("Users found: {}", userDtos);
        return userDtos;
    }
}
