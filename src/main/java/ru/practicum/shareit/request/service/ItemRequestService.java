package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequest);

    List<ItemRequestDtoResponse> findItemRequestsByRequester(long userid);

    List<ItemRequestDtoResponse> findItemRequests(long userid, Pageable pageRequest);

    ItemRequestDtoResponse findItemRequestsById(long requestId, long userid);
}
