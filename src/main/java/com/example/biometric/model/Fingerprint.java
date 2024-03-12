package com.example.biometric.model;

import java.io.Serializable;

import com.machinezoo.sourceafis.FingerprintTemplate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

// import lombok.Data;

@Entity
@Table(name = "fingerprint")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fingerprint implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "template")
    private FingerprintTemplate template;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FingerprintTemplate getTemplate() {
        return template;
    }

    public void setTemplate(FingerprintTemplate template) {
        this.template = template;
    }

}

