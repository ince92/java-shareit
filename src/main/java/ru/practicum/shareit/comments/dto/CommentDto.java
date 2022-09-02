package ru.practicum.shareit.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String text;
    private Long itemId;
    private Long authorId;
    private LocalDateTime created;
}
