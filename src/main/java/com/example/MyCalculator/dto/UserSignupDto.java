package com.example.MyCalculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDto {
    @NotBlank(message = "아이디를 입력하세요")
    @Size(min = 3, max = 10, message = "아이디는 3 ~ 10자 여야 합니다.")
    private String userName;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 6, max = 20, message = "패스워드는 6 ~ 20자 여야 합니다.")
    private String password;
}
