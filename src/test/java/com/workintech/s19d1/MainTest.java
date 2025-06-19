package com.workintech.s19d1;

import com.workintech.s19d1.entity.Actor;
import com.workintech.s19d1.entity.Gender;
import com.workintech.s19d1.entity.Movie;
import com.workintech.s19d1.exceptions.ApiException;
import com.workintech.s19d1.exceptions.ExceptionResponse;
import com.workintech.s19d1.repository.ActorRepository;
import com.workintech.s19d1.repository.MovieRepository;
import com.workintech.s19d1.service.ActorServiceImpl;
import com.workintech.s19d1.service.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/// //
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@ExtendWith(ResultAnalyzer.class)
class MainTest {

    @Mock
    private ActorRepository mockActorRepository;

    @Mock
    private MovieRepository mockMovieRepository;

    private Actor actor;
    private Movie movie;

    private ActorServiceImpl actorService;
    private MovieServiceImpl movieService;


    @BeforeEach
    void setUp() {
        actor = new Actor();
        actor.setId(1L);
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setGender(Gender.MALE);
        actor.setBirthDate(LocalDate.of(1980, 1, 1));

        movie = new Movie();
        movie.setId(1L);
        movie.setName("Example Movie");
        movie.setDirectorName("Jane Doe");
        movie.setRating(8);
        movie.setReleaseDate(LocalDate.of(2020, 1, 1));
        actorService = new ActorServiceImpl(mockActorRepository);
        movieService = new MovieServiceImpl(mockMovieRepository);
    }

    @Test
    @DisplayName("Add Movie to Actor")
    void testAddMovieToActor() {
        actor.addMovie(movie);
        assertEquals(1, actor.getMovies().size(), "Actor should have one movie.");
        assertTrue(actor.getMovies().contains(movie), "Movie should be in actor's movie list.");
    }

    @Test
    @DisplayName("Add Actor to Movie")
    void testAddActorToMovie() {
        movie.addActor(actor);
        assertEquals(1, movie.getActors().size(), "Movie should have one actor.");
        assertTrue(movie.getActors().contains(actor), "Actor should be in movie's actor list.");
    }

    @Test
    @DisplayName("Actor Setters and Getters")
    void testActorSettersAndGetters() {
        assertEquals(1L, actor.getId());
        assertEquals("John", actor.getFirstName());
        assertEquals("Doe", actor.getLastName());
        assertEquals(Gender.MALE, actor.getGender());
        assertEquals(LocalDate.of(1980, 1, 1), actor.getBirthDate());
    }

    @Test
    @DisplayName("Movie Setters and Getters")
    void testMovieSettersAndGetters() {
        assertEquals(1L, movie.getId());
        assertEquals("Example Movie", movie.getName());
        assertEquals("Jane Doe", movie.getDirectorName());
        assertEquals(8, movie.getRating());
        assertEquals(LocalDate.of(2020, 1, 1), movie.getReleaseDate());
    }

    @Test
    @DisplayName("ApiException Custom Constructor")
    void testApiExceptionCustomConstructor() {
        String expectedMessage = "Custom error message";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        ApiException exception = new ApiException(expectedMessage, expectedStatus);


        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the input.");


        assertEquals(expectedStatus, exception.getHttpStatus(), "The HttpStatus should match the input.");
    }

    @Test
    @DisplayName("Ensure ApiException is a RuntimeException")
    void ensureApiExceptionIsARuntimeException() {
        ApiException exception = new ApiException("Error", HttpStatus.INTERNAL_SERVER_ERROR);


        try {
            throw exception;
        } catch (RuntimeException e) {
            assertTrue(e instanceof ApiException, "ApiException should be an instance of RuntimeException.");
        }
    }

    @Test
    @DisplayName("ExceptionResponse Properties Initialization")
    void testExceptionResponsePropertiesInitialization() {
        String expectedMessage = "Error occurred";
        int expectedStatus = 400;
        LocalDateTime expectedDateTime = LocalDateTime.now();

        ExceptionResponse exceptionResponse = new ExceptionResponse(expectedMessage, expectedStatus, expectedDateTime);

        // Verify that each property is correctly initialized
        assertEquals(expectedMessage, exceptionResponse.getMessage(), "The message should match the initialized value.");
        assertEquals(expectedStatus, exceptionResponse.getStatus(), "The status should match the initialized value.");
        assertEquals(expectedDateTime, exceptionResponse.getDateTime(), "The dateTime should match the initialized value.");
    }

    @Test
    @DisplayName("ActorRepository should be  instance of JpaRepository")
    void actorRepositoryInstanceCheck() {

        assertTrue(mockActorRepository instanceof JpaRepository, "ActorRepository should be an instance of JpaRepository");
    }

    @Test
    @DisplayName("MovieRepository should be  instance of JpaRepository")
    void movieRepositoryInstanceCheck() {

        assertTrue(mockMovieRepository instanceof JpaRepository, "MovieRepository should be an instance of JpaRepository");
    }


    @Test
    @DisplayName("Find All Actors")
    void findAll() {
        when(mockActorRepository.findAll()).thenReturn(Arrays.asList(actor));
        List<Actor> actors = actorService.findAll();
        assertEquals(actors.size(), 1);
    }

    @Test
    @DisplayName("Find Actor By Id - Success")
    void findByIdSuccess() {
        when(mockActorRepository.findById(any())).thenReturn(Optional.of(actor));
        Actor foundActor = actorService.findById(1L);
        assertThat(foundActor).isNotNull();
        assertThat(foundActor.getId()).isEqualTo(actor.getId());
    }

    @Test
    @DisplayName("Find Actor By Id - Not Found")
    void findByIdNotFound() {
        long actorId = 2L;

        assertThatThrownBy(() -> actorService.findById(actorId))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("actor is not found with id: " + actorId);
    }

    @Test
    @DisplayName("Save Actor")
    void save() {
        when(mockActorRepository.save(any())).thenReturn(actor);
        Actor savedActor = actorService.save(actor);
        assertThat(savedActor).isNotNull();
        verify(mockActorRepository).save(any());
    }

    @Test
    @DisplayName("Delete Actor")
    void delete() {

        actorService.delete(actor);

    }

    @Test
    @DisplayName("Find all movies")
    void findAllMovies() {
        when(mockMovieRepository.findAll()).thenReturn(Arrays.asList(movie));
        List<Movie> movies = movieService.findAll();
        assertThat(movies.get(0)).isEqualTo(movie);
    }

    @Test
    @DisplayName("Find movie by ID - Success")
    void findMovieByIdSuccess() {
        when(mockMovieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
        Movie foundMovie = movieService.findById(movie.getId());
        assertThat(foundMovie).isNotNull();
        assertThat(foundMovie.getId()).isEqualTo(movie.getId());
    }

    @Test
    @DisplayName("Find movie by ID - Not Found")
    void findMovieByIdNotFound() {
        when(mockMovieRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> movieService.findById(2L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Movie is not found with id: 2")
                .matches(exception -> ((ApiException) exception).getHttpStatus() == HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Save movie")
    void saveMovie() {
        when(mockMovieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie savedMovie = movieService.save(new Movie());
        assertThat(savedMovie).isNotNull();
        assertThat(savedMovie.getId()).isEqualTo(movie.getId());
    }

    @Test
    @DisplayName("Delete movie")
    void deleteMovie() {
        doNothing().when(mockMovieRepository).delete(any(Movie.class));
        movieService.delete(movie);
        verify(mockMovieRepository, times(1)).delete(movie);
    }

}
