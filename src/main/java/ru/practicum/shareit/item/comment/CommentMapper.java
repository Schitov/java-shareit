package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        UserDto authorDto = UserMapper.userToDto(comment.getAuthor());
        ItemDto itemDto = ItemMapper.toItemDto(comment.getItem());

        return CommentDto.builder()
                .text(comment.getText())
                .created(comment.getCreated())
                .author(authorDto)
                .id(comment.getId())
                .item(itemDto)
                .build();
    }

    public static Comment toComment(CommentDto commentDto) {
        User author = UserMapper.dtoToUser(commentDto.getAuthor());
        Item item = ItemMapper.toItem(commentDto.getItem());

        return Comment.builder()
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .author(author)
                .id(commentDto.getId())
                .item(item)
                .build();
    }

}
