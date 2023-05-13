package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {
    long id;
    String name;
    String description;
    boolean available;
    UserDto owner;
    ItemRequest request;
}
