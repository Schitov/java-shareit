package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "start_time")
    LocalDateTime start;
    @Column(name = "end_time")
    LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    User booker;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    Status status;
}