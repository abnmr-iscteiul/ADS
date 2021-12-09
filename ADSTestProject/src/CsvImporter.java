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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class CsvImporter {
	private static List<String[]> csvReader;
	private static ArrayList<String> uniqueDates = new ArrayList<String>();

	public static void main(String[] args) throws IOException, CsvException {
		String fileName = "D:\\ADS\\abc10.csv";
		String fileNameAulas = "D:\\ADS\\ADS - Salas.csv";

		List<Sala> salas = new CsvToBeanBuilder<Sala>(new FileReader(fileName)).withSkipLines(1).withSeparator(';')
				.withType(Sala.class).build().parse();

		List<Aula> aulas = new CsvToBeanBuilder<Aula>(new FileReader(fileNameAulas)).withSkipLines(1).withSeparator(';')
				.withType(Aula.class).build().parse();

		adicionarCaracteristicas(salas, fileName);

		uniqueDates = getAllDates(aulas);

		separateByWeekday(aulas, salas, uniqueDates);

		printFinalCSV(fileNameAulas, aulas);

	}

	private static void printFinalCSV(String original, List<Aula> aulas) throws IOException, CsvException {
		// String STRING_ARRAY_SAMPLE = "D:ADS/string-array-sample.csv";
		File csvOutputFile = new File("D:ADS/finalCSVFile.csv");

		String[] csvHeader;

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(original)).withCSVParser(csvParser).build()) {

			csvReader = reader.readAll();
			csvHeader = csvReader.get(0);

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

	private static void adicionarCaracteristicas(List<Sala> beans, String fileName)
			throws FileNotFoundException, IOException, CsvException {

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withCSVParser(csvParser).build()) {

			csvReader = reader.readAll();

			for (int i = 0; i < csvReader.size(); i++) {
				startHorarioArray(i, beans);
				for (int j = 0; j < csvReader.get(i).length; j++) {
					if (csvReader.get(i)[j].equals("X")) {
						beans.get(i - 1).setCaracteristicas(csvReader.get(0)[j]);

					}
				}

			}
		}
		System.out.println("CSV File Size   " + csvReader.size());

	}

	public static void separateByWeekday(List<Aula> aulas, List<Sala> salas, ArrayList<String> uniqueDates) {

		for (String s : uniqueDates) {
			List<Aula> dayArray = new ArrayList<>();

			System.out.println(s);

			for (int i = 0; i < aulas.size(); i++) {
				if (aulas.get(i).getDia().equals(s))
					dayArray.add(aulas.get(i));

			}
			System.out.println();
			atribuirSalas(dayArray, salas, 10);

		}
		int counti = 0;
		for (int i = 0; i < aulas.size(); i++) {
			if (aulas.get(i).getSalaAtribuida() != null) {
				counti++;
				// System.out.println(aulas.get(i).getSalaAtribuida().toString());

			}
		}
		System.out.println("Numero de aulas com salas atribuidas " + counti);
		System.out.println("Numero de aulas total " + aulas.size());

	}

	private static void atribuirSalas(List<Aula> weekdayArray, List<Sala> salas, int alunosExtra) {
		int count = 0;
		for (Sala sala : salas) {

			for (int i = 0; i < weekdayArray.size(); i++) {

				int slotIndex = sala.getSlotIndex(weekdayArray.get(i).getInicio());
				int finalSlotindex = sala.getSlotIndex(weekdayArray.get(i).getFim());

				if (slotIndex > -1) {
					if (!sala.isTimeSlotUsed(slotIndex, finalSlotindex)) {
						if (sala.getCaracteristicas().contains(weekdayArray.get(i).getCaracteristicaspedida())
								&& weekdayArray.get(i)
										.getNumeroInscritos() < (sala.getCapacidadeNormal() + alunosExtra)) {

							count++;
							weekdayArray.get(i).setSalaAtribuida(sala.getNome());
							weekdayArray.get(i).setLotacao(sala.getCapacidadeNormal());
							weekdayArray.get(i).setCaracteristicasReaisDaSala(sala.getCaracteristicas());

							sala.setSlotsUsed(slotIndex, finalSlotindex);
							weekdayArray.remove(weekdayArray.get(i));
							i--;

						}
					}
				}
			}

		}
		for (Sala s : salas) {
			s.getSlotArray().clear();
			s.fillHorario();

		}
		System.out.println("Numero de vezes que a atribuição de salas acontece  " + count);

	}

	private static void startHorarioArray(int index, List<Sala> csvImporterArray) {
		if (index > 0)
			csvImporterArray.get(index - 1).fillHorario();
	}

}