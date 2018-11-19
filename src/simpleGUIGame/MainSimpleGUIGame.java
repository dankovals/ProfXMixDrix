package simpleGUIGame;

public class MainSimpleGUIGame {
//    private static int rowsNum, colsNum, winSequenceNum;
//    private Player[] playersList;


    public static void main(String[] args){
        System.out.println(args.length);
        InputScreen inputScreen = new InputScreen();
        while (! inputScreen.isInitiationReady()) {
            System.out.print("");
        }
        GameB b = new GameB(inputScreen);

    }



}

