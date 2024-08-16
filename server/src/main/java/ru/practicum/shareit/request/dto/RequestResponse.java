package ru.practicum.shareit.request.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor()
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class RequestResponse {
    private long id;
    private String description;
    @CreationTimestamp
    private LocalDateTime created;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ResponseOnRequest> items;

    @Builder(toBuilder = true)
    public record ResponseOnRequest(long itemId, String name, long userId) {
    }

}
