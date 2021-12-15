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

	private static double[] overfitValues;
	private static boolean comCaracteristica;
	private static ArrayList<String> algoritmosEscolhidos;

	private static ArrayList<int[]> printableResults = new ArrayList<int[]>();

	// ALGORITMOS - sexta e sabado com mais alunos que lugares da sala, nao ter em
	// conta as caracteristicas, ter em conta as caracteristicas
	// --> mudar para apenas 1 metodo que é custom o overfit e que pode ter em conta
	// as caracteristicas da sala ou nao

	// LIFO, FIFO, Random (DONE)
	// algoritmo que tenta atribuir primeiro as salas com menor capacidade

	// METRICAS DE AVALIAÇAO - numero de aulas com sala atribuida,
	// trocas de edificio,
	// mudanças de sala,
	// salas disponiveis,
	// numero de salas atribuidas com a caracteristica pedida

	public static void main(String[] args) throws IOException, CsvException {
		// String fileNameAulas = "/Users/chainz/Desktop/ADS/adsAulas.csv";
		// String fileNameSala = "/Users/chainz/Desktop/ADS/adsSalas.csv";

		String fileNameAulas = "D:\\ADS\\ADS - Aulas.csv";
		String fileNameSala = "D:\\ADS\\abc10.csv";

		overfitValues = new double[] { 0.05, 0.05, 0.05, 0.05, 0.20, 0.20 };
		comCaracteristica = true;
		algoritmosEscolhidos = new ArrayList<String>();
		algoritmosEscolhidos.add("RANDOM");
		algoritmosEscolhidos.add("LIFO");

		List<Sala> salas = new CsvToBeanBuilder<Sala>(new FileReader(fileNameSala)).withSkipLines(1).withSeparator(';')
				.withType(Sala.class).build().parse();

		for (String algoritmo : algoritmosEscolhidos) {
			String resultado = "D:\\ADS\\final" + algoritmo + ".csv";

			List<Aula> aulas = new CsvToBeanBuilder<Aula>(new FileReader(fileNameAulas)).withSkipLines(1)
					.withSeparator(';').withType(Aula.class).build().parse();

			adicionarCaracteristicas(salas, fileNameSala);

			uniqueDates = getAllDates(aulas);
			salas = escolherAlgoritmoOrdenacao(salas, algoritmo);
			separarPorDiaEAtribuirSalas(aulas, salas, uniqueDates);

			Avaliacao avaliacao = new Avaliacao(aulas, salas);
//			int[] resultadosAvaliacao = avaliacao.getAvaliacao();
//			for (int i = 0; i != resultadosAvaliacao.length; i++) {
//				System.out.println(resultadosAvaliacao[i]);
//			}
			printableResults.add(avaliacao.getAvaliacao());

//			contarAulasComSalasAtribuidas(aulas);

			printCSVFinal(fileNameAulas, aulas, resultado);
		}

		for (int i = 0; i < printableResults.size(); i++) {
			System.out.println(algoritmosEscolhidos.get(i));
			for (int j = 0; j < printableResults.get(i).length; j++) {
				if (j == 0)
					System.out.println("Numero de aulas com salas atribuidas " + printableResults.get(i)[j]);
				else if (j == 4)
					System.out.println("Numero de aulas total " + printableResults.get(i)[j]);
				else
					System.out.println(printableResults.get(i)[j]);

			}
			System.out.println("========================================");
		}

	}

	public static ArrayList<int[]> resultado(String fileNameAulas, String fileNameSala, String path, double[] overfitValues1,boolean comCaracteristica1, 
			ArrayList<String> algoritmosEscolhidos1 )
			throws IllegalStateException, IOException, CsvException {
			
		
			overfitValues = overfitValues1;
			comCaracteristica = comCaracteristica1;
			algoritmosEscolhidos = algoritmosEscolhidos1;
			
			List<Sala> salas = new CsvToBeanBuilder<Sala>(new FileReader(fileNameSala)).withSkipLines(1).withSeparator(';')
					.withType(Sala.class).build().parse();
			
			
	
			for (String algoritmo : algoritmosEscolhidos) {
				String resultado = path + algoritmo + ".csv";
				System.out.println(algoritmo);
				List<Aula> aulas = new CsvToBeanBuilder<Aula>(new FileReader(fileNameAulas)).withSkipLines(1)
						.withSeparator(';').withType(Aula.class).build().parse();

				adicionarCaracteristicas(salas, fileNameSala);

				uniqueDates = getAllDates(aulas);
				salas = escolherAlgoritmoOrdenacao(salas, algoritmo);
				separarPorDiaEAtribuirSalas(aulas, salas, uniqueDates);

				Avaliacao avaliacao = new Avaliacao(aulas, salas);
//				int[] resultadosAvaliacao = avaliacao.getAvaliacao();
//				for (int i = 0; i != resultadosAvaliacao.length; i++) {
//					System.out.println(resultadosAvaliacao[i]);
//				}
				printableResults.add(avaliacao.getAvaliacao());

//				contarAulasComSalasAtribuidas(aulas);

				printCSVFinal(fileNameAulas, aulas, resultado);
			}

		return printableResults;

	}

	private static void printCSVFinal(String original, List<Aula> aulas, String path) throws IOException, CsvException {
		File csvOutputFile = new File(path);
		String[] csvHeader;

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
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

	public static ArrayList<String> getAllDates(List<Aula> aulas) {
		Set<String> uniqueDates = new HashSet<String>();

		for (Aula a : aulas) {
			uniqueDates.add(a.getDia());
		}

		uniqueDates.remove("");
		ArrayList<String> uniqueDatesArray = new ArrayList<>(uniqueDates);

		return uniqueDatesArray;
	}

	public static ArrayList<String> getAllTurmas(List<Aula> aulas) {
		Set<String> uniqueTurmas = new HashSet<String>();
		String[] turmasByComma;

		for (Aula a : aulas) {
			if (a.getSalaAtribuida().isBlank()) {
				if (a.getTurma().contains(",")) {
					turmasByComma = a.getTurma().split(", ");

					for (String turmas : turmasByComma)
						uniqueTurmas.add(turmas);

				} else
					uniqueTurmas.add(a.getTurma());
			}
		}

		uniqueTurmas.remove("");
		ArrayList<String> uniqueTurmasArray = new ArrayList<>(uniqueTurmas);

		return uniqueTurmasArray;
	}

	public static String getEdificio(List<Sala> salas, String nomeSala) {
		String edificio = "";
		for (Sala sala : salas) {
			if (sala.getNome().equals(nomeSala)) {
				edificio = sala.getEdificio();
				break;
			}
		}
		return edificio;
	}

	public static List<Aula> ordenarPorHora(List<Aula> aulas) {
		List<Aula> aulasOrdenadas = new ArrayList<Aula>();

		while (!aulas.isEmpty()) {

			Aula aux = aulas.get(0);
			for (Aula aula : aulas) {
				if (aula.getInicioDouble() < aux.getInicioDouble()) {
					aux = aula;
				}
			}
			aulasOrdenadas.add(aux);
			aulas.remove(aux);
		}

		return aulasOrdenadas;
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

	public static void separarPorDiaEAtribuirSalas(List<Aula> aulas, List<Sala> salas, ArrayList<String> uniqueDates) {

		for (String s : uniqueDates) {
			List<Aula> aulasDesseDia = new ArrayList<>();

			for (int i = 0; i < aulas.size(); i++) {
				if (aulas.get(i).getDia().equals(s))
					aulasDesseDia.add(aulas.get(i));

			}
			preencherAulasComSalaAtribuida(aulasDesseDia, salas);
			atribuirSalas(aulasDesseDia, salas);
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

	private static void atribuirSalas(List<Aula> aulasDesseDia, List<Sala> salas) {
		for (Sala sala : salas) {
			for (Aula aula : aulasDesseDia) {
				if (!aula.getSalaAtribuida().isBlank()) {
					continue;
				}
				int slotIndex = sala.getSlotIndex(aula.getInicio());
				int finalSlotindex = sala.getSlotIndex(aula.getFim());

				if (slotIndex > -1) {
					if (!sala.isTimeSlotUsed(slotIndex, finalSlotindex)) {

						double overfitValue = overfitValues[aula.getDiaSemanaInt()];
						metodoCustomOverfitECaracteristicas(aula, sala, slotIndex, finalSlotindex, overfitValue,
								comCaracteristica);

						/*
						 * switch(metodoAUsar) { case "sextaESabado": metodoSextaSabado(aula, sala,
						 * slotIndex, finalSlotindex); break; case "apenasCapacidade":
						 * metodoApenasCapacidade(aula, sala, slotIndex, finalSlotindex); break; case
						 * "comCaractECapac": metodoComCaractECapac(aula, sala, slotIndex,
						 * finalSlotindex); break; }
						 */

					}
				}
			}
		}
		for (Sala s : salas) {
			s.getSlotArray().clear();
			s.criarSlots();
		}
	}

	private static void metodoCustomOverfitECaracteristicas(Aula aulaDesseDia, Sala sala, int slotInicial,
			int slotFinal, double overfitValue, boolean comCaracteristica) {

		double alunosExtra = sala.getCapacidadeNormal() * overfitValue;

		if (comCaracteristica) {
			if (sala.getCaracteristicas().contains(aulaDesseDia.getCaracteristicaPedida())
					&& aulaDesseDia.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {
				aulaDesseDia.setSalaAtribuida(sala.getNome());
				aulaDesseDia.setLotacao(sala.getCapacidadeNormal());
				aulaDesseDia.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());

				sala.setSlotsUsed(slotInicial, slotFinal);
			}
		} else {
			if (aulaDesseDia.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {
				aulaDesseDia.setSalaAtribuida(sala.getNome());
				aulaDesseDia.setLotacao(sala.getCapacidadeNormal());
				aulaDesseDia.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());

				sala.setSlotsUsed(slotInicial, slotFinal);
			}
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

	private static List<Sala> escolherAlgoritmoOrdenacao(List<Sala> salas, String algoritmo) {
		switch (algoritmo) {
		case "FIFO":
			break;
		case "LIFO":
			return aplicarLIFO(salas);
		case "RANDOM":
			return baralharLista(salas);
		case "LOWERCAPACITYFIRST":
			return ordenarMenorCapacidadePrimeiro(salas);
		}
		return salas;
	}

	private static List<Sala> aplicarLIFO(List<Sala> salas) {
		List<Sala> salasComNovaOrdem = new ArrayList<Sala>();
		for (int i = salas.size() - 1; i >= 0; i--) {
			salasComNovaOrdem.add(salas.get(i));
		}
		return salasComNovaOrdem;
	}

	private static List<Sala> baralharLista(List<Sala> salas) {
		Collections.shuffle(salas);
		return salas;
	}

	private static List<Sala> ordenarMenorCapacidadePrimeiro(List<Sala> salas) {
		List<Sala> salasAux = new ArrayList<Sala>(salas);
		List<Sala> salasOrdenadas = new ArrayList<Sala>();

		while (!salasAux.isEmpty()) {

			Sala aux = salasAux.get(0);
			for (Sala sala : salasAux) {
				if (sala.getCapacidadeNormal() < aux.getCapacidadeNormal()) {
					aux = sala;
				}
			}
			salasOrdenadas.add(aux);
			salasAux.remove(aux);
		}
		return salasOrdenadas;
	}

}