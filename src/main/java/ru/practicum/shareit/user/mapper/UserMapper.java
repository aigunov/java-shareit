package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;

public final class UserMapper {
    /**
     * User -> UserDtoToCreate
     */
    public static UserDtoResponse toUserDto(final User user) {
        return UserDtoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * UserDto -> UserDtoToCreate
     */
    public static User toUser(final UserDtoCreate userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
