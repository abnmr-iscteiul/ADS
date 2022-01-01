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
	 * Indica se o time slot está ou não a ser utilizado
	 * 
	 * @return true caso o slot esteja a ser utilizado, false caso contrário
	 */
	public boolean isUsedTimeSlot() {
		return usedTimeSlot;
	}

	
	/**
	 * Altera o estado de utilizção do time slot
	 * 
	 * @param usedTimeSlot - true, o time slot fica utilizado, false, o time slot deoxa de estar utilizado
	 */
	public void setUsedTimeSlot(boolean usedTimeSlot) {
		this.usedTimeSlot = usedTimeSlot;
	}
	
	

}
