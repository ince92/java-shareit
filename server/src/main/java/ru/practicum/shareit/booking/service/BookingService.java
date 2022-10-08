package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(BookingRequestDto bookingRequestDto, long bookerId);

    BookingResponseDto bookingApproving(long bookingId, long userId, Boolean approved);

    BookingResponseDto getBookingById(long bookingId, long userId);

    List<BookingResponseDto> getBookingList(long userId, String state, Pageable pageRequest);

    List<BookingResponseDto> getOwnerBookingList(long userId, String state, Pageable pageRequest);
}
