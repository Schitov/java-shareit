package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

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
