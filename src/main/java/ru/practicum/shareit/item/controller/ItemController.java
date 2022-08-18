package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping()
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated({Create.class}) @RequestBody ItemDto item) {
        ItemDto newItem = itemService.create(item, userId);
        log.info("Добавлен предмет - {}", newItem.getName());
        return newItem;
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto update(@PathVariable("itemId") long itemId,
                          @Validated({Update.class}) @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemDto updatedItem = itemService.update(item, itemId, userId);
        log.info("Обновлен предмет- {}", updatedItem.getName());
        return updatedItem;
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto findItemById(@PathVariable("itemId") long itemId) {
        return itemService.findItemById(itemId);
    }

    @GetMapping()
    public List<ItemDto> findOwnersItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findOwnersItems(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> findAvailableItems(@RequestParam(name = "text")
                                            String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.findAvailableItems(text.toLowerCase());
    }
}
