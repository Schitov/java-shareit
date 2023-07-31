package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserDto {
    long id;
    @NotBlank
    String name;
    @Email
    @NotNull
    String email;

}
