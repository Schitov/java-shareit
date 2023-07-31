package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exception.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService  {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto saveBooking(BookingCreateDto bookingDto, long userId) {
        log.debug("UserId: {userId}. BookingDto: {bookingDto}");
        User user = userRepository.findById(userId).orElseThrow(() -> new ExistenceOfUserException("Not found user"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new ExistenceOfItemException("Not found Item"));
        log.debug("User: {user}. Item: {item}. BookingDto: {bookingDto}");
        if (item.getAvailable() && (user.getId() != item.getOwner().getId())) {
            return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toNewBooking(bookingDto, user, item)));
        } else if (user.equals(item.getOwner())) {
            throw new BookingException("Booking coudn't be completed");
        } else {
            throw new BlockedException("Item is blocked");
        }
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(long userId, long bookingId, Boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ExistenceOfObjectException("Бронирование не найдено"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new BookingException("Booking coudn't be completed");
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new BlockedException("Item is blocked");
        }
        booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ExistenceOfObjectException("Booking isn't existed"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ExistenceOfObjectException("Booking isn't existed");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUsersBookings(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new ExistenceOfUserException("User isn't existed"));
        List<Booking> res = null;
        switch (state) {
            case CURRENT:
                res = bookingRepository.findBookingsByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case FUTURE:
                res = bookingRepository.findBookingsByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case PAST:
                res = bookingRepository.findBookingsByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case WAITING:
                res = bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                res = bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            default:
                res = bookingRepository.findBookingsByBooker_IdOrderByStartDesc(userId);
        }
        return res.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getUserItemsBookings(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new ExistenceOfUserException("User isn't existed"));
        List<Long> itemIds = itemRepository.findByOwnerId(userId).stream().map(Item::getId).collect(Collectors.toList());
        List<Booking> res = null;
        switch (state) {
            case CURRENT:
                res = bookingRepository.findBookingsByStartBeforeAndEndAfterAndItem_IdInOrderByStartDesc(
                        LocalDateTime.now(), LocalDateTime.now(), itemIds);
                break;
            case FUTURE:
                res = bookingRepository.findBookingsByStartAfterAndItem_IdInOrderByStartDesc(LocalDateTime.now(), itemIds);
                break;
            case PAST:
                res = bookingRepository.findBookingsByStartBeforeAndEndBeforeAndItem_IdInOrderByStartDesc(
                        LocalDateTime.now(), LocalDateTime.now(), itemIds);
                break;
            case WAITING:
                res = bookingRepository.findBookingsByStatusAndItem_IdInOrderByStartDesc(Status.WAITING, itemIds);
                break;
            case REJECTED:
                res = bookingRepository.findBookingsByStatusAndItem_IdInOrderByStartDesc(Status.REJECTED, itemIds);
                break;
            default:
                res = bookingRepository.findBookingsByItem_IdInOrderByStartDesc(itemIds);
        }
        return res.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
