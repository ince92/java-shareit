package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Validated({Create.class}) @RequestBody ItemDto item) {
        log.info("Creating item - {}", item.getName());
        return itemClient.create(item, userId);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> update(@PathVariable("itemId") long itemId,
                          @Validated({Update.class}) @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Updating item- {}", itemId);
        return itemClient.update(item, itemId, userId);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable("itemId") long itemId,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> findOwnersItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.findOwnersItems(userId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> findAvailableItems(@RequestParam(name = "text") String text,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.findAvailableItems(text.toLowerCase(),from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Validated({Create.class}) @RequestBody CommentRequestDto comment,
                                         @PathVariable("itemId") long itemId) {
        comment.setCreated(LocalDateTime.now());
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        log.info("Creating comment - {}", comment.getId());
        return itemClient.addComment(comment);
    }
}
