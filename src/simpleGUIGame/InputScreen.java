package simpleGUIGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InputScreen implements ActionListener{
    private JFrame inputFrame;
    private JTextField tfRows, tfCols, tfWinSeq, tfPlayers;
    private JButton nextButton, startButton;
    private JTextArea taError;
    private JLabel lRows, lCols, lWinSeq, lPlayers;
    private int rowsNum, colsNum, winSeqNum, playersNum;
    private List<String> namesList;
    private Boolean initiationReady = false;


    public InputScreen(){
        inputFrame = new JFrame();
        inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tfRows = new JTextField(); tfRows.setBounds(250,50,100,20); lRows = new JLabel("NUMBER OF ROWS:"); lRows.setBounds(50,50,200,20);
        tfCols = new JTextField(); tfCols.setBounds(250,100,100,20); lCols = new JLabel("NUMBER OF COLUMNS:"); lCols.setBounds(50,100,200,20);
        tfWinSeq = new JTextField(); tfWinSeq.setBounds(250,150,100,20); lWinSeq = new JLabel("WINNING SEQUENCE"); lWinSeq.setBounds(50,150,200,20);
        tfPlayers = new JTextField(); tfPlayers.setBounds(250,200,100,20); tfPlayers.addActionListener(this);  lPlayers = new JLabel("NUMBER OF PLAYERS:"); lPlayers.setBounds(50,200,200,20);
        nextButton = new JButton("NEXT"); nextButton.setBounds(260,350,80,50); nextButton.addActionListener(this);
        taError = new JTextArea(); taError.setBounds(50,420,500,160); taError.setBackground(Color.lightGray); taError.setForeground(Color.red);

        inputFrame.add(tfRows); inputFrame.add(tfCols); inputFrame.add(tfWinSeq); inputFrame.add(tfPlayers);
        inputFrame.add(nextButton); inputFrame.add(taError); inputFrame.add(lRows); inputFrame.add(lCols); inputFrame.add(lWinSeq); inputFrame.add(lPlayers);
        inputFrame.setSize(600, 600); inputFrame.setLayout(null); inputFrame.getContentPane().setBackground(Color.lightGray); inputFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            taError.selectAll();
            taError.replaceSelection("ERRORS:\n");
            String s1 = tfRows.getText();
            String s2 = tfCols.getText();
            String s3 = tfWinSeq.getText();
            String s4 = tfPlayers.getText();
            if (!(s1.matches("[0-9]+") && s2.matches("[0-9]+") && s3.matches("[0-9]+") && s4.matches("[0-9]+"))) {
                taError.append("All boxes must contain positive integers\n");
            } else {
                Boolean goodNumbers = true;
                rowsNum = Integer.parseInt(s1);
                colsNum = Integer.parseInt(s2);
                winSeqNum = Integer.parseInt(s3);
                playersNum = Integer.parseInt(s4);
                if (rowsNum < 3 || rowsNum > 1000 || colsNum < 3 || colsNum > 1000) {
                    taError.append("Must choose board size values between 3 to 1000\n");
                    goodNumbers = false;
                }
                if (winSeqNum < 3 || (winSeqNum > rowsNum && winSeqNum > colsNum)) {
                    taError.append("Winning sequence value must be smaller than the board size and greater than 3\n");
                    goodNumbers = false;
                }
                if (playersNum < 2 || playersNum > 5) {
                    taError.append("Must choose between 2 to 5 players\n");
                    goodNumbers = false;
                }
                if (goodNumbers) {
                    changeScreenContent();
                }
            }
        }
        else if (e.getSource()==startButton){
            getPlayersInfo();
            if (namesList.contains("")){
                taError.selectAll();
                taError.replaceSelection("ERRORS:\nFill all the names");
            }
            else{
                initiationReady = true;
                inputFrame.dispose();
            }
        }
    }

    private void changeScreenContent() {
        startButton = new JButton("START"); startButton.setBounds(260,350,80,50); startButton.addActionListener(this);
        inputFrame.getContentPane().removeAll(); inputFrame.getContentPane().repaint();
        inputFrame.add(startButton);
        taError.selectAll(); taError.replaceSelection(""); inputFrame.add(taError);
        for (int i = 0; i < playersNum; i++){
            JTextField tf = new JTextField(); tf.setBounds(250,50 *(i+1),100,20); tf.setName("Player " + (i+1) + " name"); inputFrame.add(tf);
            JLabel label = new JLabel("PLAYER "+ (i+1) + " NAME:"); label.setBounds(50,50*(i+1),200,20); inputFrame.add(label);
        }
    }

    private void getPlayersInfo() {
        namesList = new ArrayList<String>();
        Component[] components = inputFrame.getContentPane().getComponents();
        for (Component component : components) {
            if (component.getClass().equals(JTextField.class)) {
                JTextField tf = (JTextField) component;
                namesList.add(tf.getText());
            }
        }
    }

    public boolean isInitiationReady(){
        return initiationReady;
    }

    public List<String> getNamesList() {
        return namesList;
    }

    public int getRowsNum() {
        return rowsNum;
    }

    public int getColsNum() {
        return colsNum;
    }

    public int getWinSeqNum() {
        return winSeqNum;
    }

    public int getPlayersNum() {
        return playersNum;
    }
}