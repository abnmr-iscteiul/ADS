import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter {
	
	public static void main(String[] args) throws IOException, CsvException {
		//String fileName = "C:\\Users\\Chainz\\Desktop\\ads.csv";
		String fileName = "/Users/chainz/Desktop/ADS/ads.csv";

		List<Sala> salas = criarListaSalas(fileName);

		for (int i = 0; i < salas.size(); i++) {
			System.out.println(salas.get(i).toString());
		}
		
	}

	public static List<Sala> criarListaSalas(String filePath) throws IOException, CsvException {
		
		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
		CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(csvParser).build();
			
		List<String[]> csv = reader.readAll();
		List<Sala> listaSalas = new ArrayList<Sala>();

		for (int linha = 1; linha < csv.size(); linha++) {
			List<CaracteristicaSala> listaCaracteristicas = new ArrayList<CaracteristicaSala>();
			
			for (int coluna = 5; coluna < csv.get(linha).length; coluna++) {
				
				if (csv.get(linha)[coluna].equals("X")) {
					CaracteristicaSala caractSala = new CaracteristicaSala(csv.get(0)[coluna]);
					listaCaracteristicas.add(caractSala);
				}
			}
			
			Sala sala = new Sala(csv.get(linha)[0], csv.get(linha)[1], Integer.parseInt(csv.get(linha)[2]), Integer.parseInt(csv.get(linha)[3]), Integer.parseInt(csv.get(linha)[4]), listaCaracteristicas);
			listaSalas.add(sala);
		}
		return listaSalas;
	}
	
}