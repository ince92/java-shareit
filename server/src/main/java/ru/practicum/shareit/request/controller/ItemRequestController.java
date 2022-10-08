package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDto create(@RequestBody ItemRequestDto request,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        request.setRequesterId(userId);
        request.setCreated(LocalDateTime.now());
        ItemRequestDto itemRequestDto = itemRequestService.create(request);
        log.info("Добавлен запрос на вещь - {}", itemRequestDto.getId());
        return itemRequestDto;
    }

    @GetMapping()
    public List<ItemRequestDtoResponse> findItemRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.findItemRequestsByRequester(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoResponse> findItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemRequestService.findItemRequests(userId, pageRequest);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoResponse findItemRequestsById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @PathVariable("requestId") long requestId) {
        return itemRequestService.findItemRequestsById(requestId, userId);
    }

}
