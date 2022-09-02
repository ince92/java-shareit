package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping()
    public BookingDtoOut create(@Validated({Create.class}) @RequestBody BookingDto booking,
                                @RequestHeader("X-Sharer-User-Id") long userId) {
        BookingDtoOut bookingDto = bookingService.create(booking, userId);
        log.info("Добавлен букинг - {}", bookingDto.getId());
        return bookingDto;
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDtoOut bookingApproving(@PathVariable("bookingId") long bookingId,
                                          @RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(name = "approved") boolean approved) {

        BookingDtoOut bookingDto = bookingService.bookingApproving(bookingId, userId, approved);
        log.info("Обновлен букинг- {}", bookingDto.getId());
        return bookingDto;
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoOut getBookingById(@PathVariable("bookingId") long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }


    @GetMapping(value = "/owner")
    public List<BookingDtoOut> getOwnerBookingList(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getOwnerBookingList(userId, state);
    }

    @GetMapping()
    public List<BookingDtoOut> getBookingList(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingList(userId, state);
    }


}
