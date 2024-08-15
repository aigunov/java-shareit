package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemCreate create;
    private ItemResponse response;
    private ItemUpdate update;
    private CommentCreate comment;
    private CommentResponse commentResponse;


    @Mock
    private ItemService service;

    @InjectMocks
    private ItemController controller;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        create = ItemCreate.builder()
                .name("item")
                .description("item description")
                .available(true)
                .build();

        response = ItemResponse.builder()
                .name("item")
                .description("item description")
                .available(true)
                .id(1L)
                .comments(List.of(
                        new CommentResponse(1L, "wow", "stupidIdiot227", LocalDateTime.now()),
                        new CommentResponse(2L, "that's shit doesn't work", "i love your mom", LocalDateTime.now())
                ))
                .build();

        update = ItemUpdate.builder()
                .name("item")
                .description("item description")
                .available(true)
                .id(1L)
                .build();

        comment = CommentCreate.builder()
                .itemId(1L)
                .authorId(1L)
                .text("big bruuuuuuuuh")
                .build();

        commentResponse = CommentResponse.builder()
                .id(1L)
                .text("wow")
                .authorName("hz")
                .build();
    }

    @Test
    void addItem_whenInvokeMethod_ReturnCreatedItem() throws Exception {
        when(service.createItem(any())).thenReturn(response);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsBytes(create))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.available", is(response.getAvailable())));

        verify(service, times(1)).createItem(any());

    }

    @Test
    void updateItem_whenInvokeMethod_ReturnUpdatedItem() throws Exception {
        when(service.updateItem(any())).thenReturn(response);
        long itemId = 1, userId = 1;

        mvc.perform(patch("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(update))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(update.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(update.getName())))
                .andExpect(jsonPath("$.description", is(update.getDescription())))
                .andExpect(jsonPath("$.available", is(update.getAvailable())));

        verify(service, times(1)).updateItem(any());
    }

    @Test
    void getItem_whenUserInvokeMethod_thenReturnFoundedItem() throws Exception {
        long itemId = 1;

        when(service.getItemById(anyLong())).thenReturn(response);

        mvc.perform(get("/items/" + itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.available", is(response.getAvailable())))
                .andExpect(jsonPath("$.name", is((response.getName()))))
                .andExpect(jsonPath("$.description", is(response.getDescription())));

        verify(service, times(1)).getItemById(anyLong());
    }

    @Test
    void getItem_whenItemOwnerInvokeMethod_thenReturnFoundedItem() throws Exception {
        long userId = 1L, itemId = 1L;

        when(service.getOwnerItemById(anyLong(), anyLong())).thenReturn(response);

        mvc.perform(get("/items/" + itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.available", is(response.getAvailable())))
                .andExpect(jsonPath("$.name", is((response.getName()))))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.comments", hasSize(response.getComments().size())))
                .andExpect(jsonPath("$.comments[0].text", is(response.getComments().get(0).getText())))
                .andExpect(jsonPath("$.comments[0].authorName", is(response.getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$.comments[0].id", is(response.getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.comments[1].text", is(response.getComments().get(1).getText())))
                .andExpect(jsonPath("$.comments[1].authorName", is(response.getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$.comments[1].id", is(response.getComments().get(1).getId()), Long.class));

        verify(service, times(1)).getOwnerItemById(anyLong(), anyLong());
    }

    @Test
    void getItemsOfUser_whenInvokeMethod_returnItemsOfUser() throws Exception {
        List<ItemResponse> itemResponses = List.of(
                new ItemResponse(null, null, null, 1, "fuck", "fuckx2", false)
        );

        long userId = 1L;

        when(service.getUserItems(userId)).thenReturn(itemResponses);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemResponses)))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(service, times(1)).getUserItems(anyLong());
    }

    @Test
    void searchItems_whenInvokeMethod_thenReturnFoundedItems() throws Exception {
        when(service.search(anyString())).thenReturn(List.of(response));

        String text = "bruh";

        mvc.perform(get("/items/search?text=" + text)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(response.getName())));

        verify(service, times(1)).search(anyString());
    }

    @Test
    void postComment_whenInvokeMethod_thenReturnCreatedComment() throws Exception {
        when(service.postComment(anyLong(), anyLong(), any())).thenReturn(commentResponse);

        long itemId = 1L, userId = 1L;

        mvc.perform(post("/items/" + itemId + "/comment")
                        .content(mapper.writeValueAsBytes(comment))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentResponse.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentResponse.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentResponse.getText())));

        verify(service, times(1)).postComment(anyLong(), anyLong(), any());
    }
}