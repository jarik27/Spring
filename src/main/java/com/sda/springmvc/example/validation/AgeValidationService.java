package com.sda.springmvc.example.validation;

import com.sda.springmvc.example.entities.User;
import org.springframework.stereotype.Service;

@Service
public class AgeValidationService {

    public boolean isValid(User user) {
        return user.getAge() >= 18 && user.getAge() <= 70;
    }
}
