package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreate;


/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public final class RequestController {
    private final RequestClient requestClient;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createRequest(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId,
                                                @RequestBody @Valid final RequestCreate requestCreate) {
        log.info("Create request: {}", requestCreate);
        return requestClient.createRequest(userId, requestCreate);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserRequests(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId) {
        log.info("Get user: {} requests", userId);
        return requestClient.getUserRequests(userId);
    }


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") final long userId,
                                                 @Min(0) @RequestParam(required = true) final int from,
                                                 @Min(1) @RequestParam(required = true) final int size) {
        log.info("Get all requests: {}", from);
        int page = from / size + 1;
        return requestClient.getAllRequests(userId, from, size);
    }


    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequest(@RequestHeader(value = "X-Sharer-User-Id", required = true) final long userId,
                                             @PathVariable final long requestId) {
        log.info("Get request by id: {}", requestId);
        return requestClient.getRequestById(requestId, userId);
    }
}
