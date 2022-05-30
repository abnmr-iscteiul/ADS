package com.example.demo;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.solution.integersolution.impl.DefaultIntegerSolution;

/**
 * Created by Antonio J. Nebro on 03/07/14.
 * Bi-objective problem for testing class {@link DefaultIntegerSolution )}, e.g., integer encoding.
 * Objective 1: minimizing the distance to value N
 * Objective 2: minimizing the distance to value M
 */
@SuppressWarnings("serial")
public class Timetabling extends AbstractIntegerProblem {
	List<Aula> aulas;
	
	LocalDate date = LocalDate.parse("2021-09-06");
	
	/** Constructor */
	public Timetabling(List<Aula> aulas)  {
		this.aulas=aulas;
		setNumberOfVariables(aulas.size());
		setNumberOfObjectives(3);
		setName("Timetabling");
		
		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(1);
			upperLimit.add(365*26);
		}
		setVariableBounds(lowerLimit, upperLimit);
	}
	

	/** Evaluate() method */
	@Override
    public IntegerSolution evaluate(IntegerSolution solution) { 
        int penalizar=0;
        int penalizar2=0;
        for (int i = 0; i < solution.variables().size(); i++) {
            int value = solution.variables().get(i);
            aulas.get(i).setDia(dia(value));
            aulas.get(i).setInicio(inicio(value));
            aulas.get(i).setFim(fim(value));
            int dia =  (int) Math.ceil(value/26);

            if(dia%7==0) {
                penalizar+=1;
            }
            if (dia > 263) {
                penalizar2+=1;
            }

        }
        solution.objectives()[0]=penalizar;
        solution.objectives()[1]=penalizar2;
        return solution ;
    }
	
	public String dia (int value) {
		int dia =  (int) Math.ceil(value/26);
		LocalDate date2 = date.plusDays(dia-1);
		if(date2.getDayOfMonth()<10&&date2.getMonthValue()<10)
			return "0"+date2.getDayOfMonth()+"/"+"0"+date2.getMonthValue()+"/"+date2.getYear();
		if(date2.getMonthValue()<10)
			return date2.getDayOfMonth()+"/"+"0"+date2.getMonthValue()+"/"+date2.getYear();
		if(date2.getDayOfMonth()<10)
			return "0"+date2.getDayOfMonth()+"/"+date2.getMonthValue()+"/"+date2.getYear();
		
		return date2.getDayOfMonth()+"/"+date2.getMonthValue()+"/"+date2.getYear();
	}
	public String inicio (int value) {
		int z=value;
		while(z>26)
			z-=26;
		int hours = (z*30+8*60-30) / 60; 
		int minutes = (z*30+8*60-30) % 60;
		
		if(hours<10&&minutes<10)
			return "0"+hours+":0"+minutes+":00";
		if(hours<10)
			return "0"+hours+":"+minutes+":00";
		if(minutes<10)
			return hours+":0"+minutes+":00";
		return hours+":"+minutes+":00";
	}
	
	
	public String fim (int value) {
		int z=value;
		while(z>26)
			z-=26;
		int hours = (z*30+60+8*60) / 60; 
		int minutes = (z*30+60+8*60) % 60;
		
		if(hours<10&&minutes<10)
			return "0"+hours+":0"+minutes+":00";
		if(hours<10)
			return "0"+hours+":"+minutes+":00";
		if(minutes<10)
			return hours+":0"+minutes+":00";
		return hours+":"+minutes+":00";
	}
}