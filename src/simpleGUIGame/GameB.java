package simpleGUIGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GameB extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private int rows, cols, winSeq, playersNum;
    private List<String> namesList;
    private JPanel boardPanel;
    private JFrame infoFrame;
    JLabel nextPlayerLabel;
    private JScrollPane boardScrollPane;
    private List<JButton> buttonList;

    private int curPlayerNum;




    public GameB(InputScreen inputScreen) {
        super("Proffesional X MiX DriX");
        getBoardStats(inputScreen);
        createBoardPanel();
        createBoardScrollPanel();
        add(boardScrollPane,BorderLayout.CENTER);
        setBounds(0,0,(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-350,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addInfo();



    }

    private void addInfo() {
        curPlayerNum = 1;
        infoFrame = new JFrame();
        infoFrame.setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-275,0,275, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-30);
//        infoFrame.getContentPane().setBackground(Color.blue);
        nextPlayerLabel = new JLabel("<html> Next Player:<br/> "+ namesList.get(0)+"</html>"); nextPlayerLabel.setBounds(10,10, 255, 90); nextPlayerLabel.setFont(new Font("Serif", Font.CENTER_BASELINE, 20));
        nextPlayerLabel.setBackground(new Color(1,150,254));nextPlayerLabel.setOpaque(true);infoFrame.add(nextPlayerLabel);
        JLabel winSeqLabel = new JLabel("winning sequence: "+ winSeq); winSeqLabel.setBounds(60,100, 155, 50); infoFrame.add(winSeqLabel);
        for (int i =0 ; i<playersNum ; i++){
            JLabel l = new JLabel(" Player "+ (i+1) + ":   " + namesList.get(i)); l.setBounds(20,150+30*i, 235, 30); l.setOpaque(true);
            l.setBackground(new Color(1+60*i,(150+120*i)%256,254- 60*i)); infoFrame.add(l);
        }
        JButton b1 = new JButton("MainSimpleGUIGame Menu"); b1.setBounds(70,400,135, 60);  infoFrame.add(b1); b1.addActionListener(this);
        JButton b2 = new JButton("Restart Game"); b2.setBounds(70,500,135, 60);  infoFrame.add(b2);b2.addActionListener(this); b2.setName("b2");
        JButton b3 = new JButton("EXIT"); b3.setBounds(70,600,135, 60);  infoFrame.add(b3);b3.addActionListener(this); b3.setName("b3");


        infoFrame.setLayout(null);
        infoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        infoFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        infoFrame.setUndecorated(true);
        infoFrame.setVisible(true);
    }

    private void createBoardScrollPanel() {
        boardScrollPane = new JScrollPane();
        boardScrollPane.setViewportView(boardPanel);

    }

    private void createBoardPanel() {
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(50*cols, 50*rows));
        boardPanel.setBackground(Color.cyan);
        boardPanel.setLayout(null);
        addButtons();
    }
    private void restartBoardPanel() {
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(50*cols, 50*rows));
        boardPanel.setBackground(Color.cyan);
        boardPanel.setLayout(null);
        for (JButton b : buttonList){
            b.setBackground(null);
            b.setText("");
        }
    }

    private void addButtons() {
        buttonList = new ArrayList<JButton>();
        for (int row = 0 ; row<rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton b = new JButton();
                b.setBounds(50*col, 50*row, 50,50);
                b.addActionListener(this);
                buttonList.add(b);
                boardPanel.add(b);
            }
        }
    }

    private void getBoardStats(InputScreen inputScreen) {
        rows = inputScreen.getRowsNum();
        cols = inputScreen.getColsNum();
        winSeq = inputScreen.getWinSeqNum();
        playersNum = inputScreen.getPlayersNum();
        namesList = inputScreen.getNamesList();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressed = (JButton) e.getSource();
        if(pressed.getText().equals("MainSimpleGUIGame Menu")){
            this.dispose();
            infoFrame.dispose();
            InputScreen newInputScreen = new InputScreen();
            while (! newInputScreen.isInitiationReady()) {
                System.out.println("");
            }
            GameB b = new GameB(newInputScreen); //
            return;
        }
        if(pressed.getText().equals("Restart Game")){
            this.dispose();

            restartBoardPanel();
            createBoardScrollPanel();
            add(boardScrollPane,BorderLayout.CENTER);
            setBounds(0,0,(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-350,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            addInfo();

            return;
        }

            pressed.setBackground(new Color(1+60*(curPlayerNum-1),(150+120*(curPlayerNum-1))%256,254- 60*(curPlayerNum-1)));
        pressed.setText(String.valueOf(curPlayerNum));
        pressed.setEnabled(false);
        if (isWin()){
            for (JButton b : buttonList) {
                b.setEnabled(false);
            }
            createWinnerMsg();
        }
        else if (isBoardFull()){
            for (JButton b : buttonList) {
                b.setEnabled(false);
            }
            createTieMsg();
        }
        else {
            curPlayerNum = (curPlayerNum % playersNum) + 1;
            nextPlayerLabel.setBackground(new Color(1+60*(curPlayerNum-1),(150+120*(curPlayerNum-1))%256,254- 60*(curPlayerNum-1)));
            nextPlayerLabel.setText("<html>Next Player:<br/>"+ namesList.get(curPlayerNum-1)+"</html>");
        }
    }

    private void createTieMsg() {
        JFrame tieFrame = new JFrame();
        JLabel tieLabel = new JLabel("There is no winner:");
        tieLabel.setBounds(0,0,700,200);
        tieLabel.setFont(new Font("Serif", Font.BOLD, 45));

        tieFrame.add(tieLabel);
        tieFrame.setBounds(400,300, 700,200);
        tieFrame.setVisible(true);
        System.out.println("TIE");
    }

    private boolean isBoardFull() {
        for (JButton b : buttonList) {
            if (b.isEnabled()) {
                return false;
            }
        }
        return true;
    }


    private void createWinnerMsg() {
        JFrame winFrame = new JFrame();
        JLabel winLabel = new JLabel("The Winner Is: " + namesList.get(curPlayerNum-1));
        winLabel.setBounds(0,0,700,200);
        winLabel.setFont(new Font("Serif", Font.BOLD, 40));

        winFrame.add(winLabel);
        winFrame.setBounds(400,300, 700,200);
        winFrame.setVisible(true);
    }

    private boolean isWin() {
        for (int row = 0; row<rows; row++){
            for (int col = 0; col<cols; col++){
                JButton b = getButtonAt(row,col);
                if(b.getText().equals(String.valueOf(curPlayerNum))){
                    if (row + winSeq <= rows && checkCol(row,col)){
                        System.out.println("col winner");
                        return true;
                    }
                    if (col + winSeq <= cols && checkRow(row,col)){
                        System.out.println("row winner");
                        return true;
                    }
                    if (row + winSeq <= rows && col + winSeq <= cols && checkSouthEast(row,col)) {
                        System.out.println("SouthEast winner");
                        return true;
                    }
                    if (row + winSeq <= rows && col + 1 - winSeq >= 0 && checkSouthWest(row,col)) {
                        System.out.println("SouthWest winner");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean checkCol(int row, int col) {
        for (int i = 1; i < winSeq; i++){
            if (!getButtonAt(row + i, col).getText().equals(String.valueOf(curPlayerNum))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRow(int row, int col) {
        for (int i = 1; i < winSeq; i++){
            if (!getButtonAt(row, col+i).getText().equals(String.valueOf(curPlayerNum))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSouthEast(int row, int col) {
        for (int i = 1; i < winSeq; i++){
            if (!getButtonAt(row + i, col+i).getText().equals(String.valueOf(curPlayerNum))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSouthWest(int row, int col) {
        for (int i = 1; i < winSeq; i++){
            if (!getButtonAt(row + i, col-i).getText().equals(String.valueOf(curPlayerNum))) {
                return false;
            }
        }
        return true;
    }

    private JButton getButtonAt(int row, int col) {
        return buttonList.get(row*cols + col);
    }

}
