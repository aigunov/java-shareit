package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;

/**
 * @author Mr.White
 * Интерфейс для сервсиной логики для user
 */
public interface UserService {
    UserDtoResponse addUser(UserDtoCreate user);

    UserDtoResponse getUser(Long id);

    List<UserDtoResponse> getAllUsers();

    void deleteUser(Long id);

    UserDtoResponse updateUser(UserDtoUpdate user, Long userId);
}
