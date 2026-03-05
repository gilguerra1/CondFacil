package com.condofacil.repository;

import com.condofacil.model.Block;
import com.condofacil.model.Condominium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByBlockUuid(UUID blockUuid);

    List<Block> findByCondominium(Condominium condominium);
}
