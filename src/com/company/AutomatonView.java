package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class AutomatonView extends JComponent implements MouseListener, MouseMotionListener {

    Simulation simulation;
    final int cellWidth=10;
    final int cellHeight=10;
    final int space=10;
    AutomatonViewState state;

    AutomatonView(Simulation simulation){
        this.simulation=simulation;
        updateSizeConstraints();
        state=AutomatonViewState.NORMAL;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    static private Color hslColor(float h, float s, float l) {
        float q, p, r, g, b;

        if (s == 0) {
            r = g = b = l; // achromatic
        } else {
            q = l < 0.5 ? (l * (1 + s)) : (l + s - l * s);
            p = 2 * l - q;
            r = hue2rgb(p, q, h + 1.0f / 3);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1.0f / 3);
        }
        return new Color(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }

    static private float hue2rgb(float p, float q, float h) {
        if (h < 0) {
            h += 1;
        }

        if (h > 1) {
            h -= 1;
        }

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }
        return p;
    }

    protected void paintComponent(Graphics g) {
//        if (isOpaque()) { //paint background
//            g.setColor(getBackground());
//            g.fillRect(0, 0, getWidth(), getHeight());
//        }
        if(simulation==null){
            return;
        }
            int rowCount=simulation.getRows();
            int columnCount=simulation.getColumns();
            g.setColor(Color.BLACK);
            g.fillRect(0,0,columnCount*cellHeight,rowCount*cellWidth);
            float hue;
            for(int i=0;i<rowCount;i++){
                for(int j=0;j<columnCount;j++){
                    Cell cell=simulation.getCell(i,j);
                    if(cell.isAlive()){
                        if(state==AutomatonViewState.NORMAL){
                            hue=(float)j/columnCount;
//                            hslColor(hue,1.0f,0.5f);
                            g.setColor(   hslColor(hue,1.0f,0.5f));
                        }else{
                            g.setColor(Color.green);
                        }
                        g.fillRect(j*cellWidth,i*cellHeight,cellWidth,cellHeight);
                    }
//                        g2d.setColor(Color.MAGENTA);
                }
            }
//            Graphics2D g2d = (Graphics2D)g.create();
//            g2d.setColor(Color.BLACK);
//            g2d.fillRect(0,0,getWidth(),getHeight());
//            g2d.setColor(Color.cyan);
//            g2d.fillRect(100,100,50,50);
//            g2d.dispose(); //clean up
    }

    public void setSimulation(Simulation simulation){
        this.simulation=simulation;
        updateSizeConstraints();
        repaint();
    }

    public void updateSizeConstraints(){

        if(simulation!=null){
//            because of the rows
            int  height=simulation.getRows()*cellHeight;
            int width=simulation.getColumns()*cellWidth;
            setMinimumSize(new Dimension(width,height));
            setPreferredSize(new Dimension(width,height));
            setMaximumSize(new Dimension(width,height));
        }else{
            setMinimumSize(new Dimension(100,100));
            setPreferredSize(new Dimension(100,100));
            setMaximumSize(new Dimension(100,100));
        }
    }
    public void setState(AutomatonViewState state){
        this.state=state;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        handleMousePressedAndDragged(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(state==AutomatonViewState.EDIT){
            handleMousePressedAndDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void handleMousePressedAndDragged(MouseEvent e){
        if(state==AutomatonViewState.EDIT) {
            double x = e.getPoint().getX();
            double y = e.getPoint().getY();
            int row = (int) (y / cellWidth);
            int column = (int) (x / cellHeight);
            if(row>=0 && row<simulation.getRows() && column>=0 && column<simulation.getRows()){
                Cell cell=simulation.cells[row][column];
//            anEvent.getButton() == MouseEvent.BUTTON1
                if(e.getButton()==MouseEvent.BUTTON1){
                    cell.alive=true;
                }else if(e.getButton()==MouseEvent.BUTTON3){
                    cell.alive=false;
                }
                repaint();
            }
        }
    }

    public AutomatonViewState getState() {
        return state;
    }
}

