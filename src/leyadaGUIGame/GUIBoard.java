package leyadaGUIGame;

import terminalGame.Board;
import terminalGame.Cell;
import terminalGame.Player;


//this class enveloped the Board class from the terminal game.
public class GUIBoard {
    private Board board;
    private int numCellsX, numCellsY, winSequence, currentPlayerIdx;
    private Player[] playersLst;

    public GUIBoard(int x, int y, int winSeq, Player[] players) { // board size and isPlayerWon size
        this.numCellsX = x;
        this.numCellsY = y;
        this.winSequence = winSeq;
        this.playersLst = players;
        this.currentPlayerIdx = 0;
        board = new Board(x, y, winSeq);
    }

    // play one Turn
    public boolean playOneTurn(int x, int y) { // played location and player who played there
        return board.playTurn(x, y, playersLst[currentPlayerIdx]);
    }

    // pass to the next player
    public void nextPlayer(){
        currentPlayerIdx = (currentPlayerIdx + 1) % playersLst.length;
    }

    // checks if current player won the game.
    public boolean isPlayerWon(int x, int y) {
        return board.isPlayerWon(playersLst[currentPlayerIdx], x, y);
    }

    // Returns the Board object
    public Board getBoard() {
        return board;
    }

    // Resets the values of the cells on the board, and create new Board
    public void reset() {
        board = new Board(numCellsX, numCellsY, winSequence);
        currentPlayerIdx = 0;

    }

    // Returns the board as Matrix of Cells objects
    public Cell[][] getCellBoard() {
        return board.getCellsTable();
    }

    // returns the current Player
    public Player getPlayer() {
        return playersLst[currentPlayerIdx];
    }

    //create list of playersLst in size numPlayers
    public static Player[] createPlayersLst(int numPlayers) { // create playersLst;
        Player[] pa = new Player[numPlayers];
        for(int i = 0; i < numPlayers; i++) {
            pa[i] = new Player(String.valueOf(i), "null", i, i);
        }
        return pa;
    }

    public static Player[] createPlayersLst(int nuuu, String[] pls, String[] picz, int[] plc) { // create playersLst
        Player[] pa = new Player[nuuu];
        for(int i = 0; i < nuuu; i++) {
            pa[i] = new Player(pls[i], picz[i], plc[i], i);
        }
        return pa;
    }

}