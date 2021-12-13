package com.example.demo;
public enum Edificio {
    SEDAS_NUNES("Edificio Sedas Nunes"), ALA_AUTONOMA("Ala Autonoma"), II("Edificio II"), POLIDESPORTIVO("Polidesportivo");


	private String nome;

	private Edificio(String nome) {
		this.nome = nome;
	}


	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return nome;
	}

}