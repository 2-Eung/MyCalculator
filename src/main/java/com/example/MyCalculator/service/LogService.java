package com.example.MyCalculator.service;

import com.example.MyCalculator.model.Log;
import com.example.MyCalculator.model.User;
import com.example.MyCalculator.repository.LogRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
@Builder
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final CalculatorService calculatorService;

    public int create(String inputPanels, HttpSession httpSession, Integer underPoint){
        User user = (User) httpSession.getAttribute("user");

        Log log = Log.builder()
                .fixed(false)
                .createdAt(LocalDateTime.now())
                .calculatorInputPanels(inputPanels)
                .calculatorRes(calculatorService.updateResult(inputPanels, underPoint))
                .userId(user.getId())
                .build();

        return logRepository.save(log);
    }

    public  int updateFixed(Integer logId) {

        Log log = Log.builder()
                .id(logId)
                .fixed(true)
                .build();

        return logRepository.updateFixed(log);
    }

    public int delete(Integer logId){

        Log log = Log.builder()
                .id(logId)
                .build();

        return logRepository.delete(log);
    }

    public List<Log> findLogsNotFixedByUserId (Log log) {
        return  logRepository.findLogsNotFixedByUserId(log);
    }

    public List<Log> findLogsFixedByUserId (Log log) {
        return  logRepository.findLogsFixedByUserId(log);
    }
}
