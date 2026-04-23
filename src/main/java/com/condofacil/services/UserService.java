package com.condofacil.services;

import com.condofacil.dto.UserRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void  create(UserRegistrationDTO dto){

        UUID personUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();

        String sqlPerson = """
                INSERT INTO person (person_uuid, full_name, tax_id, email, phone, create_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sqlPerson, new  String[]{"id"});
            ps.setObject(1, personUuid);
            ps.setString(2, dto.fullName());
            ps.setString(3, dto.taxId());
            ps.setString(4, dto.email());
            ps.setString(5, dto.phone());
            ps.setObject(6 , LocalDateTime.now());
            return ps;
        }, keyHolder);

        Long personId = keyHolder.getKey().longValue();

        String sqlUser = """
                INSERT INTO users(user_uuid, login, password, role, person_id)
                VALUES(?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sqlUser,
                userUuid,
                dto.login(),
                passwordEncoder.encode(dto.password()),
                dto.roleType().name(),
                personId
        );
    }
}
