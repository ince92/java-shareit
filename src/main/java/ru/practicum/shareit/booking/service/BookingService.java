package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(BookingRequestDto bookingRequestDto, long bookerId);

    BookingResponseDto bookingApproving(long bookingId, long userId, Boolean approved);

    BookingResponseDto getBookingById(long bookingId, long userId);

    List<BookingResponseDto> getBookingList(long userId, String state);

    List<BookingResponseDto> getOwnerBookingList(long userId, String state);
}
