package tests;

import javax.swing.*;
import java.awt.*;

public class TEST extends JFrame{
    int x;
    int y;

    public TEST() {
        setLayout(null);
        JPanel test = new JPanel();
        test.setBounds(0,0,1000,1000);
        test.setPreferredSize(new Dimension(2000,2000));
        test.setBackground(Color.green);
        JScrollPane scrollFrame = new JScrollPane(test);
        test.setAutoscrolls(true);
        scrollFrame.setBounds(0, 0, 100, 100);
        scrollFrame.setPreferredSize(new Dimension(8000,3000));
        this.add(scrollFrame);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        TEST t = new TEST();
    }

}
