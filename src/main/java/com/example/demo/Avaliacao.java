package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Avaliacao {
	
	private List<Aula> aulas;
	private List<Sala> salas;
	
	private int numAulasComSalaAtribuida;
	private int numTrocasEdificio;
	private int numTrocasSala;
	private int numSalasAtribComCaracPedida;
	private int algoritmo;
	private int NUM_METRICAS = 5;

	 
	Avaliacao(List<Aula> aulas, List<Sala> salas, String algoritmo) {
		this.aulas = aulas;
		this.salas = salas;
		this.numAulasComSalaAtribuida = calcularNumAulasComSalaAtribuida();
		this.numTrocasEdificio = calcularNumTrocasEdificio();
		this.numTrocasSala = calcularNumTrocasSala();
		this.numSalasAtribComCaracPedida = calcularNumSalasAtribComCaracPedida();
		
		if(algoritmo=="FIFO")
			this.algoritmo=0;
		if(algoritmo=="LIFO")
			this.algoritmo=1;
		if(algoritmo=="RANDOM")
			this.algoritmo=2;
		if(algoritmo=="LOWERCAPACITYFIRST")
			this.algoritmo=3;
		
	}
	

	private int calcularNumAulasComSalaAtribuida() {
		int count = 0;
		for (Aula aula : aulas) {
			if (!aula.getSalaAtribuida().isBlank()) {
				count++;
			}
		}
		return count;
	}
	

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
	
	
	private int compararSalas(List<Aula> aulasDaTurma) {
		int count = 0;
		for (int i = 0; i != aulasDaTurma.size() - 1; i++) {
			if (!aulasDaTurma.get(i).getSalaAtribuida().equals(aulasDaTurma.get(i + 1).getSalaAtribuida())) {
				count++;
			}
		}
		return count;
	}
	

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
	

	private int calcularNumSalasAtribComCaracPedida() {
		int count = 0;
		for (Aula aula : aulas) {
			if(aula.getCaracteristicasReaisDaSala().contains(aula.getCaracteristicaPedida()) && !aula.getCaracteristicaPedida().isBlank()) {
				count++;
			}
		}
		return count;
	}
	

	public int[] getAvaliacao() {
		int[] resultados = new int[NUM_METRICAS+1];
		resultados[0] = numAulasComSalaAtribuida;
		resultados[1] = numTrocasEdificio;
		resultados[2] = numTrocasSala;
		resultados[3] = numSalasAtribComCaracPedida;
		resultados[4] = aulas.size();
		resultados[5] = algoritmo;
		return resultados;
	}
}
