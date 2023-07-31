package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@RequiredArgsConstructor
public class ItemRequest {
    int id;
    String description;
    @ManyToOne(fetch = FetchType.LAZY) // Many items can be associated with one user
    @JoinColumn(name = "user_id") // The column that links the User entity
    User requestor;
    LocalDateTime created;
}
