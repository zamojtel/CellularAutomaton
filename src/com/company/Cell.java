package com.company;

import java.util.zip.CheckedOutputStream;

public class Cell implements Cloneable {
    int row;
    int column;
    boolean alive;
// if alive certain color
//    dead another one

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean isAlive(){
        return alive;
    }

    @Override
    public Cell clone() throws CloneNotSupportedException{
        return (Cell)super.clone();
    }

}
