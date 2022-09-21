package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

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
                                                        @RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return bookingService.getOwnerBookingList(userId, state,pageRequest);
    }

    @GetMapping()
    public List<BookingResponseDto> getBookingList(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return bookingService.getBookingList(userId, state,pageRequest);
    }


}
