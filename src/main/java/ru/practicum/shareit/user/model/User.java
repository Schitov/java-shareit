package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class User {
    long id;
    @NotBlank
    String name;
    @Email
    @NotNull
    String email;

}
