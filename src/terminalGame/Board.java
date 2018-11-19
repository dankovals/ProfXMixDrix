package terminalGame;

// represents the board in the game
public class Board { //snobord
    private int x, y, winLength; // winLength is winning lenght
    private Cell[][] cellsTable;
    public Board(int x, int y, int winLength) {
        this.x = x;
        this.y = y;
        if(winLength <= x && winLength <= y) {
            this.winLength = winLength;
        }
        else this.winLength = (Math.min(x, y));
        cellsTable = new Cell[x][y];
        buildBoardCells();
    }

    // Returns the number of cells on board.
    public int size() {
        return this.x * this.y;
    }
    public Cell[][] getCellsTable() {
        return cellsTable;
    }
    public int getXSize() {
        return x;
    }
    public int getYSize() {
        return y;
    }


    // checks if there is a winner. terminalGame four options to check.
    public boolean isPlayerWon(Player player, int px, int py) {
        boolean is_win = (
            isWinInTheDirection(player, px, py, 1, 0) ||
            isWinInTheDirection(player, px, py, 0, 1) ||
            isWinInTheDirection(player, px, py, 1, 1) ||
            isWinInTheDirection(player, px, py, 1, -1));
        return is_win;
    }

    // checks if the given coordinate are legal for the next move
    public boolean canPlayTurn(int x, int y) {
        if(x < this.x && y < this.y && x >= 0 && y >= 0) {
            return cellsTable[x][y].isEmpty();
        }
        return false;
    }

    // change the board(add new char) according to the given coordinates.
    public boolean playTurn(int x, int y, Player player) {
        if((x > getXSize() - 1|| y > getYSize() - 1) || !(x >= 0 && y >= 0)) return false;
        return cellsTable[x][y].addToCell(player);
    }

    // Prints the board
    public String toString() {
        String ret = "   ";
        for(int x = 0; x!=this.x; x++) {
            ret += "|---";
        }
        ret += "|";
        ret += "\n";
        for(int y = 0; y!=this.y; y++) {
            if((this.y - y - 1) < 10) {
                ret +="  " + (this.y - y - 1) + "| ";
            }
            else if((this.y - y - 1) < 100) {
                ret += " " + (this.y - y - 1) + "| ";
            }
            else if((this.y - y - 1) < 1000){
                ret += (this.y - y - 1) + "| ";
            }
            else {
                ret += (this.y - y - 1) + "|";
            }
            
            for(int x = 0; x!=this.x; x++) {
                ret += cellsTable[x][this.y - y - 1].getPlayer().getTerminalChar();
                ret += " | ";
            }
            ret += "\n   ";
            for(int x = 0; x!=this.x; x++) {
                ret += "|---";
            }
            ret += "|";
            ret += "\n";
        }
        ret += "   ";
        for(int x = 0; x!=this.x; x++) {
            if(x < 10) {
                ret += " " + x + "  ";// valueOf int and count stuff;
            }
            else if(x < 100) {
                ret += " " + x + " ";
            }
            else if(x < 1000){
                ret += " " + x + "";
            }
            else {
                ret += "" + x + "";
            }
            
        }
        return ret;
    }

    //returns the player in the given coordinate
    private Player player(int x, int y) {
        return cellsTable[x][y].getPlayer();
    }

    // Checks if the given coordinate inside the board
    private boolean isInBoard(int x, int y){
        return ((0 <= x && x < this.x) && (0 <= y && y < this.y));
    }

    // checks if the cell contains the given player
    private boolean isInCell(Player player, int x, int y) {
        return this.player(x, y) == player;
    }

    // count how many consecutive chars, start from x,y in one direction.
    private int countConsecutiveInOneDirection(Player player, int x, int y, int incx, int incy) {
        int count = 0;
        while(true){
            x += incx;
            y += incy;
            if(!(isInBoard(x ,y) && isInCell(player, x, y))){
                return count;
            }
            count += 1;
        }
    }

    // Checks one direction from the last move, checks if there is a winning sequence.
    private boolean isWinInTheDirection(Player player, int x, int y, int incx, int incy){
        if(isInCell(player, x, y)) {
            int length = countConsecutiveInOneDirection(player, x, y, incx, incy) + countConsecutiveInOneDirection(player, x, y, -incx, -incy) + 1;
            return length >= winLength;
        }
        return false;
    }

    // Build Matrix of cells (with "DefaultPlayer") that represents the board.
    private void buildBoardCells() {
        for(int i = this.x; i!=0; i--) {
            for(int j = this.y; j!=0; j--) {
                cellsTable[i - 1][j - 1] = new Cell(i, j, new Player("DefaultPlayer", '0'));
            }
        }
    }


}