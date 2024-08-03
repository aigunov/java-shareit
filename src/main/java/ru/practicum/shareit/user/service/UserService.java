package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;

import java.util.List;

/**
 * @author Mr.White
 * Интерфейс для сервсиной логики для user
 */
public interface UserService {
    UserResponse addUser(UserCreate user);

    UserResponse getUser(Long id);

    List<UserResponse> getAllUsers();

    void deleteUser(Long id);

    UserResponse updateUser(UserUpdate user);
}
