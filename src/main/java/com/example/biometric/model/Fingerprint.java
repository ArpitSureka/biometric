package com.example.biometric.model;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    private String template;

    @Column(name = "path")
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FingerprintTemplate getTemplate() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            return objectMapper.readValue(this.template , FingerprintTemplate.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to FingerprintTemplate", e);
        }
    }

    public void setTemplate(FingerprintTemplate template) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            this.template = objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error saving JSON from FingerprintTemplate", e);
            // Handle serialization error
        }
    }

}

