package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Mr.White
 * Интерфейс для работы с любиым user хранилищем(временным, файловым, базой данных)
 */
public interface UserDAO {
    List<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(long id);

    Optional<User> getUser(long id);

    User updateUser(User user);
}
