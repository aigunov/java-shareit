package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDtoToCreate;
import ru.practicum.shareit.user.dto.UserDtoToUpdate;

import java.util.List;

/**
 * @author Mr.White
 * Интерфейс для сервсиной логики для user
 */
public interface UserService {
    User addUser(UserDtoToCreate user);

    User getUser(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);

    User updateUser(UserDtoToUpdate user, Long userId);
}
