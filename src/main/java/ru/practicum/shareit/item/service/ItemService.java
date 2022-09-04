package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto user, long userId);

    ItemDto update(ItemDto user, long itemId, long userId);

    ItemDtoWithBookingDates findItemById(long id, long userId);

    List<ItemDtoWithBookingDates> findOwnersItems(long userId);

    List<ItemDto> findAvailableItems(String text);

    CommentResponseDto addComment(CommentRequestDto comment);
}
