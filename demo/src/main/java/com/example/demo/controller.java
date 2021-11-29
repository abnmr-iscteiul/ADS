package com.example.demo;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

@Controller
@Configuration
@RequestMapping
public class controller {
	
	@GetMapping("/")
	public String mapping(Model model) {	
		return "import";
	}
	
	@PostMapping("/post")
	public String post(@RequestParam("salas") MultipartFile file,@RequestParam("horarios") MultipartFile file2,Model model) {
		
		Path filepath = Paths.get("src/main/resources", file.getOriginalFilename());

	    try (OutputStream os = Files.newOutputStream(filepath)) {
	        os.write(file.getBytes());
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    Path filepath2 = Paths.get("src/main/resources", file2.getOriginalFilename());

	    try (OutputStream os = Files.newOutputStream(filepath2)) {
	        os.write(file2.getBytes());
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		try {
			CsvImporter.resultado(filepath.toString(), filepath2.toString());
		} catch (IllegalStateException | IOException | CsvException e) {
		}

		return "import";
	}
	
}