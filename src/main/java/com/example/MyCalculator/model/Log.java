package com.example.MyCalculator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private Integer id;
    private boolean fixed;
    private LocalDateTime createdAt;

    private String calculatorInputPanels;
    private String calculatorRes;

    private Integer userId;
}
