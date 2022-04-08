package com.company;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SimulationSaver {

    SimulationSaver(){
    }

    public void save(Simulation simulation,String fileName) throws FileNotFoundException {
        try(PrintWriter printWriter=new PrintWriter(fileName)) {
            printWriter.print(simulation.getRows());
            printWriter.print(" ");
            printWriter.print(simulation.getColumns());
            printWriter.println();
            for(int i=0;i<simulation.nRows;i++){
                for(int j=0;j<simulation.nColumns;j++){
                    if(simulation.cells[i][j].isAlive()){
                        printWriter.print("x");
                    }else{
                        printWriter.print(".");
                    }
                }
                printWriter.println();
            }
        } catch (FileNotFoundException e) {
            throw e;
        }
    }
}
