package com.condofacil.services;

import com.condofacil.dto.UnitRequestDTO;
import com.condofacil.dto.UnitResponseDTO;
import com.condofacil.model.Block;
import com.condofacil.model.Unit;
import com.condofacil.model.UnitType;
import com.condofacil.repository.BlockRepository;
import com.condofacil.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository repository;
    private final BlockRepository blockRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public UnitResponseDTO create(UnitRequestDTO dto){
        log.info("Creating a new unit: {}", dto.number() );

        Block block = blockRepository.findByBlockUuid(dto.blockUuid())
                .orElseThrow(() -> new IllegalArgumentException("Blcok not found with UUID: " + dto.blockUuid()));

        UUID newUUID = UUID.randomUUID();

        String sql = """
                INSERT INTO unit(
                unit_uuid, number, floor, type, block_id
                ) VALUES (?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                newUUID,
                dto.number(),
                dto.floor(),
                dto.type().toString(),
                block.getId()
        );

        Unit entity = repository.findByUnitUuid(newUUID)
                .orElseThrow(() -> new RuntimeException("Erro retrieving saved unit"));

        return UnitResponseDTO.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    public UnitResponseDTO findByUuid(UUID uuid){

        log.info("Searching for unit with UUID {}", uuid);

        String sql = """
                SELECT
                    u.unit_uuid,
                    u.number,
                    u.floor,
                    u.type,
                    b.name AS block_name,
                    b.block_uuid AS block_uuid,
                    c.name AS condominium_name
                FROM unit u
                INNER JOIN block b ON u.block_id = b.id
                INNER JOIN condominium c ON b.condominium_id = c.id
                WHERE u.unit_uuid = ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UnitResponseDTO(
                rs.getObject("unit_uuid", UUID.class),
                rs.getString("number"),
                rs.getInt("floor"),
                UnitType.valueOf(rs.getString("type")),
                rs.getString("block_name"),
                rs.getString("condominium_name"),
                rs.getObject("block_uuid", UUID.class)
        ), uuid);
    }
}
