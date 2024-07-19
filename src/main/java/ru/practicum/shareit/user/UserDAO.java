package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDtoToUpdate;

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

    User updateUser(UserDtoToUpdate userDto, long userId);
}
