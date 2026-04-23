package com.condofacil.services;

import com.condofacil.dto.CondominiumRequestDTO;
import com.condofacil.repository.CondominiumRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CondominiumServicesTest {

    @Mock
    private CondominiumRepository repository;

    @InjectMocks
    private CondominiumServices services;

    @Mock
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Sould throw exception when a Tax Id is already registered")
    void shouldThrowExceptionWhenTaxIdAlreadyRegistered(){

        String taxId = "12.345.678/0001-001";

        CondominiumRequestDTO dto = new CondominiumRequestDTO(
                "Condo Teste", "Legal Name", taxId, "Rua A",
                "1", "Madalena", "Recife", "PE", "58640-235",
                "1234-1235", "teste@email.com"
        );

        when(repository.existsByTaxId(taxId)).thenReturn(true);

        assertThatThrownBy(() -> services.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Condominium already registered with this tax id");

        verify(repository, never()).save(any());

        verifyNoInteractions(jdbcTemplate);
    }
}