package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBooker_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findBookingsByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBooker_IdAndStatusOrderByStartDesc(long userId, Status status);

    List<Booking> findBookingsByBooker_IdOrderByStartDesc(long userId);

    List<Booking> findBookingsByStartBeforeAndEndAfterAndItem_IdInOrderByStartDesc(LocalDateTime start, LocalDateTime end, List<Long> itemIds);

    List<Booking> findBookingsByStartAfterAndItem_IdInOrderByStartDesc(LocalDateTime start, List<Long> itemIds);

    List<Booking> findBookingsByStartBeforeAndEndBeforeAndItem_IdInOrderByStartDesc(LocalDateTime start, LocalDateTime end, List<Long> itemIds);

    List<Booking> findBookingsByStatusAndItem_IdInOrderByStartDesc(Status status, List<Long> itemIds);

    List<Booking> findBookingsByItem_IdInOrderByStartDesc(List<Long> itemIds);

    Optional<Booking> findFirstBookingByBooker_IdAndItem_IdAndEndBefore(long bookerId, long itemId, LocalDateTime before);
}
