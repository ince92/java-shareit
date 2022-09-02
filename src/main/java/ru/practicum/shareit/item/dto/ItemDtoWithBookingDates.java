package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOutLess;
import ru.practicum.shareit.comments.dto.CommentDtoOut;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithBookingDates {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private BookingDtoOutLess lastBooking;
    private BookingDtoOutLess nextBooking;
    private List<CommentDtoOut> comments;
}
