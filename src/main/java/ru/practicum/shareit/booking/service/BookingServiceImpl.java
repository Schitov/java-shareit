package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto saveBooking(BookingCreateDto bookingDto, long userId) {
        log.debug("UserId: {}. BookingDto: {}", userId, bookingDto.getId());
        if ((bookingDto.getEnd() == null) || (bookingDto.getStart() == null)) { // Дата начала и дата окончания заданы
            throw new ExistenceDateException("Empty date");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ExistenceOfUserException("Not found user"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new ExistenceOfItemException("Not found Item"));
        log.debug("User: {uer}. Item: {}. BookingDto: {}", userId, item.getId(), bookingDto.getId());

        if (user.equals(item.getOwner())) {
            throw new BookingException("Booking coudn't be completed");
        } else if (bookingDto.getEnd().isBefore(bookingDto.getStart()) // Дата старта раньше даты окончания
                || bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new DateIntersectionException("Date intersection");
        } else if (bookingDto.getStart().isBefore(LocalDateTime.now())) { // Дата начала и дата окончания аренды не могут находиться в прошлом
            throw new DateIntersectionException("End date couldn't be in past");
        } else if (item.getAvailable() && (user.getId() != item.getOwner().getId())) {
            return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toNewBooking(bookingDto, user, item)));
        } else {
            throw new BlockedException("Item is blocked");
        }
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(long userId, long bookingId, Boolean isApproved) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ExistenceOfBookingException("Booking is not found, sorry"));

        log.debug("Значения в updateBookingStatus: " +
                "User: {}. Item: {}. Approval: {}", userId, bookingId, isApproved);

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

        log.debug("Значения в getBookingById: " +
                "User: {}. bookingId: {}", userId, bookingId);

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ExistenceOfBookingException("Booking isn't existed"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ExistenceOfUserException("User isn't existed");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUsersBookings(long userId, State state) {

        log.debug("Значения в getUsersBookings: " +
                "userId: {}. State: {}", userId, state);

        userRepository.findById(userId).orElseThrow(() -> new ExistenceOfUserException("User isn't existed"));
        List<Booking> res = null;
        switch (state) {
            case CURRENT:
                res = bookingRepository.findBookingsByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case FUTURE:
                res = bookingRepository
                        .findBookingsByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
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

        log.debug("Значения в getUserItemsBookings: userId: {}. State: {}", userId, state);

        List<Long> itemIds = itemRepository
                .findByOwnerId(userId, Sort.by("id")).stream().map(Item::getId).collect(Collectors.toList());
        List<Booking> res = null;
        switch (state) {
            case CURRENT:
                res = bookingRepository.findBookingsByStartBeforeAndEndAfterAndItem_IdInOrderByStartDesc(
                        LocalDateTime.now(), LocalDateTime.now(), itemIds);
                break;
            case FUTURE:
                res = bookingRepository
                        .findBookingsByStartAfterAndItem_IdInOrderByStartDesc(LocalDateTime.now(), itemIds);
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
