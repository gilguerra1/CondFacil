package com.condofacil.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_uuid", nullable = false, unique = true, updatable = false)
    private UUID personUuid;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "tax_id", unique = true, length = 14)
    private String taxId;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onPrePersist() {
        if (this.personUuid == null) {
            this.personUuid = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
    }
}
