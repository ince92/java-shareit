package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {

    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void setup() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(userRepository, itemRepository, bookingRepository, commentRepository,
                itemRequestRepository);
    }

    @Test
    void updateTest() {
        User user = new User(1L, "name1", "email1@mail");
        ItemDto item = new ItemDto(1L, "Updateditemname", null, null, null);
        Item updatedItem = new Item(1L, "itemname", "description", true, user, null);
        when(itemRepository.save(any())).thenReturn(ItemMapper.toItem(item, user, null));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(updatedItem));
        ItemDto itemRequest = itemService.update(item, item.getId(), user.getId());

        assertEquals(updatedItem.getName(), itemRequest.getName());
        assertEquals(updatedItem.getId(), itemRequest.getId());
        assertEquals(updatedItem.getAvailable(), itemRequest.getAvailable());
        assertEquals(updatedItem.getDescription(), itemRequest.getDescription());
    }

    @Test
    void createTest() {
        User user = new User(1L, "name1", "email1@mail");
        ItemDto item = new ItemDto(1L, "itemname", "description", true, null);
        when(itemRepository.save(any())).thenReturn(ItemMapper.toItem(item, user, null));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ItemDto itemRequest = itemService.create(item, user.getId());

        assertEquals(item.getId(), itemRequest.getId());
        assertEquals(item.getName(), itemRequest.getName());
        assertEquals(item.getAvailable(), itemRequest.getAvailable());
        assertEquals(item.getDescription(), itemRequest.getDescription());
    }

    @Test
    void findItemByIdTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemname", true, user, null);

        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemId(any())).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItemId(any())).thenReturn(new ArrayList<>());

        ItemDtoWithBookingDates itemDto = itemService.findItemById(item.getId(), user.getId());

        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(item.getDescription(), itemDto.getDescription());
    }

    @Test
    void findOwnersItemsTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        when(itemRepository.findOwnersItems(anyLong(), any())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemId(any())).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItemId(any())).thenReturn(new ArrayList<>());

        List<ItemDtoWithBookingDates> itemList = itemService.findOwnersItems(user.getId(), Pageable.ofSize(10));

        assertEquals(1, itemList.size());
        assertEquals(item.getId(), itemList.get(0).getId());
        assertEquals(item.getName(), itemList.get(0).getName());
        assertEquals(item.getAvailable(), itemList.get(0).getAvailable());
        assertEquals(item.getDescription(), itemList.get(0).getDescription());
    }

    @Test
    void findAvailableItems() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        when(itemRepository.findAvailableItems(anyString(), any())).thenReturn(Collections.singletonList(item));
        List<ItemDto> itemList = itemService.findAvailableItems("itemname", Pageable.ofSize(10));

        assertEquals(1, itemList.size());
        assertEquals(item.getId(), itemList.get(0).getId());
        assertEquals(item.getName(), itemList.get(0).getName());
        assertEquals(item.getAvailable(), itemList.get(0).getAvailable());
        assertEquals(item.getDescription(), itemList.get(0).getDescription());
    }

    @Test
    void lastNextBookingTest() {
        User user1 = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        User user3 = new User(3L, "name3", "email3@mail");
        User user4 = new User(4L, "name4", "email4@mail");

        Item item = new Item(1L, "itemname", "itemdescription", true, user1, null);

        Booking booking1 = new Booking(1L, LocalDateTime.of(2022, 1, 1, 1, 1),
                LocalDateTime.of(2022, 1, 2, 1, 1), item, user2, BookingStatus.APPROVED);
        Booking booking2 = new Booking(2L, LocalDateTime.of(2022, 2, 1, 1, 1),
                LocalDateTime.of(2022, 2, 2, 1, 1), item, user3, BookingStatus.APPROVED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item, user4, BookingStatus.APPROVED);

        when(itemRepository.findOwnersItems(anyLong(), any())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemId(any())).thenReturn(List.of(booking1, booking2, booking3));
        when(commentRepository.findAllByItemId(any())).thenReturn(new ArrayList<>());

        List<ItemDtoWithBookingDates> itemList = itemService.findOwnersItems(user1.getId(), Pageable.ofSize(10));

        assertEquals(1, itemList.size());
        assertEquals(item.getId(), itemList.get(0).getId());
        assertEquals(item.getName(), itemList.get(0).getName());
        assertEquals(item.getAvailable(), itemList.get(0).getAvailable());
        assertEquals(item.getDescription(), itemList.get(0).getDescription());
        assertEquals(booking3.getId(), itemList.get(0).getNextBooking().getId());
        assertEquals(booking2.getId(), itemList.get(0).getLastBooking().getId());
    }

    @Test
    void addCommentTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "description", true, user, null);
        Booking booking1 = new Booking(1L, LocalDateTime.of(2022, 1, 1, 1, 1),
                LocalDateTime.of(2022, 1, 2, 1, 1), item, user2, BookingStatus.APPROVED);
        CommentRequestDto commentDto = new CommentRequestDto(1L, "text", item.getId(), user2.getId(), LocalDateTime.now());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllUserBookings(anyLong(), anyLong(), any())).thenReturn(List.of(booking1));
        when(commentRepository.save(any())).thenReturn(CommentMapper.toComment(commentDto, item, user2));

        CommentResponseDto newComment = itemService.addComment(commentDto);

        assertEquals(newComment.getId(), commentDto.getId());
        assertEquals(newComment.getText(), commentDto.getText());
    }

    @Test
    void createWrongRequestTest() {
        User user = new User(1L, "name1", "email1@mail");
        ItemDto item = new ItemDto(1L, "itemname", "description", true, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.create(item, 1L));
        assertEquals("Запрос с таким id не найден!", e.getMessage());
    }

    @Test
    void createWrongUserTest() {
        ItemDto item = new ItemDto(1L, "itemname", "description", true, 1L);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.create(item, 1L));
        assertEquals("Пользователь с таким id не найден!", e.getMessage());
    }

    @Test
    void updateWrongUserTest() {
        ItemDto item = new ItemDto(1L, "Updateditemname", null, null, null);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.update(item, 1L, 1L));
        assertEquals("Пользователь с таким id не найден!", e.getMessage());
    }

    @Test
    void updateWrongItemTest() {
        User user = new User(1L, "name1", "email1@mail");
        ItemDto item = new ItemDto(1L, "Updateditemname", null, null, null);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.update(item, 1L, 1L));
        assertEquals("Предмет с таким id не найден!", e.getMessage());
    }

    @Test
    void updateWrongOwnerTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        ItemDto item = new ItemDto(1L, "Updateditemname", null, null, null);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(ItemMapper.toItem(item, user, null)));
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.update(item, 1L, user2.getId()));
        assertEquals("Пользователь не может обновить чужой предмет!", e.getMessage());
    }

    @Test
    void findItemByIdWrongIdTest() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.findItemById(1L, 1L));
        assertEquals("Предмет с таким id не найден!", e.getMessage());
    }

    @Test
    void addCommentWrongUserTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "description", true, user, null);
        CommentRequestDto commentDto = new CommentRequestDto(1L, "text", item.getId(), user2.getId(), LocalDateTime.now());

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.addComment(commentDto));
        assertEquals("Пользователь с таким id не найден!", e.getMessage());
    }

    @Test
    void addCommentWrongItemTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "description", true, user, null);
        CommentRequestDto commentDto = new CommentRequestDto(1L, "text", item.getId(), user2.getId(), LocalDateTime.now());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.addComment(commentDto));
        assertEquals("Предмет с таким id не найден!", e.getMessage());
    }

    @Test
    void addCommentWrongBookingTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "description", true, user, null);
        CommentRequestDto commentDto = new CommentRequestDto(1L, "text", item.getId(), user2.getId(), LocalDateTime.now());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllUserBookings(anyLong(), anyLong(), any())).thenReturn(new ArrayList<>());

        ValidationException e = assertThrows(ValidationException.class, () -> itemService.addComment(commentDto));
        assertEquals("Нет доступа к созданию отзыва!", e.getMessage());
    }


}