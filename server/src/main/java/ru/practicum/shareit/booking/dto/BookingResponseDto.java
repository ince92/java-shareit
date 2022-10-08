package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private BookingStatus status;
    private User booker;
}
