package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.Create;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ItemRequestDto {
    private long id;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotBlank(groups = {Create.class})
    private Long requesterId;
    @FutureOrPresent
    private LocalDateTime created;
}
