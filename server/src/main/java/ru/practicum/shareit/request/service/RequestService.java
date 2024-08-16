package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;

import java.util.List;

public interface RequestService {
    RequestResponse createRequest(RequestCreate requestCreate);

    List<RequestResponse> getUserRequests(long userId);

    RequestResponse getRequestById(long requestId, long userId);

    List<RequestResponse> getAllRequests(PageRequest of);
}
