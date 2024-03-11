package com.example.biometric.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.machinezoo.sourceafis.FingerprintTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// import lombok.Data;

@Entity
@Table(name = "fingerprint")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fingerprint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "template")
    private FingerprintTemplate template;

    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getName() {
    //     return name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // public FingerprintTemplate getTemplate() {
    //     return template;
    // }

    // public void setTemplate(FingerprintTemplate template) {
    //     this.template = template;
    // }

}

