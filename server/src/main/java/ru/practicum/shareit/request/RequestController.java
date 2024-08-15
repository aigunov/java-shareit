package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public final class RequestController {
    private final RequestService requestService;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public RequestResponse createRequest(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId,
                                         @RequestBody final RequestCreate requestCreate) {
        requestCreate.setRequesterId(userId);
        log.info("Create request: {}", requestCreate);
        return requestService.createRequest(requestCreate);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponse> getUserRequests(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId) {
        log.info("Get user: {} requests", userId);
        return requestService.getUserRequests(userId);
    }


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponse> getAllRequests(@RequestParam(required = true) final int from,
                                                @RequestParam(required = true) final int size) {
        log.info("Get all requests: {}", from);
        int page = from / size + 1;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "created"));
        return requestService.getAllRequests(PageRequest
                .of(page, size, Sort.by(Sort.Direction.ASC, "created")));
    }


    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponse getRequest(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId,
                                      @PathVariable final long requestId) {
        log.info("Get request by id: {}", requestId);
        return requestService.getRequestById(requestId, userId);
    }
}
