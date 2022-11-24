package com.project.lovlev.models.services;

import com.project.lovlev.enums.Sex;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Data
public class CustomValidators {

    public void validatePassword(String password) {
        if (!Pattern.matches("^.[^\s]{4,20}", password)) {
            throw new IllegalArgumentException("password must be between 4-20 characters, no spaces allowed");
        }
    }
}
