package com.sda.springmvc.example.events;

import com.sda.springmvc.example.entities.User;
import org.springframework.context.ApplicationEvent;

public class UserSignupEvent extends ApplicationEvent {

    private UserSignupEvent(Object source) {
        super(source);
    }

    public static UserSignupEvent newInstance(User newUser) {
        return new UserSignupEvent(newUser);
    }

    public String email() {
        return getUser().getEmail();
    }

    private User getUser() {
        return (User) getSource();
    }

    @Override
    public String toString() {
        User user = getUser();
        return "UserSignupEvent{username:" + user.getName() + ", email: " +  user.getEmail() + "}";
    }
}
