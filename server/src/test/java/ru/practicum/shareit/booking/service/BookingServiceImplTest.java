package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {
    BookingService bookingService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;

    @BeforeEach
    void setup() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository,
                itemRepository);
    }

    @Test
    void createTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(bookingRepository.save(any())).thenReturn(BookingMapper.toBooking(booking, user2, item, BookingStatus.WAITING));
        when(userRepository.findById(any())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        BookingResponseDto bookingDto = bookingService.create(booking, user2.getId());

        assertEquals(booking.getId(), bookingDto.getId());
    }

    @Test
    void bookingApprovingTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());


        when(bookingRepository.save(any())).thenReturn(BookingMapper.toBooking(booking, user2, item, BookingStatus.APPROVED));
        when(bookingRepository.findById(any())).thenReturn(Optional.of(BookingMapper.toBooking(booking, user2, item, BookingStatus.WAITING)));
        BookingResponseDto bookingDto = bookingService.bookingApproving(booking.getId(), user.getId(), true);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItemId(), bookingDto.getItem().getId());
        assertEquals(BookingStatus.APPROVED, bookingDto.getStatus());
    }

    @Test
    void getBookingByIdTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(bookingRepository.findById(any())).thenReturn(Optional.of(BookingMapper.toBooking(booking, user2, item, BookingStatus.WAITING)));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        BookingResponseDto bookingDto = bookingService.getBookingById(booking.getId(), user2.getId());

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItemId(), bookingDto.getItem().getId());
    }

    @Test
    void getBookingListTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item, user2, BookingStatus.WAITING);
        when(userRepository.existsById(anyLong())).thenReturn(true);

        when(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(), any(), any(),
                any())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingResponseDto> bookingList = bookingService.getBookingList(user2.getId(), "CURRENT", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList.get(0).getId());

        List<BookingResponseDto> bookingList1 = bookingService.getBookingList(user2.getId(), "PAST", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList1.get(0).getId());
        assertEquals(booking.getStart(), bookingList1.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList1.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList1.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList1.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList2 = bookingService.getBookingList(user2.getId(), "FUTURE", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList2.get(0).getId());
        assertEquals(booking.getStart(), bookingList2.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList2.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList2.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList2.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList3 = bookingService.getBookingList(user2.getId(), "WAITING", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList3.get(0).getId());
        assertEquals(booking.getStart(), bookingList3.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList3.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList3.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList3.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList4 = bookingService.getBookingList(user2.getId(), "REJECTED", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList4.get(0).getId());
        assertEquals(booking.getStart(), bookingList4.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList4.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList4.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList4.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList5 = bookingService.getBookingList(user2.getId(), "ALL", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList5.get(0).getId());
        assertEquals(booking.getStart(), bookingList5.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList5.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList5.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList5.get(0).getBooker().getId());
    }

    @Test
    void getOwnerBookingListTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item, user2, BookingStatus.WAITING);
        when(userRepository.existsById(anyLong())).thenReturn(true);

        when(bookingRepository.findAllItemOwnerCurrentBookings(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllItemOwnerPastBookings(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllItemOwnerFutureBookings(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllItemOwnerBookingsByStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllItemOwnerBookingsByStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllItemOwnerBookings(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingResponseDto> bookingList = bookingService.getOwnerBookingList(user2.getId(), "CURRENT", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList.get(0).getId());

        List<BookingResponseDto> bookingList1 = bookingService.getOwnerBookingList(user2.getId(), "PAST", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList1.get(0).getId());
        assertEquals(booking.getStart(), bookingList1.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList1.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList1.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList1.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList2 = bookingService.getOwnerBookingList(user2.getId(), "FUTURE", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList2.get(0).getId());
        assertEquals(booking.getStart(), bookingList2.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList2.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList2.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList2.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList3 = bookingService.getOwnerBookingList(user2.getId(), "WAITING", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList3.get(0).getId());
        assertEquals(booking.getStart(), bookingList3.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList3.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList3.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList3.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList4 = bookingService.getOwnerBookingList(user2.getId(), "REJECTED", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList4.get(0).getId());
        assertEquals(booking.getStart(), bookingList4.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList4.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList4.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList4.get(0).getBooker().getId());
        List<BookingResponseDto> bookingList5 = bookingService.getOwnerBookingList(user2.getId(), "ALL", Pageable.unpaged());
        assertEquals(booking.getId(), bookingList5.get(0).getId());
        assertEquals(booking.getStart(), bookingList5.get(0).getStart());
        assertEquals(booking.getEnd(), bookingList5.get(0).getEnd());
        assertEquals(booking.getItem().getId(), bookingList5.get(0).getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingList5.get(0).getBooker().getId());
    }

    @Test
    void createWrongUserTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.create(booking, -1));
        assertEquals("Пользователь с таким id не найден!", e.getMessage());
    }

    @Test
    void createWrongItemTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.create(booking, 2L));
        assertEquals("Предмет с таким id не найден!", e.getMessage());
    }

    @Test
    void createWrongBookerTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.create(booking, 1L));
        assertEquals("Нельзя арендовать свой предмет!", e.getMessage());
    }

    @Test
    void createNotAvailableTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", false, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(userRepository.findById(any())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        ValidationException e = assertThrows(ValidationException.class, () -> bookingService.create(booking, 2L));
        assertEquals("Нельзя арендовать!", e.getMessage());
    }

    @Test
    void createWrongTimeTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1), item.getId());
        when(userRepository.findById(any())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        ValidationException e = assertThrows(ValidationException.class, () -> bookingService.create(booking, 2L));
        assertEquals("Время начала пожже времени конца!!", e.getMessage());
    }

    @Test
    void bookingApprovingWrongBookingTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());

        when(bookingRepository.findById(any())).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.bookingApproving(
                booking.getId(), 2L, true));
        assertEquals("Букинг с таким id не найден!", e.getMessage());
    }

    @Test
    void bookingApprovingWrongStatusTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());

        when(bookingRepository.findById(any())).thenReturn(Optional.of(BookingMapper.toBooking(booking, user2, item,
                BookingStatus.APPROVED)));

        ValidationException e = assertThrows(ValidationException.class, () -> bookingService.bookingApproving(
                booking.getId(), 2L, true));
        assertEquals("Нет необходимости менять статус!", e.getMessage());
    }

    @Test
    void bookingApprovingWrongBookerTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user2 = new User(2L, "name2", "email2@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());

        when(bookingRepository.findById(any())).thenReturn(Optional.of(BookingMapper.toBooking(booking, user2, item,
                BookingStatus.WAITING)));

        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.bookingApproving(
                booking.getId(), 2L, true));
        assertEquals("Нет доступа к изменению букинга!", e.getMessage());
    }

    @Test
    void getBookingByIdWrongUserTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(userRepository.existsById(anyLong())).thenReturn(false);


        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(),
                2L));
        assertEquals("Пользователь с таким id не найден!", e.getMessage());

    }

    @Test
    void getBookingByIdWrongBookingTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(),
                2L));
        assertEquals("Букинг с таким id не найден!", e.getMessage());

    }

    @Test
    void getBookingByIdWrongBookerTest() {
        User user = new User(1L, "name1", "email1@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user, null);

        BookingRequestDto booking = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(BookingMapper.toBooking(booking, user, item,
                BookingStatus.WAITING)));
        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(),
                2L));
        assertEquals("Нет доступа к букингу!", e.getMessage());
    }

    @Test
    void getBookingListWrongUserTest() {
        User user = new User(1L, "name1", "email1@mail");
        when(userRepository.existsById(anyLong())).thenReturn(false);
        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.getBookingList(user.getId(),
                "ALL", Pageable.unpaged()));
        assertEquals("Пользователь с таким id не найден!", e.getMessage());
    }

    @Test
    void getBookingListWrongStateTest() {
        User user = new User(1L, "name1", "email1@mail");
        ValidationException e = assertThrows(ValidationException.class, () -> bookingService.getBookingList(user.getId(),
                "WRONGSTATE", Pageable.unpaged()));
        assertEquals("Unknown state: WRONGSTATE", e.getMessage());
    }

    @Test
    void getOwnerBookingListWrongUserTest() {
        User user = new User(1L, "name1", "email1@mail");
        when(userRepository.existsById(anyLong())).thenReturn(false);
        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.getOwnerBookingList(user.getId(),
                "ALL", Pageable.unpaged()));
        assertEquals("Пользователь с таким id не найден!", e.getMessage());
    }

    @Test
    void getOwnerBookingListWrongStateTest() {
        User user = new User(1L, "name1", "email1@mail");
        ValidationException e = assertThrows(ValidationException.class, () -> bookingService.getOwnerBookingList(user.getId(),
                "WRONGSTATE", Pageable.unpaged()));
        assertEquals("Unknown state: WRONGSTATE", e.getMessage());
    }
}