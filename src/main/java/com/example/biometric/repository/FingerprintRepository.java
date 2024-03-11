package com.example.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.biometric.model.Fingerprint;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, Long>{
}

