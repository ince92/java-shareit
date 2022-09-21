package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemRequestDto create(ItemRequestDto itemRequest) {
        User requester = userRepository.findById(itemRequest.getRequesterId()).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));

        ItemRequest newItemRequest = ItemRequestMapper.toItemRequest(itemRequest, requester);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(newItemRequest));
    }

    public List<ItemRequestDtoResponse> findItemRequestsByRequester(long userId) {
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        List<ItemRequest> requestsList = itemRequestRepository.findItemRequestByRequesterIdOrderByCreatedDesc(requester.getId());
        return getItemRequestList(requestsList);
    }

    public List<ItemRequestDtoResponse> findItemRequests(long userId, Pageable pageRequest) {
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        List<ItemRequest> requestsList = itemRequestRepository.findAllItemRequest(requester.getId(), pageRequest);

        return getItemRequestList(requestsList);
    }

    public ItemRequestDtoResponse findItemRequestsById(long requestId, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден!");
        }
        ItemRequest request = itemRequestRepository.findItemRequestById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с таким id не найден!"));
        return ItemRequestMapper.toItemRequestDtoResponse(request,
                itemRepository.findItemsByRequestId(request.getId()).stream().map(ItemMapper::toItemDto)
                        .collect(Collectors.toList()));
    }

    private List<ItemRequestDtoResponse> getItemRequestList(List<ItemRequest> requestsList) {
        List<ItemRequestDtoResponse> requestsDtoList = new ArrayList<>();
        for (ItemRequest request : requestsList) {
            requestsDtoList.add(ItemRequestMapper.toItemRequestDtoResponse(request,
                    itemRepository.findItemsByRequestId(request.getId()).stream().map(ItemMapper::toItemDto)
                            .collect(Collectors.toList())));
        }
        return requestsDtoList;
    }
}