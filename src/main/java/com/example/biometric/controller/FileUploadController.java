package com.example.biometric.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.biometric.storage.StorageFileNotFoundException;
import com.example.biometric.storage.StorageService;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import com.example.biometric.model.Fingerprint;
import com.example.biometric.service.FingerprintService;

@Controller
public class FileUploadController {

	private final StorageService storageService;

    @Autowired
    private FingerprintService fingerprintService;

	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/compare")
	public String compareFingerprint(Model model) throws IOException {
		return "compareForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
			RedirectAttributes redirectAttributes) {

		storageService.store(file);
		Boolean output = addFingerprint(name, file.getName());
		redirectAttributes.addFlashAttribute("message",
				output + "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/";
	}

	@PostMapping("/compare")
	public String handleFileCompare(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		storageService.store(file);
		String output = compareAllFingerprints(file.getName());
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		redirectAttributes.addFlashAttribute("message", output);
		System.out.println(output);

		return "redirect:/compare";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

	private Boolean addFingerprint(String name, String filePath) {

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
            e.printStackTrace();
        }
        return false;
    }

    private String compareAllFingerprints(String filePath) {
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
            e.printStackTrace();
        }
        return null;
    }

}
