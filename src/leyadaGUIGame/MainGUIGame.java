package leyadaGUIGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MainGUIGame menu window
 */
public class MainGUIGame extends JFrame {
    private int cellsNumX = 10; // board size
    private int cellsNumY = 10; // board size
    private int WIN_LEN = 5; // isPlayerWon size
    private int PLAYERS_NUMBER = 3;
    private String[] plnames = {"Moshe", "Moshe's friend teemo"};
    private String[] plimg = {"1", "2"};
    private int[] plc = {1, 2, 3};
    private java.awt.geom.GeneralPath gp;

    // starts new game - sets the MainMenu board, and wait for pushing on buttons
    public MainGUIGame() {
        setLayout(null);
        buildGUI();
        setListenersForTheMouse();
        setVisible(true);
    }

    //creates the MainMenu gui
    private void buildGUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenX = screenSize.width;
        int screenY = screenSize.height;
        setSize(screenX * 9 / 10, screenY * 9/10);
        setLocation(screenX / 20, screenY / 20);
        setClosingListener();
        setMainMenuButtons();
    }

    // sets the closing listener to finish the program
    private void setClosingListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // set the MainMenu buttons
    private void setMainMenuButtons() {
        JButton startBtn = setStartBtn();
        JButton optionsBtn = setOptionsBtn();
        JButton exitBtn = setExitBtn();
        resizeMainMenuButtons(Arrays.asList(startBtn, optionsBtn, exitBtn));
        this.add(startBtn);
        this.add(optionsBtn);
        this.add(exitBtn);
    }

    // set the start button
    private JButton setStartBtn() {
        JButton start = new JButton("START THE GREATEST GAME EVER");
        start.setBounds(px(35), py(15), px(20), py(5));
        start.addActionListener(e -> new GameManager(this, cellsNumX, cellsNumY, WIN_LEN, GUIBoard.createPlayersLst(PLAYERS_NUMBER)));
        return start;
    }

    // set the options button
    private JButton setOptionsBtn() {
        JButton opt = new JButton("OPTIONS");
        opt.setBounds(px(35), py(35), px(20), py(5));
        opt.setDefaultCapable(true);
        //opt.addActionListener(e -> moozic());
        opt.addActionListener(e -> new OptionsScreen(this));       //TODO build OptionsScreen
        return opt;
    }

    // set the exit button
    private JButton setExitBtn() {
        JButton ks = new JButton("EXIT");
        ks.setBounds(px(35), py(55), px(20), py(5));
        ks.setToolTipText("Text you choose");
        ks.addActionListener(e -> System.exit(0));
        return ks;
    }

    // Set the sizes and location of the Main Menu buttons
    private void resizeMainMenuButtons(List<JButton> clickies) {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                int buttonSize = 5;
                List<Integer> pyList = clickiesVerticalIndent(clickies.size(), buttonSize);
                for(int i = 0; i < clickies.size(); i++){
                    clickies.get(i).setBounds(px(35), pyList.get(i), px(30), py(buttonSize));
                    clickies.get(i).setFont(new Font(Font.DIALOG, Font.BOLD, 15));
                }
            }
            private List<Integer> clickiesVerticalIndent(int clickies, int buttonSize) {
                List<Integer> list = new ArrayList<>();
                double buttonPlace;
                int buttonArea = 80;
                double buttonSpacing = clam(1.0, buttonSize * 1.8, (double)(buttonArea / clickies));
                double buttonSpan = clickies * buttonSpacing;
                if(clickies % 2 == 0){
                    buttonPlace = (int)(50 - buttonSpan / 2);
                }
                else{
                    buttonPlace = (50 - (clickies / 2) * buttonSpacing - buttonSize + 1);
                }
                for(int i = 0; i < clickies; i++){
                    list.add(py(buttonPlace));
                    buttonPlace += buttonSpacing;
                }
                return list;// middle of the screen i guess, then i need to extend to the sides, so; minimum space between buttons, maximum space between buttons, and rest is screen size / number of buttons?
            }
        });
    }

    // set mouse listeners for the Main Menu screen, make the window size unchangeable
    private void setListenersForTheMouse() {
        MouseInputListener mouseInputListener = new MouseInputAdapter() {
            private Point clickedPos = null;

            // when there is press on the board - change the value of clickedPos.
            public void mousePressed(MouseEvent e) {
                if (gp.contains(e.getPoint()))
                    clickedPos = new Point(getWidth()-e.getX(), getHeight()-e.getY());
            }

            // After release the mouse, set clickedPos back to null
            public void mouseReleased(MouseEvent mouseEvent) {
                clickedPos = null;
            }

            public void mouseMoved(MouseEvent e) {
                if (gp.contains(e.getPoint()))
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                else
                    setCursor(Cursor.getDefaultCursor());
            }

            public void mouseDragged(MouseEvent e) {
                if (clickedPos != null) {
                    int dx = e.getX() + clickedPos.x;
                    int dy = e.getY() + clickedPos.y;
                    setSize(dx, dy);
                    repaint();
                }
            }
        };
        this.addMouseMotionListener(mouseInputListener);
        this.addMouseListener(mouseInputListener);
        this.setResizable(false);
    }

    // set the gp.
    @Override
    public void paint(Graphics g){
        super.paint(g);
        int w = getWidth();
        int h = getHeight();
        gp = new java.awt.geom.GeneralPath();
        gp.moveTo(w - 17, h);
        gp.lineTo(w, h - 17);
        gp.lineTo(w, h);
        gp.closePath();
    }

    // removes the Buttons of the main menu from the frame
    public void clear() {
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
    }

    // returns the x coordinates on the screen that is Percentage of the screen width
    private int px(double percents) {return (int)(this.getBounds().getWidth() * percents) / 100;}

    // returns the y coordinates on the screen that is Percentage of the screen height
    private int py(double percents) {
        return (int)(this.getBounds().getHeight() * percents) / 100;
    }

    // decide the size for the spacing
    public static double clam(double min, double max, double num){
        return Math.max(min, Math.min(max, num));
    }

    // Main - starts new Game
    public static void main(String[] args) {
        new MainGUIGame();

    }


    private static void moozic(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\hp250G3-00\\IdeaProjects\\TicTacToe\\src\\Not_A_Default_Package\\noc.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(99);
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
