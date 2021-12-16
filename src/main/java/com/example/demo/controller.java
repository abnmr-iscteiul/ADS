package com.example.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.exceptions.CsvException;

@Controller
@Configuration
@RequestMapping
public class controller {
	ArrayList<int[]> resultado;
	@GetMapping("/")
	public String mapping(Model model) {	
		return "import";
	}
	
	@GetMapping("/FIFO")
	public String fifo(Model model) {		
		String csvOutputFile;
		try {
				csvOutputFile = Files.readString(Paths.get("src/main/resources", "final"+"FIFO"+".csv"));
				model.addAttribute("CSV",csvOutputFile);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado",resultado);
		model.addAttribute("nome","FIFO");		
		return "res";
	}

	@GetMapping("/download/{algoritmo}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String algoritmo) {
		Path path =Paths.get("src/main/resources", "final"+algoritmo+".csv");
        ByteArrayResource resource=null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(algoritmo)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
	
	@GetMapping("/LIFO")
	public String lifo(Model model) {		
		String csvOutputFile;
		try {
				csvOutputFile = Files.readString(Paths.get("src/main/resources", "final"+"LIFO"+".csv"));
				model.addAttribute("CSV",csvOutputFile);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado",resultado);
		model.addAttribute("nome","LIFO");		
		return "res";
	}
	
	@GetMapping("/RANDOM")
	public String random(Model model) {		
		String csvOutputFile;
		try {
				csvOutputFile = Files.readString(Paths.get("src/main/resources", "final"+"RANDOM"+".csv"));
				model.addAttribute("CSV",csvOutputFile);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado",resultado);
		model.addAttribute("nome","Aleatório");		
		return "res";
	}
	@GetMapping("/LOWERCAPACITYFIRST")
	public String lcf(Model model) {		
		String csvOutputFile;
		try {
				csvOutputFile = Files.readString(Paths.get("src/main/resources", "final"+"LOWERCAPACITYFIRST"+".csv"));
				model.addAttribute("CSV",csvOutputFile);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado",resultado);
		model.addAttribute("nome","Menor Capacidade Primeiro");		
		return "res";
	}	
	@PostMapping("/post")
	public String post(Model model,@RequestParam("salas") MultipartFile file,@RequestParam("horarios") MultipartFile file2,
			@RequestParam(required=false,value="FIFO") String FIFO,
			@RequestParam(required=false,value="LIFO") String LIFO, @RequestParam(required=false,value="random") String random,
			@RequestParam(required=false,value="LCF") String LCF,
			@RequestParam(required=false,value="segunda") String segunda,
			@RequestParam(required=false,value="terca") String terca, @RequestParam(required=false,value="quarta") String quarta,
			@RequestParam(required=false,value="quinta") String quinta, @RequestParam(required=false,value="sexta") String sexta, 
			@RequestParam(required=false,value="sabado") String sabado
			, @RequestParam(required=false,value="flexRadioCaract") String caracter) {
		boolean caract;
		
		System.out.println(caracter);
		
		if(caracter.equals("true"))
			caract=true;
		else
			caract=false;
		
		
		
		Path filepath = Paths.get("src/main/resources", file.getOriginalFilename());
		double[] overfitValues= new double[6];
		if(segunda!=null)
			overfitValues[0]=Double.parseDouble(segunda)/100;
		else
			overfitValues[0]=0;
		if(terca!=null)
			overfitValues[1]=Double.parseDouble(terca)/100;
		else
			overfitValues[1]=0;
		if(quarta!=null)
			overfitValues[2]=Double.parseDouble(quarta)/100;
		else
			overfitValues[2]=0;
		if(quinta!=null)
			overfitValues[3]=Double.parseDouble(quinta)/100;
		else
			overfitValues[3]=0;
		if(quinta!=null)
			overfitValues[4]=Double.parseDouble(sexta)/100;
		else
			overfitValues[4]=0;
		if(sabado!=null)
			overfitValues[5]=Double.parseDouble(sabado)/100;
		else
			overfitValues[5]=0;

		ArrayList<String> algoritmosEscolhidos = new ArrayList<>();
		
		if(FIFO!=null)
			algoritmosEscolhidos.add("FIFO");
		if(LIFO!=null)
			algoritmosEscolhidos.add("LIFO");
		if(random!=null)
			algoritmosEscolhidos.add("RANDOM");
		if(LCF!=null)
			algoritmosEscolhidos.add("LOWERCAPACITYFIRST");
		
		try (OutputStream os = Files.newOutputStream(filepath)) {
			os.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		Path filepath2 = Paths.get("src/main/resources", file2.getOriginalFilename());

		try (OutputStream os = Files.newOutputStream(filepath2)) {
	        os.write(file2.getBytes());
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    try {
	    	 resultado=CsvImporter.resultado(filepath.toString(), filepath2.toString(),Paths.get("src/main/resources", "final").toString(),
	    			 overfitValues,caract,algoritmosEscolhidos);
	    	 model.addAttribute("resultado",resultado);
	    } catch (IllegalStateException | IOException | CsvException e) {
			e.printStackTrace();
		}
	    System.out.println(segunda + terca);
	    for(int i=0;i<algoritmosEscolhidos.size();i++) {
	    	
	    }
	    	
	    String csvOutputFile;
		try {
			csvOutputFile = Files.readString(Paths.get("src/main/resources", "final"+algoritmosEscolhidos.get(0)+".csv"));
			model.addAttribute("CSV",csvOutputFile);
			model.addAttribute("nome",algoritmosEscolhidos.get(0));		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "res";
	}
	
}