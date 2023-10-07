package ru.practicum.shareit.booking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.exception.exceptions.UnsupportedStateException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                    @RequestBody BookingCreateDto bookingDto) {
        log.debug("Параметр, полученный в методе createBooking: userId - {}, bookingDto ID - {}",
                userId, bookingDto.getId());
        return bookingService.saveBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam Boolean approved) {
        log.debug("Параметр, полученный в методе updateBookingStatus: userId - {}, bookingId - {}, approval status - {}"
                , userId, bookingId, approved);
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId) {
        log.debug("Параметр, полученный в методе getBookingById: userId - {}, bookingId - {}"
                , userId, bookingId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUsersBookings(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                             @RequestParam(value = "state", required = false, defaultValue = "ALL")
                                             String state) {
        log.debug("Параметр, полученный в методе getUsersBookings: userId - {}, state - {}"
                , userId, state);
        try {
            return bookingService.getUsersBookings(userId, State.valueOf(state));
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException(String.format("Unknown state: %s", state));
        }

    }

    @GetMapping("/owner")
    public List<BookingDto> getUserItemsBookings(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                 @RequestParam(value = "state", required = false, defaultValue = "ALL")
                                                 String state) {
        log.debug("Параметр, полученный в методе getUserItemsBookings: userId - {}, state - {}"
                , userId, state);
        try {
            return bookingService.getUserItemsBookings(userId, State.valueOf(state));
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException(String.format("Unknown state: %s", state));
        }
    }
}
