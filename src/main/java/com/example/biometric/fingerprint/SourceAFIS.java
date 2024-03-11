package com.example.biometric.fingerprint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.biometric.model.Fingerprint;
import com.example.biometric.service.FingerprintService;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

public class SourceAFIS {

    @Autowired
    private FingerprintService fingerprintService;
    
    public Boolean addFingerprint(String name, String filePath) {

        FingerprintImage image;
        try {
            image = new FingerprintImage(Files.readAllBytes(Paths.get(filePath)));
            var template = new FingerprintTemplate(image);
            Fingerprint newFingerprint = new Fingerprint();
            newFingerprint.setName(name);
            newFingerprint.setTemplate(template);
            fingerprintService.saveFingerprint(newFingerprint);
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public String compareAllFingerprints(String filePath) {
        FingerprintImage image;
        try {
            image = new FingerprintImage(Files.readAllBytes(Paths.get(filePath)));
            var probe = new FingerprintTemplate(image);
            var matcher = new FingerprintMatcher(probe);
            double max = Double.NEGATIVE_INFINITY;
            String match = "None Matched";
            List<Fingerprint> allFingerprints = fingerprintService.getAllFingerprints();
            for (Fingerprint fingerprint : allFingerprints) {
                double similarity = matcher.match(fingerprint.getTemplate());
                if (similarity > max) {
                    max = similarity;
                    match = fingerprint.getName() + " matched with " + similarity * 100 + "% similarity";
                }
            }
            return match;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
