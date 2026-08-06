package com.condofacil.services;

import com.condofacil.dto.UserRequestDTO;
import com.condofacil.dto.UserResponseDTO;
import com.condofacil.dto.UserUpdateDTO;
import com.condofacil.model.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO  create(UserRequestDTO dto){

        UUID personUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        String sqlPerson = """
                INSERT INTO person (person_uuid, full_name, tax_id, email, phone, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;


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

        return new UserResponseDTO(
                userUuid,
                dto.login(),
                dto.roleType(),
                personUuid,
                dto.fullName(),
                dto.email(),
                dto.phone()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        log.info("Fetching all users from database");

        String sql = """
                SELECT 
                    u.user_uuid,
                    u.login,
                    u.role,
                    p.person_uuid,
                    p.full_name,
                    p.email,
                    p.phone
                FROM users u
                INNER JOIN person p ON u.person_id = p.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserResponseDTO(
                rs.getObject("user_uuid", UUID.class),
                rs.getString("login"),
                RoleType.valueOf(rs.getString("role")),
                rs.getObject("person_uuid", UUID.class),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone")
        ));

    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByUuid(UUID uuid){

        log.info("Searching for user with UUID {}", uuid);

        String sql = """
               SELECT 
                    u.user_uuid,
                    u.login,
                    u.role,
                    p.person_uuid,
                    p.full_name,
                    p.email,
                    p.phone
                FROM users u
                INNER JOIN person p ON u.person_id = p.id
                WHERE u.user_uuid = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UserResponseDTO(
                    rs.getObject("user_uuid", UUID.class),
                    rs.getString("login"),
                    RoleType.valueOf(rs.getString("role")),
                    rs.getObject("person_uuid", UUID.class),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone")
            ), uuid);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("User not found with UUID: " + uuid);
        }
    }

    @Transactional
    public void delete(UUID uuid){

        log.info("Deleting user with UUID: {}", uuid);

        UserResponseDTO userResponseDTO = findByUuid(uuid);

        String sql = "DELETE FROM user WHERE user_uuid = ?";

        jdbcTemplate.update(sql, userResponseDTO.userUuid());

        log.info("Succesfully deleted user and person profile for UUID: {}", uuid);

    }

    @Transactional
    public UserResponseDTO update(UUID uuid, UserUpdateDTO dto){

        log.info("Updating user with UUID: {}", uuid);

        UserResponseDTO currentUser = findByUuid(uuid);

        StringBuilder sql = new StringBuilder("UPDATE user SET ");
        List<Object> params = new ArrayList<>();

        if (dto.login() != null){

            sql.append("login = ?, ");
            params.add(dto.login());

        }

        if (dto.password() != null){

            sql.append("password = ?, ");
            params.add(dto.password());

        }

        if (dto.roleType() != null){

            sql.append("roleType = ?, ");
            params.add(dto.roleType().name());

        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE user_uuid = ?");
        params.add(uuid);

        jdbcTemplate.update(sql.toString(), params.toArray());

        return findByUuid(uuid);
    }
}
