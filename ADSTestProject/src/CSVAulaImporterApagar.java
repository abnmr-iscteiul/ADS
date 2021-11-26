import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVAulaImporterApagar {

	public static void main(String[] args) throws IOException, CsvException {
		String fileName = "D:\\ADS\\ADS - Salas.csv";

		//File csvToShow = modifyFile(fileName);

		List<Aula> beans = new CsvToBeanBuilder<Aula>(new FileReader(fileName))
				.withSkipLines(1)
				.withSeparator(';')
				.withType(Aula.class)
				.build()
				.parse();

		
		
		for (int i = 0; i < beans.size(); i++) {
			
				//System.out.println(beans.get(i));
		}
		
		separateByWeekday(beans);
		System.out.println(beans.size());
		
		System.out.println(beans.get(15).printToCSV());

		
	}
	
	public static void separateByWeekday(List<Aula> beans) {
		String[] weekdays = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
		
		List<Aula> segundaArrya = new ArrayList<>();
		
		for (int i = 0; i < beans.size(); i++) {
			if (beans.get(i).getDiaSemana().equals("Seg"))
				segundaArrya.add(beans.get(i));
				
		}
		
		for (int i = 0; i < segundaArrya.size(); i++) {
			//System.out.println(segundaArrya.get(i));
			
		}
		System.out.println(segundaArrya.size());
	}
	

	public static File modifyFile(String filePath) throws IOException, CsvException {
		
		File tempCSV = File.createTempFile("tempCSV", ".csv");
		System.out.println(tempCSV.getAbsolutePath());
		tempCSV.deleteOnExit();
		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(csvParser) 
				.build()) {
			
			List<String[]> r = reader.readAll();
			//CSVWriter writer = new CSVWriter(new FileWriter(filePath));
			CSVWriter writer = new CSVWriter(new FileWriter(tempCSV.getAbsolutePath()), ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

			// r.forEach(x -> System.out.println(Arrays.toString(x)));
			for (int i = 0; i < r.size(); i++) {
				for (int j = 0; j < r.get(i).length; j++) {
					if (r.get(i)[j].equals("")) {
						r.get(i)[j] = "false";

					} else if (r.get(i)[j].equals("X")) {
						r.get(i)[j] = "true";
					}
				}
				//System.out.println(Arrays.toString(r.get(i)));
				writer.writeNext(r.get(i));


			}
			//r.forEach(x -> System.out.println(Arrays.toString(x)));
			writer.close();
		}
		return tempCSV;

		
		
	}



}