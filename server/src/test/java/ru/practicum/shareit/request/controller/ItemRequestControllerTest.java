package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    User owner = new User(1L, "owner", "owner@mail");
    private final ItemRequest request = new ItemRequest(1L, "text", owner, LocalDateTime.now());

    private final ItemDto itemDto = new ItemDto(1L, "item", "description", true,
            request.getId());

    private final ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(request);

    private final ItemRequestDtoResponse requestDtoResponse = ItemRequestMapper.toItemRequestDtoResponse(request,
            List.of(itemDto));

    @Test
    void createTest() throws Exception {
        when(itemRequestService.create(any())).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(requestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class));
    }

    @Test
    void findItemRequestsByRequestorTest() throws Exception {
        when(itemRequestService.findItemRequestsByRequester(anyLong())).thenReturn(List.of(requestDtoResponse));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(requestDtoResponse.getDescription()), String.class))
                .andExpect(jsonPath("$[0].id", is(requestDtoResponse.getId()), Long.class));
    }

    @Test
    void findItemRequests() throws Exception {
        when(itemRequestService.findItemRequests(anyLong(), any())).thenReturn(List.of(requestDtoResponse));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(requestDtoResponse.getDescription()), String.class))
                .andExpect(jsonPath("$[0].id", is(requestDtoResponse.getId()), Long.class));
    }

    @Test
    void findItemRequestsById() throws Exception {
        when(itemRequestService.findItemRequestsById(anyLong(), anyLong())).thenReturn(requestDtoResponse);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(requestDtoResponse.getDescription()), String.class))
                .andExpect(jsonPath("$.id", is(requestDtoResponse.getId()), Long.class));
    }
}