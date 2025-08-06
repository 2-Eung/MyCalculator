package com.example.MyCalculator.repository;

import com.example.MyCalculator.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public int save(User user) {
        String sql = "INSERT INTO users (user_name, password) VALUES (?, ?)";

        return jdbcTemplate.update(sql, user.getUserName(), user.getPassword());
    }

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        User user = User.builder()
                .id(resultSet.getInt("id"))
                .userName(resultSet.getString("user_name"))
                .password(resultSet.getString("password"))
                .build();
        return user;
    };

    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY id";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findByUserName(String userName) {
        String sql = "SELECT * FROM users WHERE user_name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, userName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
