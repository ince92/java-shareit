package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingResponseDtoTest {
    @Autowired
    private JacksonTester<BookingResponseDto> json;

    @Test
    void testBookingDto() throws Exception {
        User user1 = new User(1L, "name1", "email1@mail");
        User booker = new User(1L, "booker", "booker@mail");
        Item item = new Item(1L, "itemname", "itemdescription", true, user1, null);
        BookingResponseDto bookingDto = new BookingResponseDto(
                1L,
                LocalDateTime.of(2022,9,10,11,12,13).plusDays(1),
                LocalDateTime.of(2022,9,10,11,12,13).plusDays(2),
                item,
                BookingStatus.WAITING,
                booker
        );

        JsonContent<BookingResponseDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
    }
}
