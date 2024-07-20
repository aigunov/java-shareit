package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mr.White
 * Сервис класс для user-контроллера
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    /**
     * Метод добавляет пользователя
     *
     * @param userCreate - тело запроса
     * @return добавленного пользователя
     */
    @Override
    public UserResponse addUser(final UserCreate userCreate) {
        User user = userDAO.saveUser(UserMapper.toUser(userCreate));
        log.info("User {} added", userCreate.getEmail());
        return UserMapper.toUserResponse(user);
    }

    /**
     * @param userId - ID пользователя которого надо вернуть
     * @return Метод возвращает пользователя
     * если запрашиваемого пользователя нет выбрасывает исключение
     */
    @Override
    public UserResponse getUser(final Long userId) {
        UserResponse user = UserMapper.toUserResponse(userDAO.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("Данный пользователь не найден")));
        log.info("User {} found", user);
        return user;
    }

    /**
     * @return список всех пользователей
     */
    @Override
    public List<UserResponse> getAllUsers() {
        List<UserResponse> users = userDAO.getAllUsers().stream().map(UserMapper::toUserResponse).toList();
        log.info("Users {}", users);
        return users;
    }

    /**
     * Метод удаляет пользователя
     *
     * @param userId - ID пользователя которогоо надо удалить
     */
    @Override
    public void deleteUser(final Long userId) {
        userDAO.deleteUser(userId);
        log.info("User #{} deleted", userId);
    }

    /**
     * Метод для обновления пользователя
     * проверяет что обновляемый пользователь существует
     * в противно случае выбрасывает соответсвуеющее исключение
     *
     * @param userUpdate - тело запроса содержащее поля для обновления
     * @param userId     - ID пользователя которого надо обновить
     * @return обновленного пользователя
     */
    @Override
    public UserResponse updateUser(final UserUpdate userUpdate, final Long userId) {
        User oldUserData = userDAO.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("Данный пользователь не найден"));


        User newUserData = UserMapper.toUser(userUpdate, userId);
        newUserData.setEmail(userUpdate.getEmail() != null ? userUpdate.getEmail() : oldUserData.getEmail());
        newUserData.setName(userUpdate.getName() != null ? userUpdate.getName() : oldUserData.getName());


        UserResponse user = UserMapper.toUserResponse(userDAO.updateUser(newUserData));
        log.info("User {} updated", user);
        return user;
    }
}
