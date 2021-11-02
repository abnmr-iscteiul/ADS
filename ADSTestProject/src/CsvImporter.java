import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvImporter {

	public static void main(String[] args) throws IOException, CsvException {
		String fileName = "D:\\ADS\\backer.csv";

		modifyFile(fileName);

		List<Sala> beans = new CsvToBeanBuilder<Sala>(new FileReader(fileName))
				.withSkipLines(1)
				.withSeparator(';')
				.withType(Sala.class)
				.build()
				.parse();

		System.out.println(beans.size());
		for (int i = 0; i < beans.size(); i++) {
			System.out.println(beans.get(i).toString());
		}
		
	}

	public static void modifyFile(String filePath) throws IOException, CsvException {
		

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(csvParser) 
				.build()) {
			
			List<String[]> r = reader.readAll();
			//CSVWriter writer = new CSVWriter(new FileWriter(filePath));
			CSVWriter writer = new CSVWriter(new FileWriter(filePath), ';',
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

		
		
	}



}