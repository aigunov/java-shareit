package ru.practicum.shareit.request.mapper;


import io.micrometer.common.lang.NonNull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public final class RequestMapper {

    /**
     * RequestCreate -> Request
     */
    @NonNull
    public static Request toRequest(RequestCreate request) {
        return Request.builder()
                .description(request.getDescription())
                .created(request.getCreated())
                .requestor(request.getRequester())
                .build();
    }

    public static RequestResponse toRequestResponse(Request request) {
        return RequestResponse.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    public static RequestResponse toRequestResponse(Request request, List<Item> items) {
        return RequestResponse.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(items.stream().map(RequestMapper::toResponseOnRequest).toList())
                .build();
    }

    public static RequestResponse.ResponseOnRequest toResponseOnRequest(Item item) {
        return RequestResponse.ResponseOnRequest.builder()
                .itemId(item.getId())
                .name(item.getName())
                .userId(item.getOwner().getId()).build();
    }
}
