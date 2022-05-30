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
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

public class CsvImporter {

	private static List<String[]> csvReader;
	private static ArrayList<String> uniqueDates = new ArrayList<String>();

	private static double[] overfitValues;
	private static boolean comCaracteristica;
	private static boolean manterSalas;
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
			double[] overfitValues1, boolean comCaracteristica1, boolean manterSala1,
			ArrayList<String> algoritmosEscolhidos1) throws IllegalStateException, IOException, CsvException {

		ArrayList<int[]> printableResults = new ArrayList<int[]>();
		overfitValues = overfitValues1;
		comCaracteristica = comCaracteristica1;
		algoritmosEscolhidos = algoritmosEscolhidos1;
		manterSalas = manterSala1;
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
			NSGAIIStudy study = new NSGAIIStudy();
			study.executar(aulas);
			for (int i = 0; i < aulas.size(); i++) {
				System.out.println(aulas.get(i).getInicio());
			}
			
			//removerDomingos(aulas);
			//removerAulasNasFerias(aulas);
			removerAulasErradas(aulas);
			
			printCSVFinal(fileNameAulas, aulas, resultado);
		}
		
		 
//		List<Aula> aulas = new CsvToBeanBuilder<Aula>(new FileReader(fileNameAulas)).withSkipLines(1)
//				.withSeparator(';').withType(Aula.class).build().parse();
//		 NSGAIIStudy study = new NSGAIIStudy();
		
		 
//		
//		 
//		 for(int j=0;j<aulas.size();j++)
//			 System.out.println(aulas.get(j).getDia()+" "+aulas.get(j).getFim()+" "+aulas.get(j).getCurso());
		return printableResults;
	}
	
	private static void removerAulasErradas(List<Aula> aulas) {
		for (int i = 0; i < aulas.size(); i++) {
			int dia = Integer.parseInt(aulas.get(i).getDia().substring(0, 2));
			int mes = Integer.parseInt(aulas.get(i).getDia().substring(3, 5));
			int ano = Integer.parseInt(aulas.get(i).getDia().substring(6, 10));
			LocalDate data = LocalDate.of(ano, mes, dia);
			
			int hora = Integer.parseInt(aulas.get(i).getInicio().substring(0, 2));
			int minuto = Integer.parseInt(aulas.get(i).getInicio().substring(3, 5));
			LocalTime tempoInicio = LocalTime.of(hora, minuto);
			String inicio = aulas.get(i).getInicio();
			String fim = aulas.get(i).getFim();
			
			
			while(dataErrada(data)) {
				long minDay = LocalDate.of(2021, 9, 6).toEpochDay();
			    long maxDay = LocalDate.of(2022, 5, 28).toEpochDay();
			    long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
			    data = LocalDate.ofEpochDay(randomDay);
			}
			horaErrada(aulas, aulas.get(i));
			
			System.out.println("saiu do while");
			String year = data.toString().substring(0, 4);
		    String month = data.toString().substring(5, 7);
		    String day = data.toString().substring(8, 10);
			aulas.get(i).setDia(day + "/"+ month + "/" + year);
			
		}
	}
	
	private static Boolean dataErrada(LocalDate data) {
		LocalDate primeiroDiaFerias = LocalDate.of(2022, 05, 28);
		if (data.getDayOfWeek() == DayOfWeek.SUNDAY || data.isAfter(primeiroDiaFerias)) {
			return true;
		}
		return false;
	}
	
	private static void horaErrada(List<Aula> aulas, Aula aula) {
			
		for (int i = 0; i < aulas.size(); i++) {
			int hora = Integer.parseInt(aula.getInicio().substring(0, 2));
			int minuto = Integer.parseInt(aula.getInicio().substring(3, 5));
			LocalTime tempoInicio = LocalTime.of(hora, minuto);
			String inicio = aula.getInicio();
			String fim = aula.getFim();
			
			int hora2 = Integer.parseInt(aulas.get(i).getInicio().substring(0, 2));
			int minuto2 = Integer.parseInt(aulas.get(i).getInicio().substring(3, 5));
			LocalTime tempoInicioOutrasAulas = LocalTime.of(hora2, minuto2);
			
			if(aulas.get(i).getCurso() == aula.getCurso()&& aula.getDia()==aulas.get(i).getDia()) {
				if (tempoInicio == tempoInicioOutrasAulas || tempoInicio == tempoInicioOutrasAulas.plus(Duration.ofMinutes(30)) || tempoInicio == tempoInicioOutrasAulas.plus(Duration.ofMinutes(60))) {
					while(tempoInicio == tempoInicioOutrasAulas || tempoInicio == tempoInicioOutrasAulas.plus(Duration.ofMinutes(30)) || tempoInicio == tempoInicioOutrasAulas.plus(Duration.ofMinutes(60))) {
						int randomInt = (int)(Math.random() * (26)) + 1;
						inicio = inicio(randomInt);
						fim = fim(randomInt);
						tempoInicio = LocalTime.of(Integer.parseInt(inicio.substring(0, 2)), Integer.parseInt(inicio.substring(3, 5)));
					}
					System.out.println(tempoInicio+"  inicio");
					System.out.println(tempoInicioOutrasAulas+ " outras");
					aula.setInicio(inicio);
					aula.setFim(fim);
					//horaErrada(aulas, aula);
				}
			}
		}
	}
	
	public static String inicio (int value) {
		int z=value;
		while(z>26)
			z-=26;
		int hours = (z*30+8*60-30) / 60; 
		int minutes = (z*30+8*60-30) % 60;
		
		if(hours<10&&minutes<10)
			return "0"+hours+":0"+minutes+":00";
		if(hours<10)
			return "0"+hours+":"+minutes+":00";
		if(minutes<10)
			return hours+":0"+minutes+":00";
		return hours+":"+minutes+":00";
	}
	
	
	public static String fim (int value) {
		int z=value;
		while(z>26)
			z-=26;
		int hours = (z*30+60+8*60) / 60; 
		int minutes = (z*30+60+8*60) % 60;
		
		if(hours<10&&minutes<10)
			return "0"+hours+":0"+minutes+":00";
		if(hours<10)
			return "0"+hours+":"+minutes+":00";
		if(minutes<10)
			return hours+":0"+minutes+":00";
		return hours+":"+minutes+":00";
	}
	
	/*
	private static void removerDomingos(List<Aula> aulas) {
		for (int i = 0; i < aulas.size(); i++) {
			int dia = Integer.parseInt(aulas.get(i).getDia().substring(0, 2));
			int mes = Integer.parseInt(aulas.get(i).getDia().substring(3, 5));
			int ano = Integer.parseInt(aulas.get(i).getDia().substring(6, 10));
			System.out.println(dia + " " + mes + " " + ano);
			LocalDate data = LocalDate.of(ano, mes, dia);
			if (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
				long minDay = LocalDate.of(2021, 9, 6).toEpochDay();
			    long maxDay = LocalDate.of(2022, 5, 28).toEpochDay();
			    long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
			    String randomDate = LocalDate.ofEpochDay(randomDay).toString();
			    String year = randomDate.substring(0, 4);
			    String month = randomDate.substring(5, 7);
			    String day = randomDate.substring(8, 10);
			    System.out.println(day + "/"+ month + "/" + year);
				aulas.get(i).setDia(day + "/"+ month + "/" + year);
				removerDomingos(aulas);
			}
		}
	}
	
	
	private static void removerAulasNasFerias(List<Aula> aulas) {
		for (int i = 0; i < aulas.size(); i++) {
			int dia = Integer.parseInt(aulas.get(i).getDia().substring(0, 2));
			int mes = Integer.parseInt(aulas.get(i).getDia().substring(3, 5));
			int ano = Integer.parseInt(aulas.get(i).getDia().substring(6, 10));
			System.out.println(dia + " " + mes + " " + ano);
			LocalDate data = LocalDate.of(ano, mes, dia);
			LocalDate primeiroDiaFerias = LocalDate.of(2022, 05, 28);
			if (data.isAfter(primeiroDiaFerias)) {
				long minDay = LocalDate.of(2021, 9, 6).toEpochDay();
			    long maxDay = LocalDate.of(2022, 5, 28).toEpochDay();
			    long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
			    String randomDate = LocalDate.ofEpochDay(randomDay).toString();
			    String year = randomDate.substring(0, 4);
			    String month = randomDate.substring(5, 7);
			    String day = randomDate.substring(8, 10);
			    System.out.println(day + "/"+ month + "/" + year);
				aulas.get(i).setDia(day + "/"+ month + "/" + year);
				removerAulasNasFerias(aulas);
			}
		}
	}
	*/


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

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Collections.sort(uniqueDatesArray,
				(s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));

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
		// System.out.println("CSV File Size " + csvReader.size());
	}

	public static String getProximaDiaProximaSemana(String date, ArrayList<String> uniqueDates) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		int indexOfDate = uniqueDates.indexOf(date);
		String dayNextWeek = "";

		if (indexOfDate < uniqueDates.size() - 7) {
			LocalDate d1 = LocalDate.parse(uniqueDates.get(indexOfDate), formatter);

			for (int j = 0; j < 8; j++) {
				LocalDate d2 = LocalDate.parse(uniqueDates.get(indexOfDate + j), formatter);
				long diffDays = Duration.between(d1.atStartOfDay(), d2.atStartOfDay()).toDays();
				if (diffDays == 7) {
					dayNextWeek = uniqueDates.get(indexOfDate + j);

					break;
				}

			}

		}

		return dayNextWeek;
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
			List<Aula> aulasDaProximaSemana = new ArrayList<>();

			String proximaSemana = getProximaDiaProximaSemana(s, uniqueDates);

			for (int i = 0; i < aulas.size(); i++) {
				if (aulas.get(i).getDia().equals(s))
					aulasDesseDia.add(aulas.get(i));

			}
			if (!proximaSemana.equals("")) {
				for (int i = 0; i < aulas.size(); i++) {
					if (aulas.get(i).getDia().equals(proximaSemana))
						aulasDaProximaSemana.add(aulas.get(i));

				}
			}

			preencherAulasComSalaAtribuida(aulasDesseDia, salas);
			atribuirSalas(aulasDesseDia, aulasDaProximaSemana, salas);

		}
	}

	/**
	 * Atribui as aulas de um determinado dia às salas
	 * 
	 * @param aulasDesseDia        - aulas de um determinado dia
	 * @param aulasDaProximaSemana
	 * @param salas                - lista de salas
	 */
	private static void atribuirSalas(List<Aula> aulasDesseDia, List<Aula> aulasDaProximaSemana, List<Sala> salas) {
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
						metodoCustomOverfitECaracteristicas(aula, aulasDaProximaSemana, sala, slotIndex, finalSlotindex,
								overfitValue, comCaracteristica, manterSalas);
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
	 * @param aulaDesseDia         - aula à qual a sala irá ser atribuida
	 * @param aulasDaProximaSemana
	 * @param sala                 - sala a ser atribuida
	 * @param slotInicial          - slot de inicio da aula
	 * @param slotFinal            - slot final da aula
	 * @param overfitValue         - valor de overfit da capacidade da sala
	 * @param comCaracteristica    - true caso a característica da sala tenha que
	 *                             corresponder, falso caso contrario
	 * @param manterSalas2
	 */
	private static void metodoCustomOverfitECaracteristicas(Aula aulaDesseDia, List<Aula> aulasDaProximaSemana,
			Sala sala, int slotInicial, int slotFinal, double overfitValue, boolean comCaracteristica,
			boolean manterSalas2) {

		double alunosExtra = sala.getCapacidadeNormal() * overfitValue;

		if (comCaracteristica) {
			if (sala.getCaracteristicas().contains(aulaDesseDia.getCaracteristicaPedida())
					&& aulaDesseDia.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {
				if (manterSalas2) {
					prencherAulasProximaSemana(aulaDesseDia, aulasDaProximaSemana, sala);
				}
				aulaDesseDia.setSalaAtribuida(sala.getNome());
				aulaDesseDia.setLotacao(sala.getCapacidadeNormal());
				aulaDesseDia.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());

				sala.setSlotsUsed(slotInicial, slotFinal);
			}
		} else {
			if (aulaDesseDia.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {
				if (manterSalas2) {
					prencherAulasProximaSemana(aulaDesseDia, aulasDaProximaSemana, sala);
				}
				aulaDesseDia.setSalaAtribuida(sala.getNome());
				aulaDesseDia.setLotacao(sala.getCapacidadeNormal());
				aulaDesseDia.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());

				sala.setSlotsUsed(slotInicial, slotFinal);
			}
		}

	}

	public static void prencherAulasProximaSemana(Aula aulaDesseDia, List<Aula> aulasProximaSemana, Sala sala) {
		for (Aula alkas : aulasProximaSemana) {
			if (alkas.getUnidadeCurricular().equals(aulaDesseDia.getUnidadeCurricular())
					&& alkas.getTurma().equals(aulaDesseDia.getTurma())
					&& alkas.getTurno().equals(aulaDesseDia.getTurno())
					&& sala.getCaracteristicas().contains(alkas.getCaracteristicaPedida())

			) {
				alkas.setSalaAtribuida(sala.getNome());
				alkas.setLotacao(sala.getCapacidadeNormal());
				alkas.setCaracteristicasReaisDaSala(sala.getCaracteristicasInString());
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
	 * @param salas - salas de aula
	 */
	private static void preencherAulasComSalaAtribuida(List<Aula> aulas, List<Sala> salas) {
		for (Aula aula : aulas) {
			if (!aula.getSalaAtribuida().isBlank()) {
				for (Sala sala : salas) {
					if (sala.getNome() == aula.getSalaAtribuida()) {
						int slotIndex = sala.getSlotIndex(aula.getInicio());

						if (slotIndex == -1) {
							slotIndex = sala.getNextSlotIndex(aula.getInicio());
						}

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
		case "LESSCARACTFIRST":
			return ordenarMenosCaracteristicasPrimeiro(salas);
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

	/**
	 * Ordena a lista de salas recebida, de forma a devolver uma nova lista com os
	 * mesmos elementos mas ordenados tendo em conta o número de caracteristicas que
	 * a sala tem. Estão ordenados de forma crescente relativamente ao numero de
	 * caracteristicas da sala , sendo o primeiro elemento da lista a Sala com menor
	 * numero de caracteristicas.
	 * 
	 * @param salas
	 * @return
	 */
	private static List<Sala> ordenarMenosCaracteristicasPrimeiro(List<Sala> salas) {
		List<Sala> salasAux = new ArrayList<Sala>(salas);
		List<Sala> salasOrdenadas = new ArrayList<Sala>();

		while (!salasAux.isEmpty()) {

			Sala aux = salasAux.get(0);
			for (Sala sala : salasAux) {
				if (sala.getnCaracteristicas() < aux.getnCaracteristicas()) {
					aux = sala;
				}
			}
			salasOrdenadas.add(aux);
			salasAux.remove(aux);
		}
		return salasOrdenadas;
	}
	
	

}
