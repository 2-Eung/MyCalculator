package com.example.MyCalculator.controller;

import com.example.MyCalculator.dto.UserSignupDto;
import com.example.MyCalculator.service.UserSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserSignupController {
    private final UserSignupService userSignupService;

    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("userSignupDto", new UserSignupDto());

        return "signup";
    }

    @PostMapping("/signup")
    public String doSignup(@Valid @ModelAttribute UserSignupDto userSignupDto,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) { return "signup"; }

        try {
            userSignupService.create(userSignupDto);
            return "redirect:/calculator";
        } catch (IllegalArgumentException e) {

            model.addAttribute("signupError", "이름이 중복되었습니다.");
            return "signup";
        }
    }
}