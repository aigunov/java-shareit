package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;
/**
 * @author Mr.White
 * Интерфейс взаимодействия с хранилищами
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = """
            select it.*
            from items as it
            where it.is_available = true
            and (lower(it.name) like :text or lower(it.description) like :text)
            """, nativeQuery = true)
    List<Item> search(@Param("text") final String text);

    List<Item> findAllByOwnerIdOrderByIdAsc(Long id);

    Optional<Item> findByIdAndOwnerId(long id, long ownerId);

    @Query(value = """
            select it.*
            from items as it
            where request_id = :requestId
            """, nativeQuery = true)
    List<Item> getItemByRequestId(@Param("requestId") final long requestId);
}
