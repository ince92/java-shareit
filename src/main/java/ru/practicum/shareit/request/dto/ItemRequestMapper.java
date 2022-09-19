package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester() != null ? itemRequest.getRequester().getId() : null,
                itemRequest.getCreated()
        );
    }

    public static ItemRequestDtoResponse toItemRequestDtoResponse(ItemRequest itemRequest, List<ItemDto> itemList) {
        return new ItemRequestDtoResponse(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemList
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequest, User requester) {
        return new ItemRequest(
                itemRequest.getId(),
                itemRequest.getDescription(),
                requester,
                itemRequest.getCreated()
        );
    }
}
