package com.condofacil.services;

import com.condofacil.dto.CondominiumFilterDTO;
import com.condofacil.dto.CondominiumRequestDTO;
import com.condofacil.dto.CondominiumResponseDTO;
import com.condofacil.model.Condominium;
import com.condofacil.repository.CondominiumRepository;
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
public class CondominiumServices {

    private final JdbcTemplate jdbcTemplate;
    private final CondominiumRepository repository;

    @Transactional
    public CondominiumResponseDTO create(CondominiumRequestDTO dto) {
        log.info("Creating a new condominium: {}", dto.name());

        if (repository.existsByTaxId(dto.taxId())) {
            throw new RuntimeException("Condominium already registered with this tax id");
        }

        UUID newUUID = UUID.randomUUID();

        String sql = """
                INSERT INTO condominium(
                    condominium_uuid, name, legal_name, tax_id, address, number,
                    neighborhood, city, state, zip_code,
                    concierge_phone, management_email
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                newUUID,
                dto.name(),
                dto.legalName(),
                dto.taxId(),
                dto.address(),
                dto.number(),
                dto.neighborhood(),
                dto.city(),
                dto.state(),
                dto.zipCode(),
                dto.conciergePhone(),
                dto.managementEmail()
        );

        Condominium entity = repository.findByCondominiumUuid(newUUID)
                        .orElseThrow(() -> new RuntimeException("Error retrieving saved condominium"));

        return CondominiumResponseDTO.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    public List<CondominiumResponseDTO> findAll(CondominiumFilterDTO filter) {
        log.info("Fetching all condominiums");

        StringBuilder sql = new StringBuilder("SELECT * FROM condominium WHERE 1=1");

        List<Object> params =  new ArrayList<>();

        if (filter != null){
            if (filter.name() != null && !filter.name().isBlank()){
                sql.append(" AND name ILIKE ?");
                params.add("%" + filter.name() + "%");
            }

            if (filter.city() != null && !filter.city().isBlank()){
                sql.append(" AND city ILIKE ? ");
                params.add("%" + filter.city() + "%");
            }

            if (filter.state() != null && !filter.state().isBlank()) {
                sql.append(" AND state = ?");
                params.add((filter.state().toUpperCase()));
            }

            if (filter.taxId() != null && !filter.taxId().isBlank()) {
                sql.append(" AND tax_id = ?");
                params.add(filter.taxId());
            }
        }

        sql.append(" ORDER BY name ASC");

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            return new CondominiumResponseDTO(
                    rs.getObject("condominium_uuid", UUID.class),
                    rs.getString("name"),
                    rs.getString("legal_name"),
                    rs.getString("tax_id"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("address"),
                    rs.getString("management_email")
            );
        }, params.toArray());
    }

    @Transactional(readOnly = true)
    public  CondominiumResponseDTO findByUuid(UUID uuid){
        log.info("Searching for condominium with UUID: {}", uuid);

        String sql =  "SELECT * FROM condominium WHERE condominium_uuid = ?";

        List<CondominiumResponseDTO> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new CondominiumResponseDTO(
                    rs.getObject("condominium_uuid", UUID.class),
                    rs.getString("name"),
                    rs.getString("legal_name"),
                    rs.getString("city"),
                    rs.getString("tax_id"),
                    rs.getString("state"),
                    rs.getString("address"),
                    rs.getString("management_email")
            );
        }, uuid);

        return results.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Condominium not found with UUID: " + uuid));
    }

    @Transactional
    public void delete(UUID uuid){
        log.info("Deleting condominium with UUID: {}", uuid);

        if(!repository.findByCondominiumUuid(uuid).isPresent()){
            throw new RuntimeException("Condominiun not found");
        }

        String sql =  "DELETE FROM condominium WHERE condominium_uuid = ?";

        int rowsAffected = jdbcTemplate.update(sql, uuid);

        log.info("Condominium deleted. Rows affected: {}", rowsAffected);
    }

    @Transactional(readOnly = true)
    public List<CondominiumResponseDTO> findByNameContaining(String name){
        log.info("Fiding conominiu with name like: {}", name);

        String sql = "SELECT * FROM condominium where name ILIKE ?";

        String nameParam = "%" + name + "%";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new CondominiumResponseDTO(
                    rs.getObject("condominium_uuid", UUID.class),
                    rs.getString("name"),
                    rs.getString("legal_name"),
                    rs.getString("city"),
                    rs.getString("tax_id"),
                    rs.getString("state"),
                    rs.getString("address"),
                    rs.getString("management_email")
            );
        }, nameParam);
    }

    @Transactional
    public CondominiumResponseDTO update(UUID uuid, CondominiumRequestDTO dto) {
        log.info("Updating condominium with UUID: {}", uuid);


        CondominiumResponseDTO existing = findByUuid(uuid);

        if (dto.taxId() != null && !dto.taxId().equals(existing.taxId())) {
            String checkSql = "SELECT COUNT(*) FROM condominium WHERE tax_id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, dto.taxId());

            if (count != null && count > 0) {
                throw new IllegalArgumentException("CNPJ already in use");
            }
        }

        String sql = """
        UPDATE condominium
        SET name = ?, legal_name = ?, tax_id = ?, address = ?, number = ?,
            neighborhood = ?, city = ?, state = ?, zip_code = ?,
            concierge_phone = ?, management_email = ?
        WHERE condominium_uuid = ?
    """;

        jdbcTemplate.update(sql,
                dto.name(),
                dto.legalName(),
                dto.taxId(),
                dto.address(),
                dto.number(),
                dto.neighborhood(),
                dto.city(),
                dto.state(),
                dto.zipCode(),
                dto.conciergePhone(),
                dto.managementEmail(),
                uuid
        );

        return findByUuid(uuid);
    }
}