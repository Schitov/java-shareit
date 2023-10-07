package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotEmpty
    String name;
    @NotEmpty
    String description;
    @NotNull
    Boolean available;
    @ManyToOne(fetch = FetchType.LAZY) // Many items can be associated with one user
    @JoinColumn(name = "owner_id") // The column that links the User entity
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    User owner;

    @Column(name = "request_id") // The column that links the ItemRequest entity
    Integer request;

    @OneToMany
    @JoinColumn(name = "item_id")
    public List<Comment> comments;

    @OneToMany(mappedBy = "item")
    private List<Booking> bookings;

}
