package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = false)
    public RequestResponse createRequest(final RequestCreate requestCreate) {
        User user = userRepository.findById(requestCreate.getRequesterId())
                .orElseThrow(() -> new NoSuchElementException("Requester not found"));
        requestCreate.setRequester(user);
        Request request = requestRepository.save(RequestMapper.toRequest(requestCreate));
        log.info("Created request: {}", request);
        return RequestMapper.toRequestResponse(request);
    }

    @Override
    public List<RequestResponse> getUserRequests(final long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Requester not found"));
        List<RequestResponse> requests = requestRepository.findAllById(userId)
                .stream().map(RequestMapper::toRequestResponse).toList();
        log.info("Found {} requests: {}", requests.size(), requests);
        return requests;
    }


    @Override
    public RequestResponse getRequestById(final long requestId, final long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Requester not found"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        List<Item> items = itemRepository.getItemByRequestId(request.getId());
        RequestResponse requestResponse = RequestMapper.toRequestResponse(request, items);
        log.info("Found request: {}", requestResponse);
        return requestResponse;
    }

    @Override
    public List<RequestResponse> getAllRequests(final PageRequest pageRequest) {
        List<RequestResponse> requests = requestRepository.findAll(pageRequest).getContent()
                .stream()
                .map(RequestMapper::toRequestResponse)
                .toList();
        log.info("Found {} all requests: {}", requests.size(), requests);
        return requests;
    }
}


