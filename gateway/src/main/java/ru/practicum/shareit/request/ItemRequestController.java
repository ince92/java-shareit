package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping()
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody ItemRequestDto request,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        request.setRequesterId(userId);
        request.setCreated(LocalDateTime.now());
        log.info("Creating request - {}", request.getId());
        return itemRequestClient.create(request);
    }

    @GetMapping()
    public ResponseEntity<Object> findItemRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.findItemRequestsByRequester(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
       return itemRequestClient.findItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestsById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @PathVariable("requestId") long requestId) {
        return itemRequestClient.findItemRequestsById(requestId, userId);
    }

}
