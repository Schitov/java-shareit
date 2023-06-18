package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@RequiredArgsConstructor
public class ItemRequest {
    int id;
    String description;
    User requestor;
    LocalDateTime created;
}
