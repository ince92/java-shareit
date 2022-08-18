package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {
    private long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Email(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String email;
}
