package ru.practicum.shareit.item.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    private List<CommentResponse> comments;

    @Valid
    private BookingInfo nextBooking;

    @Valid
    private BookingInfo lastBooking;

    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    @Builder
    public record BookingInfo(@NotNull long id, @NotNull long bookerId) {
    }
}
