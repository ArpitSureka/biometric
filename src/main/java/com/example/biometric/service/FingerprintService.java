package com.example.biometric.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.biometric.model.Fingerprint;
import com.example.biometric.repository.FingerprintRepository;

@Service
public class FingerprintService {

    @Autowired
    private FingerprintRepository fingerprintRepository;

    @Transactional
    public List<Fingerprint> getAllFingerprints() {
        return fingerprintRepository.findAll();
    }

    @SuppressWarnings("null")
    @Transactional
    public Fingerprint saveFingerprint(Fingerprint fingerprint) {
        return fingerprintRepository.save(fingerprint);
    }
}
