package com.sda.springmvc.example.controllers;

import com.sda.springmvc.example.entities.User;
import com.sda.springmvc.example.repositories.UserRepository;
import com.sda.springmvc.example.validation.AgeValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AgeValidationService ageValidationService;

    @Test
    public void should_return_main_page() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("index"))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void should_return_registration_form() throws Exception {
        mockMvc
                .perform(get("/signup"))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user-add"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_not_register_user_with_invalid_age() throws Exception {
        final MockHttpServletRequestBuilder post
                = post("/adduser").param("dateOfBirth", "2011-12-03");

        mockMvc
                .perform(post)
                .andExpect(view().name("user-age-not-valid"))
                .andExpect(status().isOk());

        verify(ageValidationService, times(1))
                .isValid(any(User.class));
    }
}