package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ItemResponse {
    List<CommentResponse> comments;
    BookingInfo nextBooking;
    BookingInfo lastBooking;
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;

    @Builder
    public record BookingInfo(long id, long bookerId) {
    }
}
