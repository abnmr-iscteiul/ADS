import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.bean.CsvToBeanBuilder;


import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvImporter {

    public static void main(String[] args) throws IOException, CsvException {

        //String fileName = "/Users/chainz/Desktop/ADS - Caracterizacao das salas.csv";
    	String fileName = "C:/ADS-Caracterizacao Salas.csv";
    	/*
    	//CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
    	@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Sala> beans = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(Sala.class)
                .build()
                .parse();

        beans.forEach(x -> System.out.println(x));
        */

        
        
        
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
        try(CSVReader reader = new CSVReaderBuilder(
                new FileReader(fileName))
                .withCSVParser(csvParser)   // custom CSV parser
                //.withSkipLines(1)           // skip the first line, header info
                .build()){
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
        }
    }

}