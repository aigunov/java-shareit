package ru.practicum.shareit.item.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class ItemResponse {

    List<CommentResponse> comments;

    @Valid
    BookingInfo nextBooking;

    @Valid
    BookingInfo lastBooking;

    private long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private Boolean available;

    @Builder
    public record BookingInfo(@NotNull long id, @NotNull long bookerId) {
    }
}
