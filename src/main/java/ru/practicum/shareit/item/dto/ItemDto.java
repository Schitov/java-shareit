package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class ItemDto {
    long id;
    @NotEmpty
    String name;
    @NotEmpty
    String description;
    @NotNull
    Boolean available;
    UserDto owner;
    ItemRequest request;
}
