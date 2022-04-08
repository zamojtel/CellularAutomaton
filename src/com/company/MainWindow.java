package com.company;

//public class MainWindow {
//}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.prefs.Preferences;
//osługa przycisków

public class MainWindow extends JFrame implements KeyListener {

//    final JFileChooser fc = new JFileChooser();
//    int returnVal = fc.showOpenDialog(aComponent);
    Simulation simulation;
    AutomatonView automatonView;
    JButton startSimulationButton;
    JButton finishSimulationButton;
    JButton loadButton;
    JFileChooser fc;
    Timer timer;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem loadMenuItem;
    JMenuItem startMenuItem;
    JMenuItem finishMenuItem;
    JMenuItem saveMenuItem;
    Simulation initialSimulationState;
    Preferences preferences;
    SimulationSaver simulationSaver;
    JButton newSimulationButton;
    JButton changeEditStateButton;

    MainWindow()
    {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
//        simulation=new Simulation(10,10);
//        setResizable(false);
        this.preferences=Preferences.userRoot().node("settings");
        this.addKeyListener(this);
        setLayout(new FlowLayout());
        menuBar=new JMenuBar();
        menu=new JMenu("Menu");
//        KeyEvent.VK_T)
        loadMenuItem=new JMenuItem("Load");
        startMenuItem=new JMenuItem("start");
        finishMenuItem=new JMenuItem("finish");
        saveMenuItem=new JMenuItem("save");
        menu.add(loadMenuItem);
        menu.add(startMenuItem);
        menu.add(finishMenuItem);
        menu.add(saveMenuItem);
        menuBar.add(menu);
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.CTRL_MASK));

//        menu.add(MenuItem);
        setJMenuBar(menuBar);
//        adding component to the window
        automatonView=new AutomatonView(simulation);

//        startSimulationButton=new JButton("Start");
//        loadButton=new JButton("Load");
//        add(loadButton);
        newSimulationButton=new JButton("Create new simulation");
        changeEditStateButton=new JButton("Edit");

        changeEditStateButton.addActionListener((e)->{
            changeEditState();
        });
        add(changeEditStateButton);
        changeEditStateButton.setFocusable(false);
        add(newSimulationButton);
        newSimulationButton.setFocusable(false);
        newSimulationButton.addActionListener((e)->{
            newSimulation();
        });
        this.timer= new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextState();
            }
        });
        timer.setRepeats(true); // Only execute once
        finishSimulationButton=new JButton("Finish");
        startMenuItem.addActionListener((e)->{
            timer.start();

        });

        finishMenuItem.addActionListener((e)->{
            timer.stop();
        });

        loadMenuItem.addActionListener((e)->
            {
                load();
            }
        );

        saveMenuItem.addActionListener((e)->{
            save();
        });

//        add(startSimulationButton);
//        add(finishSimulationButton);
        add(automatonView);
//        adjust window size to its current content
//        show();
        setVisible(true);
        String recentFile;
//        when recentFile != "" then we want to read it
        recentFile=preferences.get("recentFile","");
        if(recentFile.length()>0){
            try {
                load(recentFile);
            }catch (Exception e){
            }
        }
        pack();
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    //obługa strzałek

    @Override
    public void keyPressed(KeyEvent e) {
//        if(simulation.simulationStatus==false){
//        }
        if(e.getKeyCode()==KeyEvent.VK_L){
            load();
        }else if(e.getKeyCode()==KeyEvent.VK_P){
            pause();
        }else if(e.getKeyCode()==KeyEvent.VK_S){
            start();
        }else if(e.getKeyCode()==KeyEvent.VK_R){
            restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    //obsługa strzałek
    //wyświetlam komunikat

    public AutomatonView getAutomatonView() {
        return automatonView;
    }

    public void load(){
        try{
            fc=new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if(returnVal==JFileChooser.APPROVE_OPTION){
                JOptionPane.showMessageDialog(null,fc.getSelectedFile());
                load(fc.getSelectedFile().toString());
                if(simulation!=null){
                    pack();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
    public void load(String fileName) throws CloneNotSupportedException,FileNotFoundException{
        simulation=SimulationLoader.load(fileName);
        if(simulation!=null){
            automatonView.setSimulation(simulation);
            initialSimulationState=simulation.clone();
            preferences.put("recentFile",fileName);
        }else{
            System.out.println("null value returned by loader");
        }
    }

    public void nextState(){
        simulation.nextState();
        automatonView.repaint();
    }

    public void pause(){
        timer.stop();
    }

    public void start()
    {
//        getAutomatonView().setState(AutomatonViewState.NORMAL);
        if(automatonView.getState()==AutomatonViewState.EDIT){
            changeState();
        }
        timer.start();
    }

    public void finish(){
        timer.restart();
    }

    public void restart(){
        try{
            simulation=initialSimulationState.clone();
            automatonView.setSimulation(simulation);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
    public void save(){
        fc=new JFileChooser();
        fc.setDialogTitle("Specify a file to save");

        int userSelection = fc.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fc.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            simulationSaver=new SimulationSaver();
            try {
                simulationSaver.save(simulation,fc.getSelectedFile().toString());
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this,"Saving failed");
                e.printStackTrace();
            }
        }

    }

    public void newSimulation() {
        try {
            JTextField rowsField = new JTextField(5);
            JTextField columnsField = new JTextField(5);

            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Rows : "));
            myPanel.add(rowsField);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Columns : "));
            myPanel.add(columnsField);
            int rows;
            int columns;
            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                rows = Integer.valueOf(rowsField.getText());
                columns = Integer.valueOf(columnsField.getText());
                simulation = new Simulation(rows, columns);
                automatonView.setSimulation(simulation);
                initialSimulationState = simulation.clone();
                pack();
            }

        } catch (Exception e) {

        }
    }
    public void changeState(){
        AutomatonViewState state=automatonView.getState();
        if(state==AutomatonViewState.EDIT){
            changeEditStateButton.setText("Finish Editing");
            state=AutomatonViewState.NORMAL;
        }else{
            changeEditStateButton.setText("Edit");
            state=AutomatonViewState.EDIT;
        }
    }

    public void changeEditState(){

        AutomatonViewState state=automatonView.getState();

        if(state==AutomatonViewState.EDIT){
            changeEditStateButton.setText("Edit");
            automatonView.setState(AutomatonViewState.NORMAL);
        }else{
            changeEditStateButton.setText("Finish editing");
            automatonView.setState(AutomatonViewState.EDIT);
            timer.stop();
        }
    }

    public void startEdit(){

    }

    public void endEdit(){

    }
}
