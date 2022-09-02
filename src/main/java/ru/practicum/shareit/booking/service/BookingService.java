package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {

    BookingDtoOut create(BookingDto bookingDto, Long bookerId);

    BookingDtoOut bookingApproving(Long bookingId, Long userId, Boolean approved);

    BookingDtoOut getBookingById(Long bookingId, Long userId);

    List<BookingDtoOut> getBookingList(Long userId, String state);

    List<BookingDtoOut> getOwnerBookingList(Long userId, String state);
}
