package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDtoToUpdate;

import java.util.List;
import java.util.Optional;

/**
 * @author Mr.White
 * Класс реализующий интерфейс UserDAO для работы с временным хранилищем
 */
@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final List<User> users;
    private Long counter = 1L;

    /**
     * @return список всех пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return users;
    }

    /**
     * @param user - пользователь которого необходимо сохранить в базе
     * @return сохраненного пользователя
     */
    @Override
    public User saveUser(final User user) {
        user.setId(counter++);
        users.add(user);
        return user;
    }

    /**
     * удаляет из хранилища пользователя
     *
     * @param userId - ID пользователя которого надо удалить
     */
    @Override
    public void deleteUser(final long userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    /**
     * @param id - ID пользователя которого надо вернуть
     * @return запрашиваемого по ID пользователя
     */
    @Override
    public Optional<User> getUser(final long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    /**
     * Обновляет в определенного пользователя в хранилище
     *
     * @param userDto - объект содержащий поля для обновления полей пользователя
     * @param userId  - ID пользователя которого необходимо обновить
     * @return обновленного пользователя
     */
    @Override
    public User updateUser(final UserDtoToUpdate userDto, final long userId) {
        User user = getUser(userId).get();
        user.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        return user;
    }
}
