package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {
    long id;
    @NotEmpty
    String name;
    @NotEmpty
    String description;
    @NotNull
    Boolean available;
    User owner;
    ItemRequest request;
}
