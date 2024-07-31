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

    List<Booking> findAllByBookerIdAndBookingStatusOrderByStartDesc(final Long bookerId, final Status status);

    List<Booking> findAllByBookerIdAndStartIsLessThanAndEndIsGreaterThanOrderByStartDesc(final Long bookerId,
                                                                                         final LocalDateTime start,
                                                                                         final LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndIsLessThanOrderByStartDesc(final Long bookerId, final LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartIsGreaterThanOrderByStartDesc(final Long bookerId,
                                                                         final LocalDateTime start);

    // -------------- The user requests a list of rents that he makes --------------

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(final Long itemOwnerId);

    List<Booking> findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(final Long itemOwnerId, final Status status);

    List<Booking> findAllByItemOwnerIdAndStartIsGreaterThanOrderByStartDesc(final Long itemOwnerId,
                                                                            final LocalDateTime start);

    List<Booking> findAllByItemOwnerIdAndEndIsLessThanOrderByStartDesc(final long userId, final LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartIsLessThanAndEndIsGreaterThanOrderByStartDesc(final Long itemOwnerId,
                                                                                            final LocalDateTime start,
                                                                                            final LocalDateTime end);


    Optional<Booking> findTop1BookingByItem_IdAndStartBeforeAndBookingStatusOrderByEndDesc(final Long itemId,
                                                                                           final LocalDateTime end,
                                                                                           final Status status);

    Optional<Booking> findTop1BookingByItem_IdAndStartAfterAndBookingStatusOrderByEndAsc(final Long itemId,
                                                                                         final LocalDateTime now,
                                                                                         final Status status);

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
            and b.status = :status
            order by b.start_date desc  
            limit 1
            """, nativeQuery = true)
    Optional<Booking> searchForBookerIdAndItemId(@Param("bookerId") final Long bookerId,
                                                 @Param("itemId") final Long itemId,
                                                 @Param("now") final LocalDateTime now,
                                                 @Param("status") final String status);

}
