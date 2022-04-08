package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Simulation implements Cloneable {

    Cell[][] cells;
    boolean[][] nextStates;
    int nRows;
    int nColumns;

//10,10
    Simulation(int nRows,int nColumns){
        this.nRows=nRows;
        this.nColumns=nColumns;
        cells=new Cell[nRows][nColumns];
        nextStates=new boolean[nRows][nColumns];
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nColumns;j++){
                cells[i][j]=new Cell(i,j);
            }
        }
    }

    public int getRows() {
        return nRows;
    }

    public int getColumns() {
        return nColumns;
    }

    public  Cell getCell(int row,int column){
        if(row==-1){
            row+=nRows;
        }else if(row==nRows){
            row=0;
        }
        if(column==-1){
            column+=nColumns;
        }else if(column==nColumns){
            column=0;
        }
        return cells[row][column];
    }

    public int countLivingNeighbors(int row,int column){
//        dr delta row
//        dc delta column
        int aliveNeighbors=0;
        for(int dr=-1;dr<=1;dr++){
            for(int dc=-1;dc<=1;dc++){
                if(dr==0 && dc==0)
                    continue;
                Cell cell=getCell(row+dr,column+dc);
                if(cell.isAlive())
                    aliveNeighbors++;
            }
        }
        return aliveNeighbors;
    }

    public void nextState(){
        for(int r =0;r<nRows;r++){
            for(int c=0;c<nColumns;c++){
                int counter=countLivingNeighbors(r,c);
                Cell cell=getCell(r,c);
                if(cell.isAlive()){
// value of boolean is returned
                    nextStates[r][c]=counter==2 || counter==3;
                }else{
                    nextStates[r][c]=counter==3;
                }
            }
        }
        for(int r =0;r<nRows;r++){
            for(int c=0;c<nColumns;c++){
                cells[r][c].alive=nextStates[r][c];
            }
        }
    }
    @Override
    public Simulation clone() throws CloneNotSupportedException{
        Simulation simulation=(Simulation)super.clone();
//
//        Cell[][] cells;
//        boolean[][] nextStates;
//        int nRows;
//        int nColumns;

        simulation.cells=new Cell[nRows][nColumns];
//        here's the copy without the objects in the array
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nColumns;j++){
                simulation.cells[i][j]=cells[i][j].clone();
            }
        }

        simulation.nextStates=new boolean[nRows][nColumns];
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nColumns;j++){
                simulation.nextStates[i][j]=nextStates[i][j];
            }
        }

        return simulation;
    }
}
