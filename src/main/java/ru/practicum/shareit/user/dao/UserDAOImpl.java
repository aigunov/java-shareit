package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * @author Mr.White
 * Класс реализующий интерфейс UserDAO для работы с временным хранилищем
 */
@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final HashMap<Long, User> users = new HashMap<>();
    private final Set<String> emailUniqSet = new HashSet();
    private Long counter = 0L;

    /**
     * @return список всех пользователей
     */
    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>(users.values());
        return usersList;
    }

    /**
     * @param user - пользователь которого необходимо сохранить в базе
     * @return сохраненного пользователя
     */
    @Override
    public User saveUser(final User user) {
        if (emailUniqSet.contains(user.getEmail())) {
            throw new DataAlreadyExistException("Email: " + user.getEmail() + " already exists");
        }
        emailUniqSet.add(user.getEmail());
        user.setId(++counter);
        users.put(counter, user);
        return user;
    }

    /**
     * удаляет из хранилища пользователя
     *
     * @param userId - ID пользователя которого надо удалить
     */
    @Override
    public void deleteUser(final long userId) {
        emailUniqSet.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    /**
     * @param id - ID пользователя которого надо вернуть
     * @return запрашиваемого по ID пользователя
     */
    @Override
    public Optional<User> getUser(final long id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * Обновляет в определенного пользователя в хранилище
     *
     * @param userDto - объект содержащий поля для обновления полей пользователя
     * @param userId  - ID пользователя которого необходимо обновить
     * @return обновленного пользователя
     */
    @Override
    public User updateUser(final UserDtoUpdate userDto, final long userId) {
        User user = getUser(userId).get();
        if (emailUniqSet.contains(userDto.getEmail()) && checkEmailIsBusyOtherUsers(userDto.getEmail(), userId)) {
            throw new DataAlreadyExistException("Email: " + user.getEmail() + " already exists");
        }
        emailUniqSet.remove(user.getEmail());
        user.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        emailUniqSet.add(user.getEmail());
        return user;
    }

    private boolean checkEmailIsBusyOtherUsers(String email, long userId) {
        return users.values().stream()
                .filter(user -> user.getId() != userId)
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
