package com.sda.springmvc.example.controllers;

import com.sda.springmvc.example.repositories.UserRepository;
import com.sda.springmvc.example.validation.AgeValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    // Ctrl K

    @Test
    public void should_return_main_page() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("index"))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).findAll();
    }
}