package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemDtoWithBookingDatesTest {
    @Autowired
    private JacksonTester<ItemDtoWithBookingDates> json;

    @Test
    void testItemDto() throws Exception {
        ItemDtoWithBookingDates dto = new ItemDtoWithBookingDates(
                1L,
                "name",
                "description",
                true,
                2L,
                new ItemDtoWithBookingDates.BookingResponseDtoLess(1L, 3L),
                new ItemDtoWithBookingDates.BookingResponseDtoLess(2L, 5L),
                List.of(new CommentResponseDto(1L, "text", "AuthorName", LocalDateTime.now().minusDays(1)))
        );

        JsonContent<ItemDtoWithBookingDates> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments.size()").isEqualTo(1);
    }

}