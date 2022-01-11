package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Controller
@Configuration
@RequestMapping
public class controller {
	private static List<String> defaultColumns = Arrays.asList(new String[]{"Curso", "Unidade de execução", "Turno", "Turma", "Inscritos no turno (no 1º semestre é baseado em estimativas)", "Turnos com capacidade superior à capacidade das características das salas", 
			"Turno com inscrições superiores à capacidade das salas", "Dia da Semana", "Início", "Fim", "Dia", "Características da sala pedida para a aula", "Sala da aula", "Lotação", "Características reais da sala"});
	List<String> params;
	List<String> items;
	ArrayList<int[]> resultado;

	@GetMapping("/")
	public String mapping(Model model) {
		return "import";
	}

	@GetMapping("/FIFO")
	public String fifo(Model model) {
		String csvOutputFile;
		try {
			csvOutputFile = Files.readString(Paths.get("src/main/resources", "final" + "FIFO" + ".csv"));
			model.addAttribute("CSV", csvOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado", resultado);
		model.addAttribute("nome", "FIFO");
		return "res";
	}

	@GetMapping("/download/{algoritmo}")
	public ResponseEntity<ByteArrayResource> download(@PathVariable String algoritmo) {
		Path path = Paths.get("src/main/resources", "final" + algoritmo + ".csv");
		ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().header(algoritmo).contentType(MediaType.parseMediaType("text/csv")).body(resource);
	}

	@GetMapping("/LIFO")
	public String lifo(Model model) {
		String csvOutputFile;
		try {
			csvOutputFile = Files.readString(Paths.get("src/main/resources", "final" + "LIFO" + ".csv"));
			model.addAttribute("CSV", csvOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado", resultado);
		model.addAttribute("nome", "LIFO");
		return "res";
	}

	@GetMapping("/RANDOM")
	public String random(Model model) {
		String csvOutputFile;
		try {
			csvOutputFile = Files.readString(Paths.get("src/main/resources", "final" + "RANDOM" + ".csv"));
			model.addAttribute("CSV", csvOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado", resultado);
		model.addAttribute("nome", "Aleatório");
		return "res";
	}

	@GetMapping("/LOWERCAPACITYFIRST")
	public String lcf(Model model) {
		String csvOutputFile;
		try {
			csvOutputFile = Files.readString(Paths.get("src/main/resources", "final" + "LOWERCAPACITYFIRST" + ".csv"));
			model.addAttribute("CSV", csvOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado", resultado);
		model.addAttribute("nome", "Menor Capacidade Primeiro");
		return "res";
	}

	@GetMapping("/LESSCARACTFIRST")
	public String lesscaractfirst(Model model) {
		String csvOutputFile;
		try {
			csvOutputFile = Files.readString(Paths.get("src/main/resources", "final" + "LESSCARACTFIRST" + ".csv"));
			model.addAttribute("CSV", csvOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("resultado", resultado);
		model.addAttribute("nome", "Menor Nº de Características Primeiro");
		return "res";
	}

	@PostMapping("/post")
	public String post(Model model, @RequestParam("salas") MultipartFile file,
			@RequestParam("horarios") MultipartFile file2, @RequestParam(required = false, value = "FIFO") String FIFO,
			@RequestParam(required = false, value = "LIFO") String LIFO,
			@RequestParam(required = false, value = "random") String random,
			@RequestParam(required = false, value = "LCF") String LCF,
			@RequestParam(required = false, value = "LESSCARACTFIRST") String LESSCARACTFIRST,
			@RequestParam(required = false, value = "segunda") String segunda,
			@RequestParam(required = false, value = "terca") String terca,
			@RequestParam(required = false, value = "quarta") String quarta,
			@RequestParam(required = false, value = "quinta") String quinta,
			@RequestParam(required = false, value = "sexta") String sexta,
			@RequestParam(required = false, value = "sabado") String sabado,
			@RequestParam(required = false, value = "flexRadioCaract") String caracter,
			@RequestParam(required = false, value = "flexRadioCaractSala") String caracterSala) {

		Path filepath = Paths.get("src/main/resources", file.getOriginalFilename());
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
		CSVReader csvReader;

		params= new ArrayList<String>();
		params.add(filepath.toString());params.add(filepath2.toString());params.add(FIFO);params.add(LIFO);params.add(random);params.add(LCF);
		params.add(LESSCARACTFIRST);params.add(segunda);params.add(terca);params.add(quarta);
		params.add(quinta);params.add(sexta);params.add(sabado);params.add(caracter);
		params.add(caracterSala);
		try {
			csvReader = new CSVReader(new FileReader(filepath.toString()));
			List<String[]> csvData = csvReader.readAll();
			String columns=csvData.get(0)[0];
			items = Arrays.asList(columns.split(";"));
			if(items.size()==defaultColumns.size()) {
				for(int i=0;i<items.size();i++) {
					items.set(i, items.get(i).replaceAll("\\P{Print}",""));
					defaultColumns.set(i, defaultColumns.get(i).replaceAll("\\P{Print}",""));
				}
			}
			else {
				if(items.size()<defaultColumns.size())
					return "falta";
				model.addAttribute("colunas",items);
				return "options";
			}
			if(items.equals(defaultColumns)){

			}
			else {
				model.addAttribute("colunas",items);
				return "options";
			}
		} catch (IOException | CsvException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		boolean caract;

		resultado = new ArrayList<int[]>();

		if (caracter.equals("true"))
			caract = true;
		else
			caract = false;

		boolean caractSala;

		if (caracterSala.equals("true"))
			caractSala = true;
		else
			caractSala = false;

		double[] overfitValues = new double[6];
		
		if(segunda!=null)
			overfitValues[0]=Double.parseDouble(segunda)/100;
		else
			overfitValues[0] = 0;

		if (terca != null)
			overfitValues[1] = Double.parseDouble(terca) / 100;
		else
			overfitValues[1] = 0;
		
		if (quarta != null)
			overfitValues[2] = Double.parseDouble(quarta) / 100;
		else
			overfitValues[2] = 0;
		if (quinta != null)
			overfitValues[3] = Double.parseDouble(quinta) / 100;
		else
			overfitValues[3] = 0;
		if (quinta != null)
			overfitValues[4] = Double.parseDouble(sexta) / 100;
		else
			overfitValues[4] = 0;
		if (sabado != null)
			overfitValues[5] = Double.parseDouble(sabado) / 100;
		else
			overfitValues[5] = 0;

		ArrayList<String> algoritmosEscolhidos = new ArrayList<>();

		if (FIFO != null)
			algoritmosEscolhidos.add("FIFO");
		if (LIFO != null)
			algoritmosEscolhidos.add("LIFO");
		if (random != null)
			algoritmosEscolhidos.add("RANDOM");
		if (LCF != null)
			algoritmosEscolhidos.add("LOWERCAPACITYFIRST");
		if (LESSCARACTFIRST != null)
			algoritmosEscolhidos.add("LESSCARACTFIRST");

		try (OutputStream os = Files.newOutputStream(filepath)) {
			os.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (OutputStream os = Files.newOutputStream(filepath2)) {
			os.write(file2.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			resultado = CsvImporter.resultado(filepath.toString(), filepath2.toString(),
					Paths.get("src/main/resources", "final").toString(), overfitValues, caract, caractSala,
					algoritmosEscolhidos);
			model.addAttribute("resultado", resultado);
		} catch (IllegalStateException | IOException | CsvException e) {
			
		}
		String csvOutputFile;
		try {
			csvOutputFile = Files.readString(Paths.get("src/main/resources", "final"+algoritmosEscolhidos.get(0)+".csv"));
			model.addAttribute("CSV",csvOutputFile);
			model.addAttribute("nome",algoritmosEscolhidos.get(0));		
		} catch (IOException e2) {
			e2.printStackTrace();
		}	
		return "res";
	}

	@PostMapping("/post2")
	public String post2(Model model,@RequestParam("1") String string,@RequestParam("2") String string2,@RequestParam("3") String string3,@RequestParam("4") String string4
			,@RequestParam("5") String string5,@RequestParam("6") String string6,@RequestParam("7") String string7,@RequestParam("8") String string8,
			@RequestParam("9") String string9
			,@RequestParam("10") String string10) {
		try {
			CSV copy= new CSV(new File(params.get(0)),items.size());
			CSV base = new CSV (new File("src/main/resources/base.csv"),defaultColumns.size());
			for(int i=0; i<copy.getRows();i++) {
				base.set(i, 0, copy.get(i, items.indexOf(string)));
				base.set(i, 1, copy.get(i, items.indexOf(string2)));
				base.set(i, 2, copy.get(i, items.indexOf(string3)));
				base.set(i, 3, copy.get(i, items.indexOf(string4)));
				base.set(i, 4, copy.get(i, items.indexOf(string5)));
				base.set(i, 5, "false");
				base.set(i, 6, "false");
				base.set(i, 7, copy.get(i, items.indexOf(string6)));
				base.set(i, 8, copy.get(i, items.indexOf(string7)));
				base.set(i, 9, copy.get(i, items.indexOf(string8)));
				base.set(i, 10, copy.get(i, items.indexOf(string9)));
				base.set(i, 11, copy.get(i, items.indexOf(string10)));
			}
			base.save(new File("src/main/resources/base.csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean caractSala;

		if (params.get(14).equals("true"))
			caractSala = true;
		else
			caractSala = false;
		
		boolean caract;
		resultado= new ArrayList<int[]>();
		
		if(params.get(13).equals("true"))
			caract=true;
		else
			caract=false;
		double[] overfitValues= new double[6];
		if(params.get(7)!=null)
			overfitValues[0]=Double.parseDouble(params.get(7))/100;
		else
			overfitValues[0]=0;
		if(params.get(8)!=null)
			overfitValues[1]=Double.parseDouble(params.get(8))/100;
		else
			overfitValues[1]=0;
		if(params.get(9)!=null)
			overfitValues[2]=Double.parseDouble(params.get(9))/100;
		else
			overfitValues[2]=0;
		if(params.get(10)!=null)
			overfitValues[3]=Double.parseDouble(params.get(10))/100;
		else
			overfitValues[3]=0;
		if(params.get(11)!=null)
			overfitValues[4]=Double.parseDouble(params.get(11))/100;
		else
			overfitValues[4]=0;
		if(params.get(12)!=null)
			overfitValues[5]=Double.parseDouble(params.get(12))/100;
		else
			overfitValues[5]=0;

		ArrayList<String> algoritmosEscolhidos = new ArrayList<>();
		
		if(params.get(2)!=null)
			algoritmosEscolhidos.add("FIFO");
		if(params.get(3)!=null)
			algoritmosEscolhidos.add("LIFO");
		if(params.get(4)!=null)
			algoritmosEscolhidos.add("RANDOM");
		if(params.get(5)!=null)
			algoritmosEscolhidos.add("LOWERCAPACITYFIRST");
		if(params.get(6)!=null)
			algoritmosEscolhidos.add("LESSCARACTFIRST");
	    try {
	    	 resultado=CsvImporter.resultado("src/main/resources/base.csv", params.get(1),Paths.get("src/main/resources", "final").toString(),
	    			 overfitValues,caract,caractSala,algoritmosEscolhidos);
	    	 model.addAttribute("resultado",resultado);
	    } catch (IllegalStateException | IOException | CsvException e) {
			e.printStackTrace();
		}
		String csvOutputFile;
		try {
			csvOutputFile = Files
					.readString(Paths.get("src/main/resources", "final" + algoritmosEscolhidos.get(0) + ".csv"));
			model.addAttribute("CSV", csvOutputFile);
			model.addAttribute("nome", algoritmosEscolhidos.get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintWriter out;
		try {
			out = new PrintWriter("src/main/resources/base.csv");
			out.println("Curso;Unidade de execução;Turno;Turma;Inscritos no turno (no 1º semestre é baseado em estimativas);Turnoss com capacidade superior à capacidade das características das salas;Turno com inscrições superiores à capacidade das salas;Dia da Semana;Início;Fim;Dia;"
					+ "Características da sala pedida para a aula;Sala da aula;Lotação;Características reais da sala".getBytes("UTF-8"));
			out.flush();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "res";
	}
}