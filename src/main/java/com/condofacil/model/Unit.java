package com.condofacil.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "unit")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_uuid", nullable = false, unique = true, updatable = false)
    private UUID unitUuid;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private Integer floor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitType type;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;

    @PrePersist
    public void generateUuid(){
        if (this.unitUuid == null){
            this.unitUuid = UUID.randomUUID();
        }
    }
}
