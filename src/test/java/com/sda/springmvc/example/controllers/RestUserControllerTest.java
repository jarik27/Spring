package com.sda.springmvc.example.controllers;

import com.sda.springmvc.example.entities.User;
import com.sda.springmvc.example.repositories.UserRepository;
import com.sda.springmvc.example.validation.AgeValidationService;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RestUserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AgeValidationService ageValidationService;

    @InjectMocks
    private RestUserController userController;

    @Before
    public void setUp() {
        standaloneSetup(userController);
    }

    @Test
    public void should_fetch_all_users_from_user_repository() {
        given()
                .when()
                .get("/api/users")
                .then()
                .log().ifValidationFails()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void should_return_not_found_when_user_does_not_exit() {
        given()
                .when()
                .get("/api/users/1")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value());

        verify(userRepository, times(1)).findById(eq(1L));
    }

    @Test
    public void should_reject_empty_json() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{}")
                .when()
                .post("/api/users")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void should_invoke_age_validation_service() {
        doReturn(true).when(ageValidationService).isValid(any(User.class));

        postValidEntity()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value());

        verify(ageValidationService, times(1)).isValid(any(User.class));
    }

    @Test
    public void should_return_bad_request_when_age_is_not_valid() {
        doReturn(false).when(ageValidationService).isValid(any(User.class));

        postValidEntity()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(CoreMatchers.is(CoreMatchers.equalTo("{" +
                        "\"status\":400," +
                        "\"error\":\"Bad Request\"," +
                        "\"message\":\"Age is not valid\"" +
                        "}")));
    }

    @Test
    @Ignore
    public void mockito_test() {
        class Foo {
            boolean isValid() {
                return true;
            }

            Optional<Foo> maybeThis() {
                return Optional.of(this);
            }

            List<String> tokens() {
                return Collections.singletonList("Hello");
            }
        }

        Foo mock = Mockito.mock(Foo.class);
        doReturn(true, false, true, true, false).when(mock).isValid();

        for (int i = 0; i < 10; ++i)
            System.out.println(mock.isValid());


        System.out.println(mock.maybeThis());

        System.out.println(mock.tokens());

    }

    private ValidatableMockMvcResponse postValidEntity() {
        final String validJson = "{" +
                "  \"name\": \"Josh Long\"," +
                "  \"email\": \"josh@long.com\"," +
                "  \"country\": \"US\"," +
                "  \"dateOfBirth\": \"2011-12-03\"" +
                "}";

        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(validJson)
                .when()
                .post("/api/users")
                .then()
                .log().ifValidationFails();
    }
}