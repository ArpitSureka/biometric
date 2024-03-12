package com.example.biometric.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.biometric.model.Fingerprint;
import com.example.biometric.repository.FingerprintRepository;

import java.util.List;

@Service
public class FingerprintService {

    private FingerprintRepository fingerprintRepository;

    @Autowired
    public FingerprintService(FingerprintRepository userRepository) {
        this.fingerprintRepository = fingerprintRepository;
    }

    @Transactional
    public List<Fingerprint> getAllFingerprints() {
        return fingerprintRepository.findAll();
    }

    @Transactional
    public Fingerprint saveFingerprint(Fingerprint fingerprint) {
        return fingerprintRepository.save(fingerprint);
    }
}
