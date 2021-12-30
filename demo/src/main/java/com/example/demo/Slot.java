package com.example.demo;

/**
 * @author chainz
 *
 * Representa um slot de tempo, com duração de 30 minutos. Serve para indicar se a
 * sala está ou não disponível num determinado periodo de tempo (Slot)
 */
public class Slot {

	String timeSlot;
	boolean usedTimeSlot;

	public Slot(String time, boolean used) {
		this.timeSlot = time;
		this.usedTimeSlot = used;

	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	/**
	 * @return true caso o slot esteja a ser utilizado, false caso contrário
	 */
	public boolean isUsedTimeSlot() {
		return usedTimeSlot;
	}

	public void setUsedTimeSlot(boolean usedTimeSlot) {
		this.usedTimeSlot = usedTimeSlot;
	}
	
	

}
