package com.example.MyCalculator.service;

import com.example.MyCalculator.dto.UserSignupDto;

import com.example.MyCalculator.model.User;
import com.example.MyCalculator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;

    public User create (UserSignupDto userSignupDto) {
        if (userRepository.findByUserName(userSignupDto.getUserName()) != null) {
            throw new IllegalArgumentException();
        }

        User user = new User();
        user.setUserName(userSignupDto.getUserName());
        user.setPassword(userSignupDto.getPassword());
        userRepository.save(user);
        return user;
    }
}