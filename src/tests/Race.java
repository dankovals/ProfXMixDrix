package tests;

import terminalGame.Board;
import terminalGame.Player;

public class Race {
    // demander papa.
    /*public static int moshe_horizontal(Board teemo, Player name, int x, int y, int l) {
        //use a switch in single func
        if(teemo.player(x, y) == name) { //tests if player in current case is the one im looking for
            l += 1; // if it is increment the number of cases
            if(y > 0) { // since im  going left continue until i reach 0 which is the end
                //y -= 1;
                l = moshe_horizontal(teemo, name, x, y - 1, l);
            }
        } // can maybe call it again with a diff param to start over from the played point and do it the other way?
        return l;
    }*/

    public static void main(String[] args) {
        int[] xy = {2, 2};
        Player one = new Player("Name", '\'');
        Player two = new Player("Name2", '!');
        Board flo = new Board(6, 6, 4);
        flo.playTurn(5, 5, one);
        flo.playTurn(4, 4, one);
        flo.playTurn(3, 3, one);
        flo.playTurn(2, 2, one);
        flo.playTurn(1, 1, one);
        flo.playTurn(0, 0, one);
        System.out.println(flo);
        System.out.println(flo.isPlayerWon(one, xy[0], xy[1]));
        System.out.println(true);

    }

}
