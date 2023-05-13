package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class User {
    long id;
    @NotBlank
    String name;
    @Email
    @NotNull
    String email;

}
