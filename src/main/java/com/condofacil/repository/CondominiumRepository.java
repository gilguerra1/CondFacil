package com.condofacil.repository;

import com.condofacil.model.Condominium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CondominiumRepository extends JpaRepository<Condominium, Long>, JpaSpecificationExecutor<Condominium>{

    Optional<Condominium> findByCondominiumUuid(UUID uuid);

    List<Condominium> findByNameContainingIgnoreCase(String name);

    boolean existsByTaxId(String taxId);
}
