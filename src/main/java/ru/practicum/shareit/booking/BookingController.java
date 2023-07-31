package ru.practicum.shareit.booking;


import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.State;

import java.util.List;

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
        return bookingService.saveBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam Boolean isApproved) {
        return bookingService.updateBookingStatus(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUsersBookings(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                             @RequestParam State state) {
        return bookingService.getUsersBookings(userId, state);
    }

    @GetMapping("/user-items")
    public List<BookingDto> getUserItemsBookings(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                 @RequestParam State state) {
        return bookingService.getUserItemsBookings(userId, state);
    }
}
