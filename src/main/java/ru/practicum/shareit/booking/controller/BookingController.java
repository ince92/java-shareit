package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping()
    public BookingResponseDto create(@Validated({Create.class}) @RequestBody BookingRequestDto booking,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        BookingResponseDto bookingDto = bookingService.create(booking, userId);
        log.info("Добавлен букинг - {}", bookingDto.getId());
        return bookingDto;
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingResponseDto bookingApproving(@PathVariable("bookingId") long bookingId,
                                               @RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "approved") boolean approved) {

        BookingResponseDto bookingDto = bookingService.bookingApproving(bookingId, userId, approved);
        log.info("Обновлен букинг- {}", bookingDto.getId());
        return bookingDto;
    }

    @GetMapping(value = "/{bookingId}")
    public BookingResponseDto getBookingById(@PathVariable("bookingId") long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }


    @GetMapping(value = "/owner")
    public List<BookingResponseDto> getOwnerBookingList(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getOwnerBookingList(userId, state);
    }

    @GetMapping()
    public List<BookingResponseDto> getBookingList(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingList(userId, state);
    }


}
