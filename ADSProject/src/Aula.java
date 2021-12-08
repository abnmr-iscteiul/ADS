import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

public class Aula {

	@CsvBindByPosition(position = 0)
	private String curso;

	@CsvBindByPosition(position = 1)
	private String unidadeCurricular;

	@CsvBindByPosition(position = 2)
	private String turno;

	@CsvBindByPosition(position = 3)
	private String turma;

	@CsvBindByPosition(position = 4)
	private int numeroInscritos;

	@CsvBindByPosition(position = 5)
	private String turnosComCapacidadeSuperior;

	@CsvBindByPosition(position = 6)
	private String turnosComInscricoesSuperiores;

	@CsvBindByPosition(position = 7)
	private String diaDaSemana;

	@CsvBindByPosition(position = 8)
	private String inicio;

	@CsvBindByPosition(position = 9)
	private String fim;

	@CsvBindByPosition(position = 10)
	private String dia;

	@CsvBindByPosition(position = 11)
	private String caracteristicaspedida;

	private String salaAtribuida;

	private int lotacao;

	private ArrayList<String> caracteristicasReaisDaSala;

	public String getCaracteristicaspedida() {
		return caracteristicaspedida;
	}

	public String getDiaSemana() {
		return diaDaSemana;
	}

	public String getInicio() {
		return inicio;
	}

	public int getNumeroInscritos() {
		return numeroInscritos;
	}

	public String getSalaAtribuida() {
		return salaAtribuida;
	}

	public void setSalaAtribuida(String sala) {
		salaAtribuida = sala;
	}

	public int getLotacao() {
		return lotacao;
	}

	public void setLotacao(int lotacao) {
		this.lotacao = lotacao;
	}

	public ArrayList<String> getCaracteristicasReaisDaSala() {
		return caracteristicasReaisDaSala;
	}

	public void setCaracteristicasReaisDaSala(ArrayList<String> caracteristicasReaisDaSala) {
		this.caracteristicasReaisDaSala = caracteristicasReaisDaSala;
	}

	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public String getDia() {
		return dia;
	}

	public String[] printToCSV() {
		String carReaisSalas = "";
		if (caracteristicasReaisDaSala != null) {
			
			for (int i =0; i<caracteristicasReaisDaSala.size(); i++){
				if (i == caracteristicasReaisDaSala.size()-1) {
					carReaisSalas += caracteristicasReaisDaSala.get(i);
				} else {
					carReaisSalas += caracteristicasReaisDaSala.get(i) + ",";
				}
			}
			
		} else {
			carReaisSalas = "";
		}

		if (salaAtribuida == null)
			salaAtribuida = "";

		System.out.println(carReaisSalas);

		String[] toCSV = { curso, unidadeCurricular, turno, turma, Integer.toString(numeroInscritos),
				turnosComCapacidadeSuperior, turnosComInscricoesSuperiores,
				diaDaSemana, inicio, fim, dia, caracteristicaspedida, salaAtribuida, Integer.toString(lotacao),
				carReaisSalas };

		return toCSV;
	}

	@Override
	public String toString() {
		return "Aula [curso=" + curso + ", unidadeCurricular=" + unidadeCurricular + ", turno=" + turno + ", turma="
				+ turma + ", numeroInscritos=" + numeroInscritos + ", turnosComCapacidadeSuperior="
				+ turnosComCapacidadeSuperior + ", turnosComInscricoesSuperiores=" + turnosComInscricoesSuperiores
				+ ", diaDaSemana=" + diaDaSemana + ", inicio=" + inicio + ", fim=" + fim + ", dia=" + dia
				+ ", caracteristicaspedida=" + caracteristicaspedida + ", salaAtribuida=" + salaAtribuida + ", lotacao="
				+ lotacao + ", caracteristicasReaisDaSala=" + caracteristicasReaisDaSala + "]";
	}
}