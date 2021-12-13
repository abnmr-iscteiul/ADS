package com.example.demo;
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
	private String caracteristicaPedida;

	@CsvBindByPosition(position = 12)
	private String salaAtribuida;

	@CsvBindByPosition(position = 13)
	private int lotacao;

	@CsvBindByPosition(position = 14)
	private String caracteristicasReaisDaSala;

	public String getCaracteristicaPedida() {
		return caracteristicaPedida;
	}

	public String getDiaSemana() {
		return diaDaSemana;
	}
	
	public int getDiaSemanaInt() {
		switch(diaDaSemana) {
		case "Seg":
			return 0;
		case "Ter":
			return 1;
		case "Qua":
			return 2;
		case "Qui":
			return 3;
		case "Sex":
			return 4;
		case "Sáb":
			return 5;
		}
		return 10;
	}

	public String getInicio() {
		return inicio;
	}
	
	public Double getInicioDouble() {
		String[] horaArray = inicio.split(":");
		double horaInicio = Double.parseDouble(horaArray[0]) + (Double.parseDouble(horaArray[1]) * 0.01);
		return horaInicio;
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

	public String getCaracteristicasReaisDaSala() {
		return caracteristicasReaisDaSala;
	}

	public void setCaracteristicasReaisDaSala(String caracteristicasReaisDaSala) {
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
	
	public String getTurma() {
		return turma;
	}

	public String[] printToCSV() {

		if (salaAtribuida == null)
			salaAtribuida = "";

		String[] toCSV = { curso, unidadeCurricular, turno, turma, Integer.toString(numeroInscritos),
				turnosComCapacidadeSuperior, turnosComInscricoesSuperiores,
				diaDaSemana, inicio, fim, dia, caracteristicaPedida, salaAtribuida, Integer.toString(lotacao),
				caracteristicasReaisDaSala };

		return toCSV;
	}

	@Override
	public String toString() {
		return "Aula [curso=" + curso + ", unidadeCurricular=" + unidadeCurricular + ", turno=" + turno + ", turma="
				+ turma + ", numeroInscritos=" + numeroInscritos + ", turnosComCapacidadeSuperior="
				+ turnosComCapacidadeSuperior + ", turnosComInscricoesSuperiores=" + turnosComInscricoesSuperiores
				+ ", diaDaSemana=" + diaDaSemana + ", inicio=" + inicio + ", fim=" + fim + ", dia=" + dia
				+ ", caracteristicaspedida=" + caracteristicaPedida + ", salaAtribuida=" + salaAtribuida + ", lotacao="
				+ lotacao + ", caracteristicasReaisDaSala=" + caracteristicasReaisDaSala + "]";
	}
}