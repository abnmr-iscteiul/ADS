import com.opencsv.bean.CsvBindByPosition;

public class Horario {

	@CsvBindByPosition(position = 1)
	private String unidadeCurricular;
	
	@CsvBindByPosition(position = 4)
	private int numeroInscritos;
	
	@CsvBindByPosition(position = 7)
	private String diaDaSemana;
	
	@CsvBindByPosition(position = 8)
	private String inicio;
	
	@CsvBindByPosition(position = 9)
	private String fim;
	
	@CsvBindByPosition(position = 11)
	private String caracteristicaspedidas;
	
	@CsvBindByPosition(position = 12)
	private String salaAtribuida;

	@Override
	public String toString() {
		return "Horario [unidadeCurricular=" + unidadeCurricular + ", numeroInscritos=" + numeroInscritos
				+ ", diaDaSemana=" + diaDaSemana + ", inicio=" + inicio + ", fim=" + fim + ", caracteristicaspedidas="
				+ caracteristicaspedidas + ", salaAtribuida=" + salaAtribuida + "]";
	}

	public String getCaracteristicaspedidas() {
		return caracteristicaspedidas;
	}

	public void setCaracteristicaspedidas(String caracteristicaspedidas) {
		this.caracteristicaspedidas = caracteristicaspedidas;
	}
	

}