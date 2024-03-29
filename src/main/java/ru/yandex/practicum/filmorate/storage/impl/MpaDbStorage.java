package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATINGS ORDER BY MPA_ID", this::mapRowToObject);
    }

    @Override
    public Mpa getMpaById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM MPA_RATINGS WHERE MPA_ID = ?", this::mapRowToObject, id);
    }

    private Mpa mapRowToObject(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("MPA_ID"))
                .name(rs.getString("NAME"))
                .build();
    }
}