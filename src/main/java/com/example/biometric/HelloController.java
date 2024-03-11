package com.example.biometric;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	// @Controller public class UploadController {

    // public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    // @GetMapping("/uploadimage") public String displayUploadForm() {
    //     return "imageupload/index";
    // }

    // @PostMapping("/upload") public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
    //     StringBuilder fileNames = new StringBuilder();
    //     Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
    //     fileNames.append(file.getOriginalFilename());
    //     Files.write(fileNameAndPath, file.getBytes());
    //     model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
    //     return "imageupload/index";
    // }
// }

}