package com.example.demo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

/**
 * @author chainz
 *
 *         Representa um objeto do tipo Sala. Este é criado recorrendo ao
 *         opencsv, que extrai informação de um ficheiro csv e carrega nos
 *         atributos da class, de acordo com a posição em que se encontram no
 *         ficheiro.
 */
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
	private int nCaracteristicas;

	ArrayList<String> caracteristicas = new ArrayList<String>();

	ArrayList<Slot> slots = new ArrayList<>();

	static String timeSlots[] = { "08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00",
			"11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00",
			"16:00:00", "16:30:00", "17:00:00", "17:30:00", "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00",
			"20:30:00", "21:00:00", "21:30:00", "22:00:00", "22:30:00", "23:00:00" };

	@Override
	public String toString() {
		return "Sala [edificio=" + edificio + ", nome=" + nome + ", capacidadeNormal=" + capacidadeNormal
				+ ", capacidadeExame=" + capacidadeExame + ", nCaracteristicas=" + nCaracteristicas
				+ ", caracteristicas=" + caracteristicas + "           " + slots.size() + "]";
	}

	/**
	 * @return caracteristicas da sala, num formato de ArrayList de Strings
	 */
	public ArrayList<String> getCaracteristicas() {
		return caracteristicas;
	}

	/**
	 * @return caracteristicas da sala, num formato de String
	 */
	public String getCaracteristicasInString() {
		String caracteristicasStr = "";
		if (!caracteristicas.isEmpty()) {
			for (int i = 0; i < caracteristicas.size(); i++) {
				if (i == caracteristicas.size() - 1) {
					caracteristicasStr += caracteristicas.get(i);
				} else {
					caracteristicasStr += caracteristicas.get(i) + ",";
				}
			}

		} else {
			caracteristicasStr = "";
		}

		return caracteristicasStr;
	}

	/**
	 * Adiciona características à sala
	 * 
	 * @param specificCaracteristicas - caracteristicas a adicionar
	 */
	public void setCaracteristicas(String specificCaracteristicas) {
		this.caracteristicas.add(specificCaracteristicas);
	}

	/**
	 * @return capacidade normal da sala
	 */
	public int getCapacidadeNormal() {
		return capacidadeNormal;
	}

	/**
	 * @return array de slots da sala
	 */
	public ArrayList<Slot> getSlotArray() {
		return slots;
	}

	public String getNome() {
		return nome;
	}

	public void criarSlots() {
		for (String s : timeSlots) {
			this.slots.add(new Slot(s, false));
		}
	}

	public int getSlotIndex(String time) {
		for (Slot s : slots) {
			if (time.equals(s.getTimeSlot()) && !s.isUsedTimeSlot()) {
				return slots.indexOf(s);
			}
		}
		return -1;

	}

	public int getNextSlotIndex(String time) {
		for (int i = 0; i < slots.size(); i++) {
			if (time.equals(slots.get(i).getTimeSlot())) {
				return i + 1;
			}
		}
		return -1;

	}

	/**
	 * Indica se o time slot em questão está preenchido (a ser utilizado) ou não
	 * 
	 * @param index
	 * @param finalIndex
	 * @return true se estiver usado, false caso contrario
	 */
	public boolean isTimeSlotUsed(int index, int finalIndex) {
		for (int i = index; i <= finalIndex; i++) {
			if (slots.get(i).isUsedTimeSlot())
				return true;
		}
		return false;
	}

	/**
	 * Marca o slot como ocupado
	 * 
	 * @param firstslotIndex - hora de inicio
	 * @param finalSlotIndex - hora de fim
	 */
	public void setSlotsUsed(int firstslotIndex, int finalSlotIndex) {
		for (int i = firstslotIndex; i <= finalSlotIndex; i++)
			slots.get(i).setUsedTimeSlot(true);
	}

	/**
	 * Indica se o slot correspondente a uma determinada hora se encontra disponivel
	 * ou não
	 * 
	 * @param time
	 * @return true se o slot estiver disponivel, falso caso contrario
	 */
	public boolean isSlotAvailable(String time) {
		for (Slot s : slots) {
			if (time.equals(s.getTimeSlot()) && !s.isUsedTimeSlot()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return nome do edificio da sala
	 */
	public String getEdificio() {
		return edificio;
	}

	/**
	 * @return número de caracteristicas que a sala tem
	 */
	public int getnCaracteristicas() {
		return nCaracteristicas;
	}

}
