package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
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

    public static ItemDtoWithBookingDates toItemDtoWithBookingDates(Item item, Booking lastBooking,
                                                                    Booking nextBooking, List<CommentResponseDto> comments) {
        return new ItemDtoWithBookingDates(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                lastBooking != null ? new ItemDtoWithBookingDates.BookingResponseDtoLess(lastBooking.getId(),
                        lastBooking.getBooker().getId()) : null,
                nextBooking != null ? new ItemDtoWithBookingDates.BookingResponseDtoLess(nextBooking.getId(),
                        nextBooking.getBooker().getId()) : null,
                comments
        );
    }

    public static Item toItem(ItemDto item, User owner, ItemRequest request) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                owner,
                request
        );
    }
}
