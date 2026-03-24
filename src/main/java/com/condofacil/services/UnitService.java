package com.condofacil.services;

import com.condofacil.dto.UnitRequestDTO;
import com.condofacil.dto.UnitResponseDTO;
import com.condofacil.dto.UnitUpdateDTO;
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

import java.util.ArrayList;
import java.util.List;
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

        if (repository.existsByNumberAndBlockId(dto.number(), block.getId())){
            throw  new IllegalArgumentException("The unit " + dto.number() + "already exist in this block");
        }
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

    @Transactional(readOnly = true)
    public List<UnitResponseDTO> findAll() {

        log.info("Fetching all units");

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
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new UnitResponseDTO(
                rs.getObject("unit_uuid", UUID.class),
                rs.getString("number"),
                rs.getInt("floor"),
                UnitType.valueOf(rs.getString("type")),
                rs.getString("block_name"),
                rs.getString("condominium_name"),
                rs.getObject("block_uuid", UUID.class)
        ));
    }

    @Transactional(readOnly = true)
    public List<UnitResponseDTO> findUnitsByBlockUuid(UUID uuid){

        log.info("Fetching all units at block uuid: {}", uuid);

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
                WHERE b.block_uuid = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new UnitResponseDTO(
                rs.getObject("unit_uuid", UUID.class),
                rs.getString("number"),
                rs.getInt("floor"),
                UnitType.valueOf(rs.getString("type")),
                rs.getString("block_name"),
                rs.getString("condominium_name"),
                rs.getObject("block_uuid", UUID.class)
        ), uuid);
    }

    @Transactional
    public UnitResponseDTO update (UUID uuid, UnitUpdateDTO dto){

        log.info("Updating unit with UUID: {}", uuid);

        this.findByUuid(uuid);

        Unit currentUnit = repository.findByUnitUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        StringBuilder sql = new StringBuilder("UPDATE unit SET ");
        List<Object> params = new ArrayList<>();

        if (dto.number() != null && !dto.number().equals(currentUnit.getNumber())) {
            if (repository.existsByNumberAndBlockId(dto.number(), currentUnit.getBlock().getId())) {
                throw new IllegalArgumentException("Unit " + dto.number() + " already exists at this block.");
            }
            sql.append("number = ?, ");
            params.add(dto.number());
        }

        if (dto.floor() != null){
            sql.append("floor = ?, ");
            params.add(dto.floor());
        }

        if (dto.type() != null){
            sql.append("type = ?, ");
            params.add(dto.type().name());
        }

        if (params.isEmpty()){
            return findByUuid(uuid);
        }
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE unit_uuid = ?");
        params.add(uuid);

        jdbcTemplate.update(sql.toString(), params.toArray());

        return findByUuid(uuid);

    }

    @Transactional
    public void  delete(UUID uuid){

        log.info("Deleting unit with UUID: {}", uuid);

        Unit unit = repository.findByUnitUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        String sql = "DELETE FROM unit WHERE unit_uuid = ?";

        int rowsAffected = jdbcTemplate.update(sql, uuid);

        log.info("Unit removed. Rows Affected: {}", rowsAffected);
    }

}
