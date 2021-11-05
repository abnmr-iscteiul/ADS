import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvImporter {
	private static List<String[]> csvReader;

	public static void main(String[] args) throws IOException, CsvException {
		String fileName = "D:\\ADS\\abc2.csv";


		List<Sala> beans = new CsvToBeanBuilder<Sala>(new FileReader(fileName)).withSkipLines(1).withSeparator(';')
				.withType(Sala.class).build().parse();

		adicionarCaracteristicas(beans, fileName);

		for (int i = 0; i < beans.size(); i++) {
			System.out.println(beans.get(i).toString());
		}

	}

/*
	public static File modifyFile(String filePath) throws IOException, CsvException {

		File tempCSV = File.createTempFile("tempCSV", ".csv");
		System.out.println(tempCSV.getAbsolutePath());
		tempCSV.deleteOnExit();

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(csvParser).build()) {

			csvReader = reader.readAll();
			CSVWriter writer = new CSVWriter(new FileWriter(tempCSV.getAbsolutePath()), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			for (int i = 0; i < csvReader.size(); i++) {
				for (int j = 0; j < csvReader.get(i).length; j++) {
					if (csvReader.get(i)[j].equals("")) {
						csvReader.get(i)[j] = "false";

					} else if (csvReader.get(i)[j].equals("X")) {
						csvReader.get(i)[j] = "true";
					}
				}
				writer.writeNext(csvReader.get(i));

			}
			writer.close();
		}
		return tempCSV;
	}
*/
	
	private static void adicionarCaracteristicas(List<Sala> beans, String fileName)
			throws FileNotFoundException, IOException, CsvException {

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withCSVParser(csvParser).build()) {

			csvReader = reader.readAll();

			for (int i = 0; i < csvReader.size(); i++) {
				for (int j = 0; j < csvReader.get(i).length; j++) {
					if (csvReader.get(i)[j].equals("X")) {
						beans.get(i - 1).setCaracteristicas(csvReader.get(0)[j]);

					}
				}

			}
		}
		
		System.out.println("CSV File Size   " + csvReader.size());


	}

}