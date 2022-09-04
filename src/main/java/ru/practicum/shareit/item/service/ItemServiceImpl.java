package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public ItemDto create(ItemDto item, long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Item newItem = ItemMapper.toItem(item, owner);
        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    public ItemDto update(ItemDto item, long itemId, long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Item updatedItem = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Предмет с таким id не найден!"));
        if (!updatedItem.getOwner().getId().equals(owner.getId())) {
            throw new NotFoundException("Пользователь не может обновить чужой предмет!");
        }
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        itemRepository.save(updatedItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDtoWithBookingDates findItemById(long id, long userId) {
        return convertItem(itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Предмет с таким id не найден!")), userId);
    }

    @Override
    public List<ItemDtoWithBookingDates> findOwnersItems(long userId) {
        return itemRepository.findOwnersItems(userId).stream().map(i -> convertItem(i, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAvailableItems(String text) {
        return itemRepository.findAvailableItems(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private ItemDtoWithBookingDates convertItem(Item item, long userId) {
        List<Booking> bookingList = bookingRepository.findAllByItemId(item.getId());

        LocalDateTime now = LocalDateTime.now();
        Booking current = null;
        Booking next = null;
        if (item.getOwner().getId() == userId) {
            for (Booking booking : bookingList) {
                if (booking.getStart().isBefore(now)) {
                    if ((current == null) || (current.getStart().isBefore(booking.getStart())))
                        current = booking;
                    continue;
                }

                if (booking.getStart().isAfter(now)) {
                    if ((next == null) || (next.getStart().isAfter(booking.getStart()))) {
                        next = booking;
                    }
                }
            }
        }

        List<CommentResponseDto> commentList = commentRepository.findAllByItemId(item.getId()).stream()
                .map(CommentMapper::toCommentDtoOut).collect(Collectors.toList());

        return ItemMapper.toItemDtoWithBookingDates(item, current, next, commentList);


    }

    public CommentResponseDto addComment(CommentRequestDto comment) {
        User author = userRepository.findById(comment.getAuthorId()).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Item item = itemRepository.findById(comment.getItemId()).orElseThrow(() ->
                new NotFoundException("Предмет с таким id не найден!"));

        List<Booking> bookingList = bookingRepository.findAllUserBookings(comment.getAuthorId(),
                comment.getItemId(), comment.getCreated());
        if (bookingList.isEmpty()) {
            throw new ValidationException("Нет доступа к созданию отзыва!");
        }
        Comment newComment = commentRepository.save(CommentMapper.toComment(comment, item, author));
        return CommentMapper.toCommentDtoOut(newComment);
    }
}
