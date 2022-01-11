package com.example.demo;

import java.util.ArrayList;
import java.util.List;

// Nº de salas utilizadas
// Nº de salas sempre livres num dia

/**
 * @author chainz
 *
 * Class que representa a avaliação de desempenho de um determinado algoritmo, 
 * de acordo com um conjunto de metricas
 */
public class Avaliacao {
	
	private List<Aula> aulas;
	private List<Sala> salas;
	
	private int numAulasComSalaAtribuida;
	private int numTrocasEdificio;
	private int numTrocasSala;
	private int numSalasAtribComCaracPedida;
	private int numSalasNaoUtilizadas;
	private int numSalasLivresTodoDia;
	private int algoritmo;
	private int NUM_METRICAS = 7;

	 
	Avaliacao(List<Aula> aulas, List<Sala> salas, String algoritmo) {
		this.aulas = aulas;
		this.salas = salas;
		this.numAulasComSalaAtribuida = calcularNumAulasComSalaAtribuida();
		this.numTrocasEdificio = calcularNumTrocasEdificio();
		this.numTrocasSala = calcularNumTrocasSala();
		this.numSalasAtribComCaracPedida = calcularNumSalasAtribComCaracPedida();
		this.numSalasNaoUtilizadas = calcularNumSalasNaoUtilizadas();
		this.numSalasLivresTodoDia = calcularNumSalasLivresTodoDia();
		
		if(algoritmo=="FIFO")
			this.algoritmo=0;
		if(algoritmo=="LIFO")
			this.algoritmo=1;
		if(algoritmo=="RANDOM")
			this.algoritmo=2;
		if(algoritmo=="LOWERCAPACITYFIRST")
			this.algoritmo=3;
		if(algoritmo=="LESSCARACTFIRST")
			this.algoritmo=4;
	}
	

	/**
	 * Calcula a métrica que consiste no número de aulas às quais foi atribuida uma sala
	 * 
	 * @return númerod de aulas com sala atribuida
	 */
	private int calcularNumAulasComSalaAtribuida() {
		int count = 0;
		for (Aula aula : aulas) {
			if (!aula.getSalaAtribuida().isBlank()) {
				count++;
			}
		}
		return count;
	}
	

	/**
	 * Calcula a métrica que consiste no número de vezes que é necessário trocar de edificio
	 * 
	 * @return número de trocas de edificio
	 */
	private int calcularNumTrocasEdificio() {
		ArrayList<String> uniqueDates = CsvImporter.getAllDates(aulas);
		ArrayList<String> uniqueTurmas = CsvImporter.getAllTurmas(aulas);
		int numTrocas = 0;
		
		for (String data : uniqueDates) {
			List<Aula> aulasDesseDia = new ArrayList<>();

			for (int i = 0; i < aulas.size(); i++) {
				if (aulas.get(i).getDia().equals(data))
					aulasDesseDia.add(aulas.get(i));
			}
			
			
			for(String turma : uniqueTurmas) {
				List<Aula> aulasDaTurma = new ArrayList<>();
				
				for (Aula aulaDesseDia : aulasDesseDia) {
					if (aulaDesseDia.getTurma().equals(turma))
						aulasDaTurma.add(aulaDesseDia);
				}
				if (!aulasDaTurma.isEmpty()) {
					aulasDaTurma = CsvImporter.ordenarPorHora(aulasDaTurma);
					numTrocas += compararEdificios(aulasDaTurma);
				}
			}
		}
		
		return numTrocas;
	}


	/**
	 * Calcula a métrica que consiste no número de vezes que é necessário trocar de sala
	 * 
	 * @return numero de trocas de sala
	 */
	private int calcularNumTrocasSala() {
		ArrayList<String> uniqueDates = CsvImporter.getAllDates(aulas);
		ArrayList<String> uniqueTurmas = CsvImporter.getAllTurmas(aulas);
		int numTrocas = 0;
		
		for (String data : uniqueDates) {
			List<Aula> aulasDesseDia = new ArrayList<>();

			for (int i = 0; i < aulas.size(); i++) {
				if (aulas.get(i).getDia().equals(data))
					aulasDesseDia.add(aulas.get(i));
			}
			
			
			for(String turma : uniqueTurmas) {
				List<Aula> aulasDaTurma = new ArrayList<>();
				
				for (Aula aulaDesseDia : aulasDesseDia) {
					if (aulaDesseDia.getTurma().equals(turma))
						aulasDaTurma.add(aulaDesseDia);
				}
				if (!aulasDaTurma.isEmpty()) {
					aulasDaTurma = CsvImporter.ordenarPorHora(aulasDaTurma);
					numTrocas += compararSalas(aulasDaTurma);
				}
			}
		}
		return numTrocas;
	}
	
	
	/**
	 * Compara a sala da aula atual com a sala da aula a seguir. Caso seja diferente incrementa o contador
	 * 
	 * @param aulasDaTurma
	 * @return contador
	 */
	private int compararSalas(List<Aula> aulasDaTurma) {
		int count = 0;
		for (int i = 0; i != aulasDaTurma.size() - 1; i++) {
			if (!aulasDaTurma.get(i).getSalaAtribuida().equals(aulasDaTurma.get(i + 1).getSalaAtribuida())) {
				count++;
			}
		}
		return count;
	}
	

	/**
	 * Compara o edificio da aula atual com o edificio da aula a seguir. Caso seja diferente incrementa o contador
	 * 
	 * @param aulasDaTurma
	 * @return contador
	 */
	private int compararEdificios(List<Aula> aulasDaTurma) {
		int count = 0;
		for (int i = 0; i != aulasDaTurma.size() - 1; i++) {
			if (!CsvImporter.getEdificio(salas, aulasDaTurma.get(i).getSalaAtribuida())
					.equals(CsvImporter.getEdificio(salas, aulasDaTurma.get(i + 1).getSalaAtribuida()))) {
				count++;
			}
		}
		return count;
	}
	

	/**
	 * Calcula a métrica que consiste no número de salas atribuidas que tem a caracteristica pedida
	 * 
	 * @return numero de aulas cuja sala tem a caracteristica pedida 
	 */
	private int calcularNumSalasAtribComCaracPedida() {
		int count = 0;
		for (Aula aula : aulas) {
			if(aula.getCaracteristicasReaisDaSala().contains(aula.getCaracteristicaPedida()) && !aula.getCaracteristicaPedida().isBlank()) {
				count++;
			}
		}
		return count;
	}
	
	private int calcularNumSalasNaoUtilizadas() {
		List<String> salasUtilizadas = new ArrayList<>();
		for (Aula aula : aulas) {
			if(!salasUtilizadas.contains(aula.getSalaAtribuida())) {
				salasUtilizadas.add(aula.getSalaAtribuida());
			}
		}
		return salas.size() - salasUtilizadas.size();
	}
	
	private int calcularNumSalasLivresTodoDia() {
		ArrayList<String> uniqueDates = CsvImporter.getAllDates(aulas);
		int salasLivres = 0;
		
		for (String data : uniqueDates) {
			List<Aula> aulasDesseDia = new ArrayList<>();
			List<String> salasUtilizadas = new ArrayList<>();

			for (int i = 0; i < aulas.size(); i++) {
				if (aulas.get(i).getDia().equals(data))
					aulasDesseDia.add(aulas.get(i));
			}
			
			for (Aula aula : aulasDesseDia) {
				if(!salasUtilizadas.contains(aula.getSalaAtribuida())) {
					salasUtilizadas.add(aula.getSalaAtribuida());
				}
			}
			
			salasLivres += salas.size() - salasUtilizadas.size();
		}
		
		return salasLivres;
	}

	/**
	 * Devolve o resultado da avaliação de todas as metricas
	 * 
	 * @return array com o resultado de cada metrica
	 */
	public int[] getAvaliacao() {
		int[] resultados = new int[NUM_METRICAS+1];
		resultados[0] = numAulasComSalaAtribuida;
		resultados[1] = numTrocasEdificio;
		resultados[2] = numTrocasSala;
		resultados[3] = numSalasAtribComCaracPedida;
		resultados[4] = aulas.size();
		resultados[5] = numSalasNaoUtilizadas;
		resultados[6] = numSalasLivresTodoDia;
		resultados[7] = algoritmo;
		return resultados;
	}
}
