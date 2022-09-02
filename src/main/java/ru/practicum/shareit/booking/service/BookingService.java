package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {

    BookingDtoOut create(BookingDto bookingDto, long bookerId);

    BookingDtoOut bookingApproving(long bookingId, long userId, Boolean approved);

    BookingDtoOut getBookingById(long bookingId, long userId);

    List<BookingDtoOut> getBookingList(long userId, String state);

    List<BookingDtoOut> getOwnerBookingList(long userId, String state);
}
