package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class User {
    @Id
    long id;
    @NotBlank
    String name;
    @Email
    @NotNull
    String email;

}
