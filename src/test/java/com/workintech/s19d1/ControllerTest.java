package com.workintech.s19d1;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.s19d1.controller.ActorController;
import com.workintech.s19d1.dto.ActorRequest;
import com.workintech.s19d1.entity.Actor;
import com.workintech.s19d1.entity.Gender;
import com.workintech.s19d1.entity.Movie;
import com.workintech.s19d1.exceptions.ApiException;
import com.workintech.s19d1.service.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

/// /
@WebMvcTest(value = {ActorController.class})
@ExtendWith(ResultAnalyzer2.class)
class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorService actorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Actor actor;
    private ActorRequest actorRequest;

    @BeforeEach
    void setUp() {
        actor = new Actor();
        actor.setId(1L);
        actor.setFirstName("Sample");
        actor.setBirthDate(LocalDate.of(1990, 1, 1));
        actor.setGender(Gender.FEMALE);
        actor.setLastName("Actor");

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setName("Sample Movie");
        movie.setRating(5);
        movie.setDirectorName("Sample Director");

        actor.setMovies(Arrays.asList(movie));

        actorRequest = new ActorRequest();
        actorRequest.setActor(actor);
        actorRequest.setMovies(Arrays.asList(movie));
    }

    @Test
    @DisplayName("Find All Actors")
    void findAll() throws Exception {
        given(actorService.findAll()).willReturn(Arrays.asList(actor));

        mockMvc.perform(get("/actor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(actor.getId().intValue())))
                .andExpect(jsonPath("$[0].firstName", is(actor.getFirstName())));
    }

    @Test
    @DisplayName("Find Actor By Id")
    void findById() throws Exception {
        given(actorService.findById(actor.getId())).willReturn(actor);

        mockMvc.perform(get("/actor/{id}", actor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(actor.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(actor.getFirstName())));
    }

    @Test
    @DisplayName("Save Actor")
    void save() throws Exception {
        given(actorService.save(any(Actor.class))).willReturn(actor);

        mockMvc.perform(post("/actor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actor.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(actor.getFirstName())));
    }

    @Test
    @DisplayName("Update Actor")
    void update() throws Exception {
        given(actorService.findById(actor.getId())).willReturn(actor);
        given(actorService.save(any(Actor.class))).willReturn(actor);

        mockMvc.perform(put("/actor/{id}", actor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actor.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(actor.getFirstName())));
    }

    @Test
    @DisplayName("Delete Actor")
    void deleteActorTest() throws Exception {
        given(actorService.findById(actor.getId())).willReturn(actor);

        mockMvc.perform(delete("/actor/{id}", actor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actor.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(actor.getFirstName()))); // Assuming you have a getName method.

        // Optionally verify that the service method was called
        // verify(actorService).delete(actor);
    }


    @Test
    @DisplayName("Handle ApiException")
    void testHandleApiException() throws Exception {
        when(actorService.findById(1L)).thenThrow(new ApiException("API Exception occurred", HttpStatus.BAD_REQUEST));
        mockMvc.perform(get("/actor/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Handle General Exception")
    void testHandleGeneralException() throws Exception {
        when(actorService.findById(1L)).thenThrow(new RuntimeException("General Exception occurred"));
        mockMvc.perform(get("/actor/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("General Exception occurred"))
                .andExpect(jsonPath("$.status").value(500));
    }
}
