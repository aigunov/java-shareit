package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoSuchUserException;
import ru.practicum.shareit.exception.UserWithSuchEmailAlreadyExist;
import ru.practicum.shareit.user.dto.UserDtoToCreate;
import ru.practicum.shareit.user.dto.UserDtoToUpdate;
import ru.practicum.shareit.utils.Mapper;

import java.util.List;

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
     * @param userDtoToCreate - тело запроса
     * @return добавленного пользователя
     */
    @Override
    public User addUser(final UserDtoToCreate userDtoToCreate) {
        userDAO.getAllUsers().stream().forEach(user -> {
            if (user.getEmail().equals(userDtoToCreate.getEmail())) {
                throw new UserWithSuchEmailAlreadyExist(
                        String.format("Пользователь с почтой %s уже существует", userDtoToCreate.getEmail()));
            }
        });
        User user = userDAO.saveUser(Mapper.toUser(userDtoToCreate));
        log.info("User {} added", userDtoToCreate.getEmail());
        return user;
    }

    /**
     * @param userId - ID пользователя которого надо вернуть
     * @return Метод возвращает пользователя
     * если запрашиваемого пользователя нет выбрасывает исключение
     */
    @Override
    public User getUser(final Long userId) {
        User user = userDAO.getUser(userId).orElseThrow(() -> new NoSuchUserException("Данный пользователь не найден"));
        log.info("User {} found", user);
        return user;
    }

    /**
     * @return список всех пользователей
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
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
     * @param userDto - тело запроса содержащее поля для обновления
     * @param userId  - ID пользователя которого надо обновить
     * @return обновленного пользователя
     */
    @Override
    public User updateUser(final UserDtoToUpdate userDto, final Long userId) {
        userDAO.getUser(userId).orElseThrow(() -> new NoSuchUserException("Данный пользователь не найден"));
        userDAO.getAllUsers().stream().forEach(user -> {
            if (user.getEmail().equals(userDto.getEmail()) && user.getId() != userId) {
                throw new UserWithSuchEmailAlreadyExist(
                        String.format("Пользователь с почтой %s уже существует", userDto.getEmail()));
            }
        });
        User user = userDAO.updateUser(userDto, userId);
        log.info("User {} updated", user);
        return user;
    }
}
