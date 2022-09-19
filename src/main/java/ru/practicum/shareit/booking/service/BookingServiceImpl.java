package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponseDto create(BookingRequestDto bookingRequestDto, long bookerId) {
        User booker = userRepository.findById(bookerId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Предмет с таким id не найден!"));

        if (item.getOwner().getId() == bookerId) {
            throw new NotFoundException("Нельзя арендовать свой предмет!");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Нельзя арендовать!");
        }
        Booking booking = BookingMapper.toBooking(bookingRequestDto, booker, item, BookingStatus.WAITING);
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("Время начала пожже времени конца!!");

        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto bookingApproving(long bookingId, long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Букинг с таким id не найден!"));

        if ((booking.getStatus() == BookingStatus.APPROVED && approved) ||
                (booking.getStatus() == BookingStatus.REJECTED && !approved)) {
            throw new ValidationException("Нет необходимости менять статус!");
        }

        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();
        if (userId == bookerId) {
            if (approved) {
                throw new NotFoundException("Нет доступа к изменению букинга!");
            }
        } else if (ownerId != userId) {
            throw new NotFoundException("Нет доступа к изменению букинга!");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBookingById(long bookingId, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден!");
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Букинг с таким id не найден!"));
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();
        if (userId != bookerId && ownerId != userId) {
            throw new NotFoundException("Нет доступа к букингу!");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingList(long userId, String requestState, Pageable pageRequest) {
        BookingState state;
        try {
            state = BookingState.valueOf(requestState);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + requestState);
        }

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден!");
        }
        switch (state) {
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now(),pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                                LocalDateTime.now(), pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                                BookingStatus.WAITING, pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                                BookingStatus.REJECTED, pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());
        }
    }

     @Override
    public List<BookingResponseDto> getOwnerBookingList(long userId, String requestState, Pageable pageRequest) {
        BookingState state;
        try {
            state = BookingState.valueOf(requestState);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + requestState);
        }

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден!");
        }

        switch (state) {
            case CURRENT:
                return bookingRepository.findAllItemOwnerCurrentBookings(userId,
                                LocalDateTime.now(), LocalDateTime.now(), pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllItemOwnerPastBookings(userId,
                                LocalDateTime.now(), pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findAllItemOwnerFutureBookings(userId,
                                LocalDateTime.now(), pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case WAITING:
                return bookingRepository.findAllItemOwnerBookingsByStatus(userId,
                                BookingStatus.WAITING, pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());

            case REJECTED:
                return bookingRepository.findAllItemOwnerBookingsByStatus(userId,
                                BookingStatus.REJECTED, pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default:

                return bookingRepository.findAllItemOwnerBookings(userId, pageRequest).stream()
                        .map(BookingMapper::toBookingDto).collect(Collectors.toList());
        }
    }
}
