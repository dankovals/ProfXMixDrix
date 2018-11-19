package leyadaGUIGame;

import terminalGame.Cell;
import terminalGame.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.min;

/**
 * MainSimpleGUIGame menu window
 */
public class GameManager {
    private JFrame mainFrame;
    private JPanel gamePanel;
    private int numCellsX, numCellsY, winLen, frameX, frameY, boardSizeX, boardSizeY;
    private int CELL_SIZE = 50;
    private final int LINE_THICKNESS = 2;
    final String[] PLAYERS_NAMES = {"teemo", "jesus", "dan"};
    private Player[] players;
    private JLabel curPlayerLabel, infoLabel;
    private MouseInputAdapter miceThing;
    private GUIBoard board;
    private Thread thread;
    private Color min = new Color(0xFF00FF); // RGBmin
    private Color max = new Color(0x00FF0E); // RGBmax
    private int numCellsFill;

    //Constructor - sets the game gui, and starts a game
    GameManager(MainGUIGame frame, int x, int y, int winLen, Player[] players) {
        frameX = frame.getWidth(); frameY = frame.getHeight();
        this.mainFrame = frame; this.numCellsX = x; this.numCellsY = y; this.winLen = winLen; this.boardSizeX = x * CELL_SIZE;
        this.boardSizeY = y * CELL_SIZE; this.players = players;
        if(boardSizeX < (3 * frameX / 4 - 16) || boardSizeY < frameY - 4 * frameY / (frameY / 8) - 16) {
            CELL_SIZE = ((frameY - 4 * frameY / (frameY / 8) - 16) / y) - 1;
            this.boardSizeX = x * CELL_SIZE; // need to consider insets for scrollbars
            this.boardSizeY = y * CELL_SIZE;
        }
        numCellsFill = 0;
        board = new GUIBoard(x, y, winLen, players);
        frame.clear();      //deletes the buttons from the Main Menu
        buildGameGUI();
    }

    //builds the GUI and start running the game
    private void buildGameGUI() {
        mainFrame.add(setExitBtn());
        mainFrame.add(setRestartBtn());
        setCurPlayerLabel();
        mainFrame.add(curPlayerLabel);
        setInfoLabel();
        mainFrame.add(infoLabel);

        playTheGame();
    }

    // sets the Exit button
    private JButton setExitBtn() {
        JButton ks = new JButton("EXIT GAME");
        ks.setBounds(px(5), py(90), px(10), py(5));
        ks.addActionListener(e -> System.exit(0));
        return ks;
    }

    // sets the Restart button
    private JButton setRestartBtn() {
        JButton restartBtn = new JButton("RESTART GAME");
        restartBtn.addActionListener(e -> {
            board.reset();
            numCellsFill = 0;
            setCurPlayerLabelText();
            infoLabel.setForeground(Color.red);
            infoLabel.setText("");
            mainFrame.revalidate();
            mainFrame.repaint();
            gamePanel.removeMouseListener(miceThing);
            gamePanel.removeMouseMotionListener(miceThing);
            gamePanel.addMouseListener(miceThing);
            gamePanel.addMouseMotionListener(miceThing);
        });
        restartBtn.setBounds(45, 100, 300, 100);
        restartBtn.setFont(new Font(Font.SERIF, Font.ITALIC, 23));
        restartBtn.setForeground(Color.DARK_GRAY);
        return restartBtn;
    }

    // sets the label of the current Player
    private void setCurPlayerLabel() {
        curPlayerLabel = new JLabel("TEMP", SwingConstants.CENTER);
        curPlayerLabel.setBounds(frameX / 20, 0, 2 * frameX / 9, 100);
        curPlayerLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        setCurPlayerLabelText();
    }

    // Sets the label of the errors and winning situations
    private void setInfoLabel() {
        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setBounds(frameX / 20 + frameX / 30, frameY / 20, 2 * frameX / 9 - 2 * frameX / 30, 100);
        infoLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        infoLabel.setForeground(Color.RED);
    }

    //creates the main board, sets its listeners, and let the begin.
    private void playTheGame() {
        gamePanel = createGamePanel();
        miceThing = setMouseInputAdapter();

        gamePanel.addMouseListener(miceThing);
        gamePanel.addMouseMotionListener(miceThing);
        JScrollPane scrollFrame = setScrollPanel();
        mainFrame.add(scrollFrame);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // creates and manage the panel of the game
    private JPanel createGamePanel() {
        return new JPanel() {

            //This func paint the board while taking care of the sizes of the cell the screen and the board
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.black);
                BufferedImage img = null;
                BufferedImage cimg;
                try {
                    img = ImageIO.read(new File("src/leyadaGUIGame/tile.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int x = 0; x <= boardSizeX; x += CELL_SIZE) {
                    g.fillRect(x, 0, LINE_THICKNESS, boardSizeY + LINE_THICKNESS);
                }

                for(int y = 0; y <= boardSizeY; y += CELL_SIZE) {
                    g.fillRect(0, y, boardSizeX + LINE_THICKNESS, LINE_THICKNESS);
                }

                for(int x = LINE_THICKNESS; x < boardSizeX; x += CELL_SIZE){
                    for(int y = LINE_THICKNESS; y < boardSizeY; y += CELL_SIZE){
                        g.drawImage(img, x, y, CELL_SIZE - LINE_THICKNESS, CELL_SIZE - LINE_THICKNESS, null);
                    }
                }

                Cell[][] cellsBoard = board.getCellBoard();
                Rectangle bounds = g.getClipBounds();
                int x = (int)bounds.getX();
                int y = (int)bounds.getY();

                int dcell_x = (x / CELL_SIZE);
                int dcell_x2 = (int)((x + bounds.getWidth() + CELL_SIZE - 1) / CELL_SIZE);
                int dcell_y = (y / CELL_SIZE);
                int dcell_y2 = (int)((y + bounds.getHeight() + CELL_SIZE - 1) / CELL_SIZE);
                Color[] player_colours; Color player_colour; int xStart; int yStart; int recWidth; int recHeight;

                player_colours = FullColour(players.length);
                for (Player player: players) {
                    player_colour = player_colours[player.num()];
                    cimg = copyCat(img);
                    cimg = colourImage(cimg, argbToArr(player_colour.getRGB()));
                    for(int i = dcell_x; i < min(dcell_x2, cellsBoard.length); i++) {
                        for(int j = dcell_y; j < min(dcell_y2, cellsBoard[i].length); j++) {
                            if(!cellsBoard[i][j].isEmpty()){
                                xStart = i * CELL_SIZE + LINE_THICKNESS;
                                yStart = j * CELL_SIZE + LINE_THICKNESS;
                                recWidth = CELL_SIZE - LINE_THICKNESS;
                                recHeight = CELL_SIZE - LINE_THICKNESS;
                                if (player.equals(cellsBoard[i][j].getPlayer())) {
                                    g.drawImage(cimg, xStart, yStart, recWidth, recHeight, null);
                                }
                            }
                        }
                    }
                }
            }

            //change the color of all the cells.
            private BufferedImage colourImage(BufferedImage img, int[] colour) {
                int[] xy; int[] pixor; int argb;
                if(img == null) return null;
                for(int i = 0; i < img.getHeight(); i++){
                    for(int j = 0; j < img.getWidth(); j++){
                        xy = new int[] {i, j};
                        argb = img.getRGB(xy[0], xy[1]);
                        pixor = argbToArr(argb);
                        pixor = colourPix(pixor, colour);
                        img.setRGB(xy[0], xy[1], arrToArgb(pixor));
                    }
                }
                return img;
            }

            private int[] colourPix(int[] data, int[] colour) {
                if(data[0] == 0){
                    return data;
                }
                double colourIntensity;
                colourIntensity = (colourCurve((data[1] + data[2] + data[3]) / 3) / 255.);
                return new int[] {data[0], (int)(colour[1] * colourIntensity), (int)(colour[2] * colourIntensity), (int)(colour[3] * colourIntensity)};
            }

            private double colourCurve(double colourStuff) {
                return (Math.pow(colourStuff, 2) / 255);
            }

            // creates an array for the RGBA
            private int[] argbToArr(int argb) {
                return new int[] {(argb>>24) & 0xff, (argb>>16) & 0xff, (argb>>8) & 0xff, argb & 0xff};
            }

            // creates RGBA from int array.
            private int arrToArgb(int[] arr) { // arr is A R G B
                return (arr[0]<<24) | (arr[1]<<16)| (arr[2]<<8) | arr[3] ;
            }

            // returns a list of colours for all the players.
            private Color[] FullColour(int pNum) {
                Color[] retLst = new Color[pNum];
                int maxD = 255 * 6;
                double d = min((double)maxD / pNum, 166);
                int[] rgb;
                for (int i = 0; i < pNum; i++) {
                    rgb = ColourD((int) (d * i));
                    retLst[i] = new Color(rgb[0], rgb[1], rgb[2]);
                }
                return retLst;
            }

            // calculate the color of the given player
            private int[] ColourD(int dist) {
                int t = 0, r = 255, g = 0, b = 0;
                boolean rs = true, gs = false, bs = false;
                int[] ct;
                ct = ColourSD(dist, t, b, bs); b = ct[0]; t = ct[1]; bs = !bs;
                ct = ColourSD(dist, t, r, rs); r = ct[0]; t = ct[1]; rs = !rs;
                ct = ColourSD(dist, t, g, gs); g = ct[0]; t = ct[1]; gs = !gs;
                ct = ColourSD(dist, t, b, bs); b = ct[0]; t = ct[1]; bs = !bs;
                ct = ColourSD(dist, t, r, rs); r = ct[0]; t = ct[1]; rs = !rs;
                ct = ColourSD(dist, t, g, gs); g = ct[0]; t = ct[1]; gs = !gs;
                return new int[]{r, g, b};
            }

            // dist is the how far away i want to go from red(255, 0, 0).
            // t is how far i am already,
            // c is the shade(r, g or b) currently being modified,
            // bool s is whether going up or down.
            private int[] ColourSD(int dist, int t, int c, boolean s) {
                int sp = min(dist - t, 255);
                t += sp;
                if (!s)
                    c += sp;
                else
                    c -= sp;
                return new int[]{c, t};
            }

            BufferedImage copyCat(BufferedImage source){
                BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
                Graphics g = b.getGraphics();
                g.drawImage(source, 0, 0, null);
                g.dispose();
                return b;
            }
        };
    }

    // set the listeners for the board panel
    private MouseInputAdapter setMouseInputAdapter() {
        return new MouseInputAdapter() {

            //dragging have the same effect like pressing
            @Override
            public void mouseDragged(MouseEvent e) {
                mousePressed(e);
            }

            // when there is a press on the board's area
            public void mousePressed(MouseEvent l) {
                String errorTxt = "Error, click again";
                int x = l.getX();
                int y = l.getY();
                x = x / CELL_SIZE;
                y = y / CELL_SIZE;
                if (board.playOneTurn(x, y)) {      // turn played successfully
                    numCellsFill ++;
                    if (board.isPlayerWon(x, y)) {      // There is a winner
                        playerWon(this);
                    }                                   // board is full, stop the game
                    else if( numCellsFill == numCellsX * numCellsY){
                        boardFull(this);
                    }
                    else{                               //move to next player.
                        board.nextPlayer();
                        setCurPlayerLabelText();
                    }
                    gamePanel.repaint(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                else {                              // illegal click, hold the error message for at most 2000 ms.
                    infoLabel.setText(errorTxt);
                    if(thread != null) thread.interrupt();
                    thread = new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            if(infoLabel.getText().equals(errorTxt)){
                                infoLabel.setText("");
                            }
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    });
                    thread.start();
                }
            }
        };
    }


    // create the scroll panel for the board, and set its sizes
    private JScrollPane setScrollPanel() {
        JScrollPane scrollFrame = new JScrollPane(gamePanel);
        gamePanel.setPreferredSize(new Dimension(numCellsX * CELL_SIZE, numCellsY * CELL_SIZE));
        gamePanel.setAutoscrolls(true);
        scrollFrame.setBounds(px(10 / .3), frameY / (frameY / 8),2 * frameX / 3 - 16, frameY - 4 * frameY / (frameY / 8) - 16);
        scrollFrame.getVerticalScrollBar().setUnitIncrement(16);
        scrollFrame.getHorizontalScrollBar().setUnitIncrement(16);
        return scrollFrame;
    }

    // This func instantiate when the last player won the game.
    // update gui for isPlayerWon stuff, kill the mouse listener and update labels
    private void playerWon(MouseInputAdapter mader) {
        thread.interrupt();
        infoLabel.setForeground(Color.green);
        infoLabel.setText("Player " + board.getPlayer() + " wins!");
        gamePanel.removeMouseListener(mader);
        gamePanel.removeMouseMotionListener(mader);
    }

    // This func instantiate when the board is full and there is no winner.
    // update gui for tie stuff, kill the mouse listener and update labels
    private void boardFull(MouseInputAdapter mader) {
        thread.interrupt();
        infoLabel.setForeground(Color.orange);
        infoLabel.setText("It's a tie. Play again!");
        gamePanel.removeMouseListener(mader);
        gamePanel.removeMouseMotionListener(mader);
    }

    // Sets the text for the player label
    private void setCurPlayerLabelText() {
        curPlayerLabel.setText("Current Player: " + board.getPlayer().toString());
    }

    // returns the x coordinates on the screen that is Percentage of the screen width
    private int px(double percents) {
        return (int)(mainFrame.getBounds().getWidth() * percents) / 100;
    }

    // returns the y coordinates on the screen that is Percentage of the screen height
    private int py(double percents) {
        return(int)(mainFrame.getBounds().getHeight() * percents) / 100;
    }



}
