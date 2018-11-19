package leyadaGUIGame;

import javax.swing.*;

public class OptionsScreen { // config window
    JFrame frame;

    public OptionsScreen(MainGUIGame frame){
        this.frame = frame;
        frame.clear();
        backToMM(frame);
    }

    private void backToMM(MainGUIGame frame) {
        frame.dispose();
        new MainGUIGame();
    }
}
