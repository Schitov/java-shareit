package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    private static BookingCreateDto last = null;
    private static BookingCreateDto next = null;

    public static CommentDto toCommentDto(Comment comment) {
        UserDto userDto = UserMapper.userToDto(comment.getAuthor());
        ItemDto itemDto = ItemMapper.toItemDto(comment.getItem());

        return CommentDto.builder()
                .created(comment.getCreated())
                .text(comment.getText())
                .id(comment.getId())
                .item(itemDto)
                .authorId(userDto.getId())
                .authorName(userDto.getName())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User author, Item item) {
        return Comment.builder()
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .id(commentDto.getId())
                .author(author)
                .item(item)
                .build();
    }

    public static Comment toNewComment(CommentDto commentDto, User user, Item item) {
        return Comment.builder()
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .id(commentDto.getId())
                .text(commentDto.getText())
                .build();

    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .name(item.getName())
                .requestId(item.getRequest())
                .description(item.getDescription())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .request(itemDto.getRequestId())
                .description(itemDto.getDescription())
                .build();
    }

    public static ItemOwnerDto toItemDtoOwner(Item item) {
        List<CommentDto> comments = item.getComments()
                .stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());

        return ItemOwnerDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .name(item.getName())
                .comments(comments)
                .requestId(item.getRequest())
                .description(item.getDescription())
                .build();
    }

    public static ItemOwnerDto toItemDtoOwner(Item item, List<BookingCreateDto> bookings) {
        List<CommentDto> comments = item.getComments()
                .stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());

        assignLastAndFirstDate(bookings);

        return ItemOwnerDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .name(item.getName())
                .requestId(item.getRequest())
                .comments(comments)
                .description(item.getDescription())
                .nextBooking(next)
                .lastBooking(last)
                .build();
    }

    private static void assignLastAndFirstDate(List<BookingCreateDto> bookings) {
        last = null;
        next = null;

        if (bookings == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        for (BookingCreateDto booking : bookings) {
            if (booking.getStart().isBefore(now)) {
                if (last == null || booking.getEnd().isAfter(last.getEnd())) {
                    last = booking;
                }
            } else if (booking.getStart().isAfter(now) &&
                    (next == null || booking.getStart().isBefore(next.getStart()))) {
                next = booking;
            }
        }
    }


}
