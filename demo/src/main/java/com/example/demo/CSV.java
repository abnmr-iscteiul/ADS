package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class CSV {
    private final List<String[]> cells;
    public int rows=0;
    public int cols;
    public CSV(File file, int size) throws IOException {
    	cols=size;
        cells = new ArrayList<>();
        try (Scanner scan = new Scanner(file)) {
        	while (scan.hasNextLine()) {
            	String line = scan.nextLine();
            	String [] s = new String[size];
            	for(int i =0; i<line.split(";").length-1;i++)
            		s[i]=line.split(";")[i];
                cells.add(s);
                rows++;
            }
        }
    }

    public String get(int row, int col) {
        String[] columns = cells.get(row);
        return columns[col];
    }

    public CSV set(int row, int col, String value) {
    	if(row>=rows) {
    		 String[] columns = new String [cols];
    		 for(int i=0;i<cols;i++)
    			 columns[i]="";
    	     columns[col] = value;
    	     cells.add(columns);
    	     return this;
    	}
        String[] columns = cells.get(row);
        columns[col] = value;
        return this;
    }

    public void save(File file) throws IOException {
    	for(int i =0; i<cells.size();i++)
    		for(int j=0;j<cols;j++)
    			if(cells.get(i)[j]==null) {
    				cells.get(i)[j] = "";
    			}
    	
    	
        try (PrintWriter out = new PrintWriter(file)) {
            for (String[] row : cells) {
                for (String cell : row) {
                    if (cell != row[0]) {
                        out.print(";");
                    }
                    out.print(cell);
                }
                out.println();
            }
        }
    }
}