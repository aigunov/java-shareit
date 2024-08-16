package ru.practicum.shareit.request.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = """
            select *
            from request as req
            where requester_id = :userId
            order by created desc
            """, nativeQuery = true)
    List<Request> findAllById(@Param("userId") final long userId);

}
