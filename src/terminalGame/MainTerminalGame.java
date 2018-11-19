package terminalGame;

import java.util.Scanner;

public class MainTerminalGame {
    private static String[] P_NAMES_LST = {"Elon61", "Jesus0001", "Danko17"};
    private static char[] P_CHARS_LST = {'Ω', '†', 'δ'};
    private static int P_NUM = 2;//P_NAMES_LST.length;//P_NAMES_LST.length;
    private static int[] BOARD_SIZE = {10, 10};
    private static int WINNING_SEQUENCE = 4;


    // create list of players
    public static Player[] createPlayersList() {
        Player[] players = new Player[P_NUM];
        for(int i = 0; i < P_NUM; i++) {
            players[i] = new Player(P_NAMES_LST[i], P_CHARS_LST[i]);
        }
        return players;
    }


    // get coordinates from player, check validity of the coordinates, and put the char on the board
    private static int[] playTurn(Board board, Player name, Scanner sc) {
        System.out.println(name + " please tell where you playOneTurn: [x, y]");
        int x = sc.nextInt(), y = sc.nextInt();
        sc.nextLine();
        while (! board.canPlayTurn(x, y)){
            System.out.println("You're wrong try again: [x y]");
            x = sc.nextInt(); y = sc.nextInt();
            sc.nextLine();
        }
        board.playTurn(x, y, name);
        return new int[]{x, y};
    }


    // start the terminalGame game
    private static void playTerminalGame(Board board) {
        Player[] pLst = createPlayersList();
        Scanner sc = new Scanner(System.in);
        for(int i = 0; i < board.size(); i++) {      // runs until the board is full, or there is a winner
            System.out.println(board);                 // print the board
            int[] xy = playTurn(board, pLst[i % P_NUM], sc);    // playOneTurn one turn
            if(board.isPlayerWon(pLst[i % P_NUM], xy[0], xy[1])) {   // checks if there is a winner
                System.out.println(board);
                System.out.println(pLst[i % P_NUM] + " IS THE WINNER Ωδ∞φε∩!!!");
                return;
            }
        }
        System.out.println("YOU'RE ALL LOSERS HAHAHAHA");   // All cells are full.
    }

    public static void main(String[] args) {
        Board terminalBoard = new Board(BOARD_SIZE[0], BOARD_SIZE[1], WINNING_SEQUENCE); // creating board
        playTerminalGame(terminalBoard); //start a game
    }
}
