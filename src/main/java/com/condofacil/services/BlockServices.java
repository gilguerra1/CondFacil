package com.condofacil.services;

import com.condofacil.dto.BlockRequestDTO;
import com.condofacil.dto.BlockResponseDTO;
import com.condofacil.model.Block;
import com.condofacil.model.Condominium;
import com.condofacil.repository.BlockRepository;
import com.condofacil.repository.CondominiumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockServices {

    private final BlockRepository repository;
    private final CondominiumRepository condominiumRepository;
    private final JdbcTemplate jdbcTemplate;


    @Transactional
    public BlockResponseDTO create(BlockRequestDTO dto){
        log.info("Creating a new block: {}", dto.name());

        Condominium condominium = condominiumRepository.findByCondominiumUuid(dto.condominiumUuid())
                .orElseThrow(() -> new IllegalArgumentException("Condominium not found with UUID: " + dto.condominiumUuid()));

        UUID newUUID = UUID.randomUUID();

        String sql = """
                    INSERT INTO block(
                    block_uuid, name, condominium_id
                    ) VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                newUUID,
                dto.name(),
                condominium.getId()
        );

        Block entity = repository.findByBlockUuid(newUUID)
                .orElseThrow(() -> new RuntimeException("Erro retrieving saved block"));


        return BlockResponseDTO.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    public List<BlockResponseDTO> findAll(){
        log.info("Fetching all blocks");

        String sql = """
                    SELECT b.block_uuid, b.name, c.name as condominium_name
                    FROM block b
                    INNER JOIN condominium c ON b.condominium_id = c.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new BlockResponseDTO(
                rs.getObject("block_uuid", UUID.class),
                rs.getString("name"),
                rs.getString("condominium_name")
        ));
    }

    @Transactional(readOnly = true)
    public BlockResponseDTO findByUuid(UUID uuid){

        log.info("Searching for block with UUID {}");

        return repository.findByBlockUuid(uuid)
                .map(BlockResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Block not found"));
    }

    @Transactional(readOnly = true)
    public List<BlockResponseDTO> findByCondominium(UUID condominiumUuid){

        log.info("Searching for block in condominium with UUID: {}", condominiumUuid);

        Condominium condominium = condominiumRepository.findByCondominiumUuid(condominiumUuid)
                .orElseThrow(() -> new IllegalArgumentException("Condominium not found"));

        return repository.findByCondominium(condominium)
                .stream()
                .map(BlockResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void delete(UUID uuid){
        log.info("Deleting block with UUID: {}", uuid);

        Block block = repository.findByBlockUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Block not found"));

        repository.delete(block);
    }

    @Transactional
    public BlockResponseDTO update(UUID uuid, BlockRequestDTO dto){

        log.info("Updating block with UUID: {}", uuid);

        this.findByUuid(uuid);

        String sql = """
                Update block
                SET name = ?
                WHERE block_uuid = ?
                """;

        int rowsAffected = jdbcTemplate.update(
                sql,
                dto.name(),
                uuid
        );

        if (rowsAffected == 0){
            throw new RuntimeException("Failed to update block: UUID not found");
        }

        return findByUuid(uuid);
    }
}
