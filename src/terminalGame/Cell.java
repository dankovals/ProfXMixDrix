package terminalGame;

// represents one cell of the board
public class Cell { //cellulose
    private Player player;
    private int x, y;

    // Constructor
    public Cell(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    //return the player in this cell
    public Player getPlayer() {
        return this.player;
    }

    public boolean addToCell(Player new_player) {
        if(player.isDefault()) { // checks if the cell is empty
            player = new_player;
            return true;
        }
        else
            return false;
    }

    // checks if the cell is empty
    public boolean isEmpty() {
        return player.isDefault();
    }

    // string representation of the cell;
    public String toString() {
        return "X: " + x + " Y: " + y + " Player: " + player;
    }
}
