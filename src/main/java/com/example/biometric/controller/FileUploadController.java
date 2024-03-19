package com.example.biometric.controller;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.example.biometric.model.Fingerprint;
import com.example.biometric.service.FingerprintService;
import com.example.biometric.service.StorageService;
import com.example.biometric.storage.StorageFileNotFoundException;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;


// import com.example.biometric.storage.StorageProperties;
@Controller
public class FileUploadController {

	@Autowired
	private StorageService storageService;

    @Autowired
    private FingerprintService fingerprintService;

	@Autowired
	private static Instrumentation instrumentation;

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
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String originalFileName = file.getOriginalFilename();
		System.out.println(originalFileName);
		String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
		String newFileName = timeStamp + extension;

		storageService.store(file, newFileName);
		// change file name 
		Boolean output = addFingerprint(name, newFileName, false);
		redirectAttributes.addFlashAttribute("message",
				output + "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/";
	}

	@PostMapping("/compare")
	public String handleFileCompare(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String originalFileName = file.getOriginalFilename();
		String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
		String newFileName = timeStamp + extension;

		storageService.store(file, newFileName);
		String output = compareAllFingerprints(newFileName, false);
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

	public Boolean addFingerprint(String name, String filePath, Boolean useTemplate) {
		if (useTemplate) {
			return addFingerprintUsingTemplate(name, filePath);
		} else {
			return addFingerprintUsingImages(name, filePath);
		}
	}

	private Boolean addFingerprintUsingTemplate(String name, String filePath) {

        FingerprintImage image;
        try {
			System.out.println(filePath);
			System.out.println("fdfvidfbndgfbtrdt");
			System.out.println(storageService.load(filePath));
			byte[] fingerprintFile = Files.readAllBytes(storageService.load(filePath));
			   // Get the size of the byte array
			int size = fingerprintFile.length;
			   // Print the size
			System.out.println("Size of fingerprintFile: " + size + " bytes");
            image = new FingerprintImage(fingerprintFile);
			System.out.println(image);
			// print datatype of image
			System.out.println(image.getClass().getName());
			//print size of fingerprintFile
			System.out.println("sdsronvgvueirnvietvgerigvegtv");
			System.out.println(fingerprintFile);
			System.out.println(fingerprintFile.length);
			//print pixel size of fingerprintFile
			System.out.println("egtrthrthtrgrtgrtgrtg");
			System.out.println(fingerprintFile.toString().toCharArray().length);
			System.out.println(fingerprintFile.toString());
            FingerprintTemplate template = new FingerprintTemplate(image);

			// print template
			

			System.out.println(template);
			// print datatype of template
			System.out.println(template.getClass().getName());
			// print size a variable is taking
			System.out.println("fgbgfhrtggerger");
			System.out.println(template);
			System.out.println(template.toByteArray().length);
			

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

	private Boolean addFingerprintUsingImages(String name, String filePath) {
		Fingerprint newFingerprint = new Fingerprint();
		newFingerprint.setName(name);
		newFingerprint.setPath(filePath);
		fingerprintService.saveFingerprint(newFingerprint);
		return true;
	}

	private String compareAllFingerprints(String filePath, Boolean useTemplate) {
		if (useTemplate) {
			return compareAllFingerprintsUsingTemplate(filePath);
		} else {
			return compareAllFingerprintsUsingImages(filePath);
		}
        
    }

    private String compareAllFingerprintsUsingTemplate(String filePath) {
        FingerprintImage image;
        try {
            image = new FingerprintImage(Files.readAllBytes(storageService.load(filePath)));
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

	private String compareAllFingerprintsUsingImages(String filePath) {
        FingerprintImage image;
        try {

			List<String> FingerprintFiles = storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList());
			for (String fingerprintFile : FingerprintFiles) {
				System.out.println(fingerprintFile);
			}

			System.out.println("sdsronvgvueirnvietvgerigvegtv");

            image = new FingerprintImage(Files.readAllBytes(storageService.load(filePath)));
            var probe = new FingerprintTemplate(image);
            var matcher = new FingerprintMatcher(probe);
            double max = Double.NEGATIVE_INFINITY;
            String match = "None Matched";
            List<Fingerprint> allFingerprints = fingerprintService.getAllFingerprints();
			String results = " ";
            for (Fingerprint fingerprint : allFingerprints) {
				var candidate = new FingerprintTemplate(new FingerprintImage(Files.readAllBytes(storageService.load(fingerprint.getPath()))));
                double similarity = matcher.match(candidate);
                if (similarity > max) {
                    max = similarity;
                    match = fingerprint.getName() + " matched with " + similarity  +  "similarity score"+ "\\n";
                }
				results = results + fingerprint.getName() + " " + similarity + "\n";
            }
            return match + results;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
