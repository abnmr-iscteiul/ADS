import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvImporter {

    public static void main(String[] args) throws IOException {

        String fileName = "/Users/chainz/Desktop/ADS - Caracterizacao das salas.csv";

        List<Sala> beans = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(Sala.class)
                .build()
                .parse();

        beans.forEach(System.out::println);
        System.out.println("Hello World");


    }

}