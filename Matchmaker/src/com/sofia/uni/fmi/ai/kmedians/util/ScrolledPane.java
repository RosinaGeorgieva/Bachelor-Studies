package com.sofia.uni.fmi.ai.kmedians.util;

import javax.swing.*;
import java.awt.*;

public class ScrolledPane extends JPanel {
    private JScrollPane vertical;
    private  JTextArea console;

    public ScrolledPane() {
        setPreferredSize(new Dimension(350, 200));
        console = new JTextArea(200, 150);
        console.setText("Results...");
        console.setEditable(false);

        vertical = new JScrollPane(console);
        vertical.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        vertical.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(vertical);

        setVisible(true);
    }

    public JTextArea getConsole() {
        return this.console;
    }
}
