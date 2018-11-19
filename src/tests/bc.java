package tests;

import javax.swing.*;
import java.awt.*;

public class bc extends JApplet {
    int p2[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    int temp = 0;
    public void paint(Graphics gr){
        for(int i = 0; i < 11; i++) {
            System.out.println();
            setSize(1000, 650);
            gr.setColor(Color.black);
            gr.fillOval(50 * i, -10 + (650 - p2[i]) % 650, 5*2, 5*2);
        }
    }

    public static void main(String[] args) {
        int p[] = {2, 6, 4, 8, 10, 12, 89, 68, 45, 37 };
        for(int i = 0; i < p.length; i++) {

        }

    }
}