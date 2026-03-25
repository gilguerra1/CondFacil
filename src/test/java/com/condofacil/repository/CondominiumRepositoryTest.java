package com.condofacil.repository;

import com.condofacil.model.Condominium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CondominiumRepositoryTest {

    private Condominium createCondo(String name, String taxId) {
        Condominium condo = new Condominium();
        condo.setName(name);
        condo.setTaxId(taxId);
        condo.setCondominiumUuid(UUID.randomUUID());
        condo.setCity("Recife");
        condo.setState("PE");
        return repository.save(condo);
    }

    @Autowired
    private CondominiumRepository repository;

    @Test
    @DisplayName("Should return TRUE when TaxId exist at database")
    void existsByTaxId() {

        Condominium condominium = createCondo("Cesar School", "123.456.78/0001-01");

        boolean exists = repository.existsByTaxId(condominium.getTaxId());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return FALSE when TaxId do not exist at database")
    void returnFalseWhenTaxIdNotExist(){
        String taxIdNotExist = "00.000.000/0001-00";

        boolean exists = repository.existsByTaxId(taxIdNotExist);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should found condominium using UUID")
    void findByCondominiumUuid(){

        Condominium condominium = createCondo("Cesar School", "123.456.78/0001-01");

        Optional<Condominium> result = repository.findByCondominiumUuid(condominium.getCondominiumUuid());


        assertThat(result).isPresent();

        assertThat(result.get())
                .extracting(Condominium::getCondominiumUuid, Condominium::getName, Condominium::getTaxId)
                .containsExactly(condominium.getCondominiumUuid(), "Cesar School", condominium.getTaxId());
    }

    @Test
    @DisplayName("Should not find condominiun with non-existent UUID")
    void notFindByCondominiumUuid(){

        UUID nonExistenUuid= UUID.randomUUID();

        Optional<Condominium> result = repository.findByCondominiumUuid(nonExistenUuid);

        assertThat(result).isEmpty();

    }
}