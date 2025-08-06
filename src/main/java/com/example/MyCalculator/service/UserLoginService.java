package com.example.MyCalculator.service;

import com.example.MyCalculator.dto.UserSignupDto;
import com.example.MyCalculator.model.User;
import com.example.MyCalculator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.EmptyStackException;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;

    public User loginCheck (UserSignupDto userSignupDto) {
        User user = userRepository.findByUserName(userSignupDto.getUserName());

        if (user == null) {
            throw new EmptyResultDataAccessException(1);
        }

        if (!user.getPassword().equals(userSignupDto.getPassword())) {
            throw new IllegalArgumentException();
        }

        return user;
    }
}
