package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingResponseDto toBookingDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getStatus(),
                booking.getBooker()
        );
    }

    public static Booking toBooking(BookingRequestDto booking, User booker, Item item, BookingStatus bookingStatus) {
        return new Booking(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                item,
                booker,
                bookingStatus
        );
    }

}
