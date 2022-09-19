package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto item, long userId);

    ItemDto update(ItemDto item, long itemId, long userId);

    ItemDtoWithBookingDates findItemById(long id, long userId);

    List<ItemDtoWithBookingDates> findOwnersItems(long userId, Pageable pageRequest);

    List<ItemDto> findAvailableItems(String text, Pageable pageRequest);

    CommentResponseDto addComment(CommentRequestDto comment);
}
