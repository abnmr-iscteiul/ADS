package com.example.demo;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class CsvImporter {

	private static List<String[]> csvReader;
	private static ArrayList<String> uniqueDates = new ArrayList<String>();

	// ALGORITMOS - sexta e sabado com mais alunos que lugares da sala, nao ter em
	// conta as caracteristicas, ter em conta as caracteristicas

	// METRICAS DE AVALIAÇAO - numero de aulas sem sala, trocas de edificio,
	// mudanças de sala, salas disponiveis, numero de salas atribuidas com a
	// caracteristica pedida

	public static void main(String[] args) throws IOException, CsvException {
		//String fileNameAulas = "C:\\Users\\Chainz\\Desktop\\ADS - Exemplo de horario do 1o Semestre.csv";
		//String fileNameSala = "C:\\Users\\Chainz\\Desktop\\ads.csv";
		
		String fileNameAulas = "D:\\ADS\\ADS - Salas.csv";
		String fileNameSala = "D:\\ADS\\abc10.csv";
		String algoritmo = "";
		String resultado = "";

		List<Sala> salas = new CsvToBeanBuilder<Sala>(new FileReader(fileNameSala)).withSkipLines(1).withSeparator(';')
				.withType(Sala.class).build().parse();

		List<Aula> aulas = new CsvToBeanBuilder<Aula>(new FileReader(fileNameAulas)).withSkipLines(1).withSeparator(';')
				.withType(Aula.class).build().parse();

		adicionarCaracteristicas(salas, fileNameSala);

		uniqueDates = getAllDates(aulas);

		separarPorDia(aulas, salas, uniqueDates, algoritmo);

		contarAulasComSalasAtribuidas(aulas);

		printCSVFinal(fileNameAulas, aulas, resultado);

	}
	
	public static void resultado(String fileNameAulas,String fileNameSala,  String path) throws IllegalStateException, IOException, CsvException {
		
		String algoritmo = "sextaESabado";
		
		List<Sala> salas = new CsvToBeanBuilder<Sala>(new FileReader(fileNameSala)).withSkipLines(1).withSeparator(';')
				.withType(Sala.class).build().parse();

		List<Aula> aulas = new CsvToBeanBuilder<Aula>(new FileReader(fileNameAulas)).withSkipLines(1).withSeparator(';')
				.withType(Aula.class).build().parse();
		
		adicionarCaracteristicas(salas, fileNameSala);

		uniqueDates = getAllDates(aulas);

		separarPorDia(aulas, salas, uniqueDates, algoritmo);

		contarAulasComSalasAtribuidas(aulas);

		printCSVFinal(fileNameAulas, aulas,path);

	}
	

	private static void printCSVFinal(String original, List<Aula> aulas, String path) throws IOException, CsvException {
		File csvOutputFile = new File(path);
		String[] csvHeader;

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(original)).withCSVParser(csvParser).build()) {
			csvHeader = reader.readNext();
		}

		try (Writer writer = new FileWriter(csvOutputFile);
				CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.NO_QUOTE_CHARACTER,
						CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);) {

			csvWriter.writeNext(csvHeader);

			for (Aula aulaPrenchida : aulas) {
				csvWriter.writeNext(aulaPrenchida.printToCSV());
			}

		}
	}

	/*
	 * public static File modifyFile(String filePath) throws IOException,
	 * CsvException {
	 * 
	 * File tempCSV = File.createTempFile("tempCSV", ".csv");
	 * System.out.println(tempCSV.getAbsolutePath()); tempCSV.deleteOnExit();
	 * 
	 * CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); //
	 * custom separator try (CSVReader reader = new CSVReaderBuilder(new
	 * FileReader(filePath)).withCSVParser(csvParser).build()) {
	 * 
	 * csvReader = reader.readAll(); CSVWriter writer = new CSVWriter(new
	 * FileWriter(tempCSV.getAbsolutePath()), ';', CSVWriter.NO_QUOTE_CHARACTER,
	 * CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
	 * 
	 * for (int i = 0; i < csvReader.size(); i++) { for (int j = 0; j <
	 * csvReader.get(i).length; j++) { if (csvReader.get(i)[j].equals("")) {
	 * csvReader.get(i)[j] = "false";
	 * 
	 * } else if (csvReader.get(i)[j].equals("X")) { csvReader.get(i)[j] = "true"; }
	 * } writer.writeNext(csvReader.get(i));
	 * 
	 * } writer.close(); } return tempCSV; }
	 */

	private static ArrayList<String> getAllDates(List<Aula> aulas) {
		Set<String> uniqueDates = new HashSet<String>();

		for (Aula a : aulas) {
			uniqueDates.add(a.getDia());
		}

		uniqueDates.remove("");
		ArrayList<String> uniqueDatesArray = new ArrayList<>(uniqueDates);

		return uniqueDatesArray;
	}

	private static void adicionarCaracteristicas(List<Sala> salas, String fileName)
			throws FileNotFoundException, IOException, CsvException {

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withCSVParser(csvParser).build()) {

			csvReader = reader.readAll();

			for (int i = 0; i < csvReader.size(); i++) {
				startSlotsArray(i, salas);

				for (int j = 0; j < csvReader.get(i).length; j++) {
					if (csvReader.get(i)[j].equals("X")) {
						salas.get(i - 1).setCaracteristicas(csvReader.get(0)[j]);
					}
				}
			}
		}
		System.out.println("CSV File Size   " + csvReader.size());
	}

	public static void separarPorDia(List<Aula> aulas, List<Sala> salas, ArrayList<String> uniqueDates, String algoritmo) {

		for (String s : uniqueDates) {
			List<Aula> aulasDesseDia = new ArrayList<>();

			System.out.println(s);

			for (int i = 0; i < aulas.size(); i++) {
				if (aulas.get(i).getDia().equals(s))
					aulasDesseDia.add(aulas.get(i));

			}
			preencherAulasComSalaAtribuida(aulasDesseDia, salas);
			atribuirSalas(aulasDesseDia, salas, algoritmo);
		}
	}

	private static void contarAulasComSalasAtribuidas(List<Aula> aulas) {
		int counti = 0;
		for (Aula aula : aulas) {
			if (!aula.getSalaAtribuida().isBlank()) {
				counti++;
			}
		}
		System.out.println("Numero de aulas com salas atribuidas " + counti);
		System.out.println("Numero de aulas total " + aulas.size());
	}

	private static void atribuirSalas(List<Aula> aulasDesseDia, List<Sala> salas, String algoritmo) {
		for (Sala sala : salas) {
			for (Aula aula : aulasDesseDia) {
				if (!aula.getSalaAtribuida().isBlank()) {
					continue;
				}
				int slotIndex = sala.getSlotIndex(aula.getInicio());
				int finalSlotindex = sala.getSlotIndex(aula.getFim());

				if (slotIndex > -1) {
					if (!sala.isTimeSlotUsed(slotIndex, finalSlotindex)) {

						switch(algoritmo) {
						case "sextaESabado":
							algoritmoSextaSabado(aula, sala, slotIndex, finalSlotindex);

						case "apenasCapacidade":
							algoritmoApenasCapacidade(aula, sala, slotIndex, finalSlotindex);
							
						case "comCaractECapac":
							algoritmoComCaractECapac(aula, sala, slotIndex, finalSlotindex);
						}
						
					}
				}
			}
		}
		for (Sala s : salas) {
			s.getSlotArray().clear();
			s.criarSlots();
		}
	}

	private static void startSlotsArray(int index, List<Sala> csvImporterArray) {
		if (index > 0) {
			csvImporterArray.get(index - 1).criarSlots();
		}
	}

	private static void preencherAulasComSalaAtribuida(List<Aula> aulas, List<Sala> salas) {
		for (Aula aula : aulas) {
			if (!aula.getSalaAtribuida().isBlank()) {
				for (Sala sala : salas) {
					if (sala.getNome() == aula.getSalaAtribuida()) {
						int slotIndex = sala.getSlotIndex(aula.getInicio());
						int finalSlotindex = sala.getSlotIndex(aula.getFim());
						sala.setSlotsUsed(slotIndex, finalSlotindex);
					}
				}
			}
		}
	}

	private static void algoritmoSextaSabado(Aula aulaDesseDia, Sala sala, int slotInicial, int slotFinal) {
		String diaSemana = aulaDesseDia.getDiaSemana();
		double alunosExtra = sala.getCapacidadeNormal() * 0.05;
		
		if (diaSemana.equals("Sex") || diaSemana.equals("Sáb")){
			alunosExtra = sala.getCapacidadeNormal() + sala.getCapacidadeNormal() * 0.2;
		} 	
		if (sala.getCaracteristicas().contains(aulaDesseDia.getCaracteristicaPedida()) && aulaDesseDia.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {
			aulaDesseDia.setSalaAtribuida(sala.getNome());
			aulaDesseDia.setLotacao(sala.getCapacidadeNormal());
			aulaDesseDia.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());

			sala.setSlotsUsed(slotInicial, slotFinal);

		}
	}
	
	private static void algoritmoComCaractECapac(Aula aulaDesseDia, Sala sala, int slotInicial, int slotFinal) {
		double alunosExtra = sala.getCapacidadeNormal() * 0.05;
		if (sala.getCaracteristicas().contains(aulaDesseDia.getCaracteristicaPedida()) && aulaDesseDia.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {
			aulaDesseDia.setSalaAtribuida(sala.getNome());
			aulaDesseDia.setLotacao(sala.getCapacidadeNormal());
			aulaDesseDia.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());

			sala.setSlotsUsed(slotInicial, slotFinal);
		}
	}
	
	//apenas tem em conta a capacidade da sala (sem overfit)
	private static void algoritmoApenasCapacidade(Aula aulaDesseDia, Sala sala, int slotInicial, int slotFinal) {
		double alunosExtra = sala.getCapacidadeNormal() * 0.05;
		if(aulaDesseDia.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {
			aulaDesseDia.setSalaAtribuida(sala.getNome());
			aulaDesseDia.setLotacao(sala.getCapacidadeNormal());
			aulaDesseDia.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());

			sala.setSlotsUsed(slotInicial, slotFinal);
		}
	}

}