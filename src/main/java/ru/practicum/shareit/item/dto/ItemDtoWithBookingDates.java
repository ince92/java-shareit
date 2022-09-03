package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithBookingDates {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private BookingResponseDtoLess lastBooking;
    private BookingResponseDtoLess nextBooking;
    private List<CommentResponseDto> comments;

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
    public static class BookingResponseDtoLess {
        private Long id;
        private Long bookerId;
    }


}
