package com.condofacil.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "condominium")
public class Condominium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "condominium_uuid", nullable = false, unique = true, updatable = false)
    private UUID condominiumUuid;

    @Column(nullable = false)
    private String name;

    @Column(name = "legal_name")
    private String legalName; //razão social

    @Column(name = "tax_id", length = 18, unique = true)
    private String taxId; //Cnpj

    private String address;

    private String number;

    private String neighborhood;

    private String city;

    private String state;

    @Column(name = "zip_code")
    private String zipCode;  //Cep

    @Column(name = "concierge_phone")
    private String conciergePhone; // telefonePortaria

    @Column(name = "management_email")
    private String managementEmail;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCondominiumUuid() {
        return condominiumUuid;
    }

    public void setCondominiumUuid(UUID condominiumUuid) {
        this.condominiumUuid = condominiumUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getManagementEmail() {
        return managementEmail;
    }

    public void setManagementEmail(String managementEmail) {
        this.managementEmail = managementEmail;
    }

    public String getConciergePhone() {
        return conciergePhone;
    }

    public void setConciergePhone(String conciergePhone) {
        this.conciergePhone = conciergePhone;
    }

    @PrePersist
    public void generateUuid(){
        if (this.condominiumUuid == null){
            this.condominiumUuid = UUID.randomUUID();
        }
    }
}
