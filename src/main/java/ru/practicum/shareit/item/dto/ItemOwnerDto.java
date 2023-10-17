package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
public class ItemOwnerDto {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private Boolean available;
    private long ownerId;
    private Integer requestId;
    private BookingCreateDto lastBooking;
    private BookingCreateDto nextBooking;
    private List<CommentDto> comments;
}
