package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDtoOutLess;
import ru.practicum.shareit.comments.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDtoWithBookingDates toItemDtoWithBookingDates(Item item, BookingDtoOutLess lastBooking,
                                                                    BookingDtoOutLess nextBooking, List<CommentDtoOut> comments) {
        return new ItemDtoWithBookingDates(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                lastBooking,
                nextBooking,
                comments

        );
    }

    public static Item toItem(ItemDto item, User owner) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                owner,
                null
        );
    }
}
