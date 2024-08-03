package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.model.User;

public final class UserMapper {
    /**
     * User -> UserResponse
     */
    public static UserResponse toUserResponse(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * UserCreate -> User
     */
    public static User toUser(final UserCreate userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * UserUpdate -> User
     */
    public static User toUser(final UserUpdate userUpdate, final long userId) {
        return User.builder()
                .id(userId)
                .name(userUpdate.getName())
                .email(userUpdate.getEmail())
                .build();
    }

    /**
     * User <- UserUpdate
     */
    public static User updateUserFromDto(final User user, final UserUpdate userUpdate) {
        user.setName(userUpdate.getName() != null && !userUpdate.getName().isEmpty() ?
                userUpdate.getName() : user.getName());
        user.setEmail(userUpdate.getEmail() != null && !userUpdate.getEmail().isEmpty() ?
                userUpdate.getEmail() : user.getEmail());
        return user;
    }
}
