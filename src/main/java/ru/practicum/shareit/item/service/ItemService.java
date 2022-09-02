package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto user, long userId);

    ItemDto update(ItemDto user, long itemId, long userId);

    ItemDtoWithBookingDates findItemById(long id, long userId);

    List<ItemDtoWithBookingDates> findOwnersItems(long userId);

    List<ItemDto> findAvailableItems(String text);

    CommentDtoOut addComment(CommentDto comment);
}
