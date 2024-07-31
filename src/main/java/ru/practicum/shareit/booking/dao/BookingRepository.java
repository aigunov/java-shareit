package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // -------------- the user requests a list of his rentals --------------
    @Query(value = """
            select bk.*        
            from bookings as bk
            where bk.booker_id = :booker_id
            order by bk.start_date desc
            """, nativeQuery = true)
    List<Booking> findAllBookingByBookerId(@Param("booker_id") final Long bookerId);

    List<Booking> findAllByBookerIdAndBookingStatusOrderByStartDesc(Long bookerId, Status status);

    List<Booking> findAllByBookerIdAndStartIsLessThanAndEndIsGreaterThanOrderByStartDesc(Long bookerId,
                                                                                         LocalDateTime start,
                                                                                         LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndIsLessThanOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartIsGreaterThanOrderByStartDesc(Long bookerId, LocalDateTime start);

    // -------------- The user requests a list of rents that he makes --------------

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long itemOwnerId);

    List<Booking> findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(Long itemOwnerId, Status status);

    List<Booking> findAllByItemOwnerIdAndStartIsGreaterThanOrderByStartDesc(Long itemOwnerId, LocalDateTime start);

    List<Booking> findAllByItemOwnerIdAndEndIsLessThanOrderByStartDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartIsLessThanAndEndIsGreaterThanOrderByStartDesc(Long itemOwnerId,
                                                                                            LocalDateTime start,
                                                                                            LocalDateTime end);


    Optional<Booking> findTop1BookingByItem_IdAndStartBeforeAndBookingStatusOrderByEndDesc(Long itemId, LocalDateTime end,
                                                                                           Status status);

    Optional<Booking> findTop1BookingByItem_IdAndStartAfterAndBookingStatusOrderByEndAsc(Long itemId, LocalDateTime now,
                                                                                         Status status);


    @Query(value = """
            select bk.*
            from bookings as bk
            where bk.item_id = :itemId
            and bk.booker_id = :bookerId
            and bk.status = :status
            and bk.start_date < :date
            """, nativeQuery = true)
    Optional<Booking> findByBookerIdAndItemIdAndBookingStatusAndStartIsLessThan(@Param("bookerId") final long bookerId,
                                                                                @Param("itemId") final long itemId,
                                                                                @Param("status") final Status bookingStatus,
                                                                                @Param("date") final LocalDateTime now);

    Optional<Booking> findTop1BookingByItemIdAndBookerIdAndEndBeforeAndBookingStatusOrderByEndDesc(Long itemId, Long bookerId,
                                                                                                   LocalDateTime now,
                                                                                                   Status status);

    /**
     * Поиск бронирования по id пользователя и вещи
     *
     * @param bookerId id пользователя
     * @param itemId   id вещи
     * @param now      точка во времени
     * @return последнее бронирование пользователя для заданной точки
     */
    @Query(value = """
            select * 
            from bookings as b  
            where b.item_id = :itemId
            and b.booker_id = :bookerId  
            and b.start_date < :now  
            order by b.start_date desc  
            limit 1
            """, nativeQuery = true)
    Booking searchForBookerIdAndItemId(@Param("bookerId") final Long bookerId,
                                       @Param("itemId") final Long itemId,
                                       @Param("now") final LocalDateTime now);

}
