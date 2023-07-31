package ru.practicum.shareit.item.comment.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String text;
    @ManyToOne(fetch = FetchType.LAZY) // Many items can be associated with one user
    @JoinColumn(name = "item_id") // The column that links the User entity
    Item item;
    @ManyToOne(fetch = FetchType.LAZY) // Many items can be associated with one user
    @JoinColumn(name = "user_id") // The column that links the User entity
    User author;
    LocalDateTime created;
}
