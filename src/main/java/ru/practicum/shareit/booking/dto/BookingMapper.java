package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDtoOut toBookingDto(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto booking, User booker, Item item, BookingStatus bookingStatus) {
        return new Booking(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                item,
                booker,
                bookingStatus
        );
    }

    public static BookingDtoOutLess toBookingDtoLess(Booking booking) {
        return new BookingDtoOutLess(
                booking.getId(),
                booking.getBooker().getId()
        );
    }
}
