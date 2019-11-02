package com.sda.springmvc.example.controllers;

import com.sda.springmvc.example.entities.User;
import com.sda.springmvc.example.events.UserSignupEvent;
import com.sda.springmvc.example.repositories.UserRepository;
import com.sda.springmvc.example.validation.AgeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

// Ctrl + Shift + T

@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AgeValidationService ageValidationService;

    public UserController(UserRepository userRepository,
                          ApplicationEventPublisher eventPublisher,
                          AgeValidationService ageValidationService) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.ageValidationService = ageValidationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "user-add";
    }

    @PostMapping("/adduser")
    public String create(@Valid User newUser, BindingResult result) {

        if (!ageValidationService.isValid(newUser)) {
            return "user-age-not-valid";
        }

        LOG.info("Creating user {}", newUser);

        if (result.hasErrors()) {
            return "user-add";
        }

        userRepository.save(newUser);
        LOG.info("Sending a signup event on thread {}", Thread.currentThread().getName());
        eventPublisher.publishEvent(UserSignupEvent.newInstance(newUser));

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") long userId, Model model) {
        return userRepository.findById(userId)
                .map(user -> getEditView(model, user))
                .orElse("user-not-found");
    }

    @PostMapping("/update/{userId}")
    public String update(@PathVariable long userId, @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-edit";
        }

        user.setId(userId);
        userRepository.save(user);

        return "redirect:/";
    }

    private String getEditView(Model model, User user) {
        model.addAttribute("user", user);
        return "user-edit";
    }

    @GetMapping("/delete/{userId}")
    public String confirmDeletion(@PathVariable long userId, Model model) {
        return userRepository.findById(userId)
                .map(user -> getDeleteView(model, user))
                .orElse("user-not-found");
    }

    private String getDeleteView(Model model, User user) {
        model.addAttribute("user", user);
        return "user-delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        userRepository.deleteById(id);
        return "redirect:/";
    }
}