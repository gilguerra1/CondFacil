package com.condofacil.repository;

import com.condofacil.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    Optional<Unit> findByUnitUuid(UUID unitUuid);

    List<Unit> findByBlock_BlockUuid(UUID blockUuid);

    boolean existsByNumberAndBlockId(String number, Long blockId);
}
