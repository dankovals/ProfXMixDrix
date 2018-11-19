package terminalGame;

// represents one player
public class Player {
    private int pColor;
    private int pID;
    private char pChar;
    private String pName;
    private String picture;

    // Constructor for terminalGame game
    public Player(String pName, char pChar) {
        this.pChar = pChar;
        this.pName = pName;
    }

    //the name is number,
    public Player(String pName, String picture, int color, int id) {
        this.pName = pName;
        this.picture = picture;
        this.pID = id;
        pColor = color;
    }

    // checks if the cell is empty - the default player
    public boolean isDefault() {
        return pName.equals("DefaultPlayer");
    }

    // Returns the name.
    public String toString() {
        return pName;
    }

    // returns the char for the terminalGame game.
    public String getTerminalChar() {
        String terChar = String.valueOf(pChar);
        if(isDefault())
            terChar =  " ";

        return terChar;
    }

    public int num() {
        return pID;
    }


    public String getPicLink() {
        return picture;
    }


    public int getColour() {
        return pColor;
    }

}
