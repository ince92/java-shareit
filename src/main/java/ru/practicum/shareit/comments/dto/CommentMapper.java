package ru.practicum.shareit.comments.dto;

import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {

    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        return new CommentDtoOut(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto comment, Item item, User author) {
        return new Comment(
                comment.getId(),
                comment.getText(),
                item,
                author,
                comment.getCreated()
        );
    }

}
