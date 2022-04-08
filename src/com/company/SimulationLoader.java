package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SimulationLoader {
    public static Simulation load(String fileName) throws FileNotFoundException {
//        reading from a file
        try(Scanner scanner=new Scanner(new File(fileName))){
            // dimensions of the board int int
            int nRows;
            int nColumns;
            nRows=scanner.nextInt();
            nColumns=scanner.nextInt();
            Simulation simulation=new Simulation(nRows,nColumns);
            //        nRows lines
            //        . means a dead cell
            //        x means an alive cell
            //        wiszący znak nowej lini
            scanner.nextLine();
            for(int i=0;i<nRows;i++){
                String line=scanner.nextLine();
                for(int j=0;j<nColumns;j++){
                    Cell c=simulation.getCell(i,j);
                    if(line.charAt(j)=='.')
                        c.alive=false;
                    else
                        c.alive=true;
                }
            }
            return simulation;
        // while leaving try the file is closed automatically
        }catch(Exception e){
            return null;
        }
    }
}
