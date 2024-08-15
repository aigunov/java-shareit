package ru.practicum.shareit.item.dto;

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

    private BookingInfo nextBooking;

    private BookingInfo lastBooking;

    private long id;

    private String name;

    private String description;

    private Boolean available;

    @Builder
    public record BookingInfo(long id, long bookerId) {
    }
}
