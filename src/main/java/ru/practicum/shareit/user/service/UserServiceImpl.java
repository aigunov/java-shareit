package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
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
     * @param userDtoCreate - тело запроса
     * @return добавленного пользователя
     */
    @Override
    public UserDtoResponse addUser(final UserDtoCreate userDtoCreate) {
        User user = userDAO.saveUser(UserMapper.toUser(userDtoCreate));
        log.info("User {} added", userDtoCreate.getEmail());
        return UserMapper.toUserDto(user);
    }

    /**
     * @param userId - ID пользователя которого надо вернуть
     * @return Метод возвращает пользователя
     * если запрашиваемого пользователя нет выбрасывает исключение
     */
    @Override
    public UserDtoResponse getUser(final Long userId) {
        UserDtoResponse user = UserMapper.toUserDto(userDAO.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("Данный пользователь не найден")));
        log.info("User {} found", user);
        return user;
    }

    /**
     * @return список всех пользователей
     */
    @Override
    public List<UserDtoResponse> getAllUsers() {
        List<UserDtoResponse> users = userDAO.getAllUsers().stream().map(UserMapper::toUserDto).toList();
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
     * @param userDtoUpdate - тело запроса содержащее поля для обновления
     * @param userId        - ID пользователя которого надо обновить
     * @return обновленного пользователя
     */
    @Override
    public UserDtoResponse updateUser(final UserDtoUpdate userDtoUpdate, final Long userId) {
        userDAO.getUser(userId).orElseThrow(() -> new NoSuchElementException("Данный пользователь не найден"));

        UserDtoResponse user = UserMapper.toUserDto(userDAO.updateUser(userDtoUpdate, userId));
        log.info("User {} updated", user);
        return user;
    }
}
