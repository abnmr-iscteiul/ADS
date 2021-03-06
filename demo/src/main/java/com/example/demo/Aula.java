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

	/**
	 * @return característica que a aula precisa que a sala tenha
	 */
	public String getCaracteristicaPedida() {
		return caracteristicaPedida;
	}

	/**
	 * @return dia da semana da aula
	 */
	public String getDiaSemana() {
		return diaDaSemana;
	}

	/**
	 * @return um inteiro correspondente ao dia da semana da aula
	 */
	public int getDiaSemanaInt() {
		switch (diaDaSemana) {
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
	
	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public void setCaracteristicaPedida(String caracteristicaPedida) {
		this.caracteristicaPedida = caracteristicaPedida;
	}

	/**
	 * @return hora de inicio da aula
	 */
	public String getInicio() {
		return inicio;
	}

	/**
	 * @return double de acordo com a hora de inicio da aula, para fazer ordenação
	 *         por hora de inicio
	 */
	public Double getInicioDouble() {
		String[] horaArray = inicio.split(":");
		double horaInicio = Double.parseDouble(horaArray[0]) + (Double.parseDouble(horaArray[1]) * 0.01);
		return horaInicio;
	}

	/**
	 * @return numero de inscritos na aula
	 */
	public int getNumeroInscritos() {
		return numeroInscritos;
	}
	
	
	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	/**
	 * @return sala atribuida à sala
	 */
	public String getSalaAtribuida() {
		return salaAtribuida;
	}

	/**
	 * Define a sala atribuida da aula como a sala que lhe foi atribuida
	 * 
	 * @param sala - sala a atribuir
	 */
	public void setSalaAtribuida(String sala) {
		salaAtribuida = sala;
	}

	/**
	 * @return lotação da sala atribuida
	 */
	public int getLotacao() {
		return lotacao;
	}

	/**
	 * @param lotacao - lotação da sala atribuida
	 */
	public void setLotacao(int lotacao) {
		this.lotacao = lotacao;
	}

	/**
	 * @return caracteristicas da sala atribuida
	 */
	public String getCaracteristicasReaisDaSala() {
		return caracteristicasReaisDaSala;
	}

	/**
	 * Serve para atualizar as caracteristas da sala atribuida na aula
	 * correspondente
	 * 
	 * @param caracteristicasReaisDaSala
	 */
	public void setCaracteristicasReaisDaSala(String caracteristicasReaisDaSala) {
		this.caracteristicasReaisDaSala = caracteristicasReaisDaSala;
	}

	/**
	 * @return hora de fim da aula
	 */
	public String getFim() {
		return fim;
	}

	/**
	 * @return dia da aula
	 */
	public String getDia() {
		return dia;
	}

	/**
	 * @return nome da turma da aula em questão
	 */
	public String getTurma() {
		return turma;
	}

	/**
	 * @return Array de strings a imprimir no csv, após ter sido feita a atribuição
	 */
	public String[] printToCSV() {

		if (salaAtribuida == null)
			salaAtribuida = "";

		String[] toCSV = { curso, unidadeCurricular, turno, turma, Integer.toString(numeroInscritos),
				turnosComCapacidadeSuperior, turnosComInscricoesSuperiores, diaDaSemana, inicio, fim, dia,
				caracteristicaPedida, salaAtribuida, Integer.toString(lotacao), caracteristicasReaisDaSala };

		return toCSV;
	}

	/**
	 * @return turno da aula
	 */
	public String getTurno() {
		return turno;
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

	public String getUnidadeCurricular() {
		return unidadeCurricular;
	}
}