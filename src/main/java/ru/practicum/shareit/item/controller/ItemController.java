package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
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
    public ItemDtoWithBookingDates findItemById(@PathVariable("itemId") long itemId,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping()
    public List<ItemDtoWithBookingDates> findOwnersItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemService.findOwnersItems(userId, pageRequest);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> findAvailableItems(@RequestParam(name = "text") String text,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemService.findAvailableItems(text.toLowerCase(),pageRequest);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Validated({Create.class}) @RequestBody CommentRequestDto comment,
                                         @PathVariable("itemId") long itemId) {
        comment.setCreated(LocalDateTime.now());
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        CommentResponseDto newComment = itemService.addComment(comment);
        log.info("Добавлен комментарий - {}", newComment.getId());
        return newComment;
    }
}
