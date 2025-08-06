package com.example.MyCalculator.repository;

import com.example.MyCalculator.model.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogRepository {
    private final JdbcTemplate jdbcTemplate;

    public int save(Log log) {

        String deleteSql = "DELETE " +
                "FROM logs " +
                "WHERE id IN (" +
                "SELECT id " +
                "FROM logs " +
                "WHERE user_id = ? AND fixed = false " +
                "ORDER BY created_at DESC " +
                "OFFSET 9" +
                ")";
        jdbcTemplate.update(deleteSql, log.getUserId());

        String sql = "INSERT " +
                "INTO logs (fixed, created_at, calculator_inputPanels, calculator_res, user_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        // 체크할때 세션써야한다?
        return jdbcTemplate.update(sql, log.isFixed(), log.getCreatedAt(), log.getCalculatorInputPanels(), log.getCalculatorRes(), log.getUserId());
    }

    public int updateFixed(Log log) {
        String sql = "UPDATE logs " +
                "SET fixed = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, log.isFixed(), log.getId());
    }

    public int delete(Log log) {
        String sql = "DELETE " +
                "FROM logs " +
                "WHERE id = ? AND fixed = true";
        return jdbcTemplate.update(sql, log.getId());
    }

    private final RowMapper<Log> logRowMapper = (resultSet, rowNum) -> {
        Log log = Log.builder()
                .id(resultSet.getInt("id"))
                .fixed(resultSet.getBoolean("fixed"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .calculatorInputPanels(resultSet.getString("calculator_inputPanels"))
                .calculatorRes(resultSet.getString("calculator_res"))
                .userId(resultSet.getInt("user_id"))
                .build();
        return log;
    };

    public List<Log> findAllLogsByUserId(Log log) {
        String sql = "SELECT id, fixed, created_at, calculator_inputPanels, calculator_res, user_id " +
                "FROM logs " +
                "WHERE user_id = ? " +
                "ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, logRowMapper, log.getUserId());
    }

    public List<Log> findLogsFixedByUserId(Log log) {
        String sql = "SELECT id, fixed, created_at, calculator_inputPanels, calculator_res, user_id " +
                "FROM logs " +
                "WHERE user_id = ? AND fixed = true " +
                "ORDER BY created_at DESC LIMIT 5";

        return jdbcTemplate.query(sql, logRowMapper, log.getUserId());
    }
    public List<Log> findLogsNotFixedByUserId(Log log) {
        String sql = "SELECT id, fixed, created_at, calculator_inputPanels, calculator_res, user_id " +
                "FROM logs " +
                "WHERE user_id = ? AND fixed = false " +
                "ORDER BY created_at DESC LIMIT 10";

        return jdbcTemplate.query(sql, logRowMapper, log.getUserId());
    }
}
