package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.enums.State;

import java.util.List;

public interface BookingService {
    BookingDto saveBooking(BookingCreateDto bookingDto, long userId);
    BookingDto updateBookingStatus(long userId, long bookingId, Boolean isApproved);
    BookingDto getBookingById(long userId, long bookingId);
    List<BookingDto> getUsersBookings(long userId, State state);
    List<BookingDto> getUserItemsBookings(long userId, State state);
}
