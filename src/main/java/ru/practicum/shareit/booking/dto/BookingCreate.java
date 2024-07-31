package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreate {

    @NotNull
    @Min(1)
    private Long itemId;

    private Long bookerId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    @AssertTrue
    public boolean isValid() {
        return end.isAfter(start) && end.isAfter(LocalDateTime.now()) && !end.isEqual(start);
    }
}
