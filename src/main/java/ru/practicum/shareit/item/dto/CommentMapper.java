package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {

    public static CommentResponseDto toCommentDtoOut(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentRequestDto comment, Item item, User author) {
        return new Comment(
                comment.getId(),
                comment.getText(),
                item,
                author,
                comment.getCreated()
        );
    }

}
