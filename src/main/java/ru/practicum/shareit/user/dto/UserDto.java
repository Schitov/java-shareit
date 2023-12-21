package ru.practicum.shareit.user.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
