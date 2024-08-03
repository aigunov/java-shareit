package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * @author Mr.White
 * Интерфейс для работы с любиым user хранилищем(временным, файловым, базой данных)
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
