package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
     ItemDto create(ItemDto user, long userId);
     ItemDto update(ItemDto user, long itemId, long userId);
     ItemDto findItemById(long id);
     List<ItemDto> findOwnersItems(long userId);
     List<ItemDto> findAvailableItems(String text);
}
