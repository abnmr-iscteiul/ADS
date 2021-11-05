import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

public class Sala {

	@CsvBindByPosition(position = 0)
	private String edificio;
	
	@CsvBindByPosition(position = 1)
	private String nome;
	
	@CsvBindByPosition(position = 2)
	private int capacidadeNormal;
	
	@CsvBindByPosition(position = 3)
	private int capacidadeExame;
	
	@CsvBindByPosition(position = 4) 
	int nCaracteristicas;
	
	//@CsvBindByPosition(position = 5)
	ArrayList<String> caracteristicas = new ArrayList<String>();

	@Override
	public String toString() {
		return "Sala [edificio=" + edificio + ", nome=" + nome + ", capacidadeNormal=" + capacidadeNormal
				+ ", capacidadeExame=" + capacidadeExame + ", nCaracteristicas=" + nCaracteristicas
				+ ", caracteristicas=" + caracteristicas + "]";
	}

	public ArrayList<String> getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String specificCaracteristicas) {
		this.caracteristicas.add(specificCaracteristicas);
	}
	
	


}
