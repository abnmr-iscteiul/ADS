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

	/**
	 * Recebe os objetos provenientes da interação entre o utilizador e a GUI. Atua
	 * como main da aplicação, executando os passos necessários para fazer a
	 * atribuição de salas de acordo com os parâmetros escolhidos pelo utilizador.
	 * 
	 * 
	 * @param fileNameAulas         - path do ficheiro CSV que contém as aulas às
	 *                              quais iram ser atribuidas salas
	 * @param fileNameSala          - path do ficheiro CSV que contém as salas a
	 *                              serem atribuidas
	 * @param path                  - path onde irá ser guardado o ficheiro final
	 * @param overfitValues1        - array de overfits, sendo que cada valor
	 *                              corresponde a um dia da semana (segunda a
	 *                              sabado)
	 * @param comCaracteristica1    - indica se, no momento de atribuir as salas, se
	 *                              deve ter em conta as caracteristicas que a sala
	 *                              necessida de ter para uma determinada aula ou
	 *                              não
	 * @param algoritmosEscolhidos1 - indica o algoritmo de ordenação escolhido
	 * @return metricas de avaliação das atribuições efetuadas
	 */
	public static ArrayList<int[]> resultado(String fileNameAulas, String fileNameSala, String path,
			double[] overfitValues1, boolean comCaracteristica1, ArrayList<String> algoritmosEscolhidos1)
			throws IllegalStateException, IOException, CsvException {

		System.out.println(comCaracteristica1);
		ArrayList<int[]> printableResults = new ArrayList<int[]>();
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

			Avaliacao avaliacao = new Avaliacao(aulas, salas, algoritmo);

			printableResults.add(avaliacao.getAvaliacao());

			printCSVFinal(fileNameAulas, aulas, resultado);
		}
		return printableResults;
	}

	/**
	 * Escreve num novo ficheiro CSV a informação original das aulas e a respetiva
	 * atribuição de salas.
	 * 
	 * @param original - ficheiro inicial, que contém a informação das aulas ainda
	 *                 por preencher
	 * @param aulas    - lista de aulas, criada a partir do CSV inicial
	 * @param path     - path onde é guardado o ficheiro CSV a ser criado
	 */
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

	/**
	 * Devolve uma lista que contém as datas em que existe pelo menos uma aula, sem
	 * repetições
	 * 
	 * @param aulas - lista de aulas, criada a partir do CSV inicial
	 * @return lista de datas, sem repetições
	 */
	public static ArrayList<String> getAllDates(List<Aula> aulas) {
		Set<String> uniqueDates = new HashSet<String>();

		for (Aula a : aulas) {
			uniqueDates.add(a.getDia());
		}

		uniqueDates.remove("");
		ArrayList<String> uniqueDatesArray = new ArrayList<>(uniqueDates);

		return uniqueDatesArray;
	}

	/**
	 * Devolve uma lista que contém o nome das turmas, sem repetições
	 * 
	 * @param aulas - lista de aulas, criada a partir do CSV inicial
	 * @return lista de turmas, sem repetições
	 */
	public static ArrayList<String> getAllTurmas(List<Aula> aulas) {
		Set<String> uniqueTurmas = new HashSet<String>();
		String[] turmasByComma;

		for (Aula a : aulas) {
			if (a.getTurma().contains(",")) {
				turmasByComma = a.getTurma().split(", ");

				for (String turmas : turmasByComma)
					uniqueTurmas.add(turmas);

			} else
				uniqueTurmas.add(a.getTurma());
		}

		uniqueTurmas.remove("");
		ArrayList<String> uniqueTurmasArray = new ArrayList<>(uniqueTurmas);

		return uniqueTurmasArray;
	}

	/**
	 * Dado o nome de uma sala, devolve o nome do edificio correspondente
	 * 
	 * @param salas    - lista de salas
	 * @param nomeSala - nome da sala
	 * @return nome do edificio onde se encontra a sala
	 */
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

	/**
	 * Ordena de forma crescente a lista recebida, pondo em primeiro lugar as aulas
	 * que começam mais cedo
	 * 
	 * @param aulas - lista de aulas
	 * @return lista de aulas, ordenada pela hora de inicio de cada aula
	 */
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

	/**
	 * Adiciona as caracteristicas de cada sala, de acordo com a informação presente
	 * no CSV
	 * 
	 * @param salas    - lista de salas
	 * @param fileName - ficheiro que contem a informação das salas
	 */
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

	/**
	 * Faz a atribuição das aulas, dia a dia
	 * 
	 * @param aulas       - lista de aulas
	 * @param salas       - lista de salas
	 * @param uniqueDates - datas com aulas
	 */
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

	/**
	 * Atribui as aulas de um determinado dia às salas
	 * 
	 * @param aulasDesseDia - aulas de um determinado dia
	 * @param salas         - lista de salas
	 */
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
					}
				}
			}
		}
		for (Sala s : salas) {
			s.getSlotArray().clear();
			s.criarSlots();
		}
	}

	/**
	 * Faz a atribuição da sala a uma determinada aula e ocupa o respetivo slot,
	 * tendo em conta o overfit escolhido pelo utilizador e tendo em conta (ou não
	 * tendo em conta) a característica da sala que está a ser atribuida.
	 * 
	 * @param aulaDesseDia      - aula à qual a sala irá ser atribuida
	 * @param sala              - sala a ser atribuida
	 * @param slotInicial       - slot de inicio da aula
	 * @param slotFinal         - slot final da aula
	 * @param overfitValue      - valor de overfit da capacidade da sala
	 * @param comCaracteristica - true caso a característica da sala tenha que
	 *                          corresponder, falso caso contrario
	 */
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

	/**
	 * Inicia os slots da sala
	 * 
	 * @param index            - indice da sala
	 * @param csvImporterArray - lista de salas
	 */
	private static void startSlotsArray(int index, List<Sala> csvImporterArray) {
		if (index > 0) {
			csvImporterArray.get(index - 1).criarSlots();
		}
	}

	/**
	 * Usado para preencher os slots das salas de aulas que tinham sido previamente
	 * atribuidas (de forma manual no ficheiro excel ou após já ter executado um
	 * algoritmo de atribuição)
	 * 
	 * @param aulas - aulas com parte das salas já atribuidas
	 * @param salas
	 */
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

	/**
	 * Escolhe e devolve uma lista, ordenada de acordo com o algoritmo escolhido.
	 * 
	 * @param salas     - lista de salas
	 * @param algoritmo - algoritmo escolhido
	 * @return lista ordenada segundo o algoritmo escolhido
	 */
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

	/**
	 * Altera a lista recebida de forma a devolver uma lista ordenada de modo a que
	 * o primeiro elemento da lista seja o elemento que foi adicionado a mesma, e
	 * assim sucessivamente.
	 * 
	 * @param salas
	 * @return lista de salas ordenada segundo LIFO
	 */
	private static List<Sala> aplicarLIFO(List<Sala> salas) {
		List<Sala> salasComNovaOrdem = new ArrayList<Sala>();
		for (int i = salas.size() - 1; i >= 0; i--) {
			salasComNovaOrdem.add(salas.get(i));
		}
		return salasComNovaOrdem;
	}

	/**
	 * Devolve a lista recebida, com os elementos baralhados aleatóriamente.
	 * 
	 * @param salas
	 * @return lista de salas com os elementos baralhados
	 */
	private static List<Sala> baralharLista(List<Sala> salas) {
		Collections.shuffle(salas);
		return salas;
	}

	/**
	 * Ordena a lista de salas recebida, de forma a devolver uma nova lista com os
	 * mesmos elementos mas ordenados tendo em conta a capacidade da Sala. Estão
	 * ordenados de forma crescente relativamente a capacidade da Sala, sendo o
	 * primeiro elemento da lista a Sala com menor capacidade.
	 * 
	 * @param salas
	 * @return lista de salas ordenada pela capacidade, de forma crescente
	 */
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