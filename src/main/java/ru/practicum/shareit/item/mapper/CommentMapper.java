package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentCreate;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    /**
     * CommentCreate -> Comment
     */
    public static Comment toComment(CommentCreate commentCreate, User author, Item item) {
        return Comment.builder()
                .text(commentCreate.getText())
                .author(author)
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }


    /**
     * Comment -> CommentResponse
     */
    public static CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .build();
    }
}
