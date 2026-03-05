package com.condofacil.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "block")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_uuid", nullable = false, unique = true, updatable = false)
    private UUID blockUuid;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy carrega os dados do condomínio só se precisarmos
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL)
    private List<Unit> units = new ArrayList<>();

    @PrePersist
    public void generateUuid(){
        if (this.blockUuid == null){
            this.blockUuid = UUID.randomUUID();
        }
    }

}
