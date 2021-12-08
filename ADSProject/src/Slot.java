
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

	public boolean isUsedTimeSlot() {
		return usedTimeSlot;
	}

	public void setUsedTimeSlot(boolean usedTimeSlot) {
		this.usedTimeSlot = usedTimeSlot;
	}
	
	

}
