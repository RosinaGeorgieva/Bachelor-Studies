package com.sofia.uni.fmi.ai.kmedians;

import com.sofia.uni.fmi.ai.kmedians.alg.KMedians;
import com.sofia.uni.fmi.ai.kmedians.alg.Matchmaker;
import com.sofia.uni.fmi.ai.kmedians.util.Calculator;
import com.sofia.uni.fmi.ai.kmedians.util.LinePlot;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Matchmaker matchmaker = new Matchmaker();

//    public static void main(String... args) {
//        Thread matchmakerThread = new Thread(matchmaker);
//        matchmakerThread.start();
//
//        JFrame frame = new JFrame();
//        frame.setTitle("Matchmaking recommender system");
//        frame.setSize(400, 500);
//
//        var inputField = new JTextField("Enter username...");
//        inputField.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                inputField.setText("");
//            }
//        });
//        inputField.setBounds(20,20, 350,30);
//        frame.add(inputField);
//
//        var button = new JButton("Matchmake!");
//        button.setBounds(265, 60, 105, 20);
//        frame.add(button);
//
//        var console = new JTextArea("Results...");
//        console.setBounds(20, 90, 350, 200);
//        console.setEditable(false);
//        frame.add(console);
//
//        button.addActionListener((event) -> {
//            try {
////                outputArea.setText("Searching for potential matches...");
//                synchronized (matchmaker) {
//                    while(!matchmaker.isReady()) {
//                        matchmaker.wait();
//                    }
//                }
//                console.setText("");
//                console.append(matchmaker.getMatches(inputField.getText()));
//            } catch (InterruptedException exception) {
//                exception.printStackTrace();
//            }
//        });
//
//        frame.setLayout(null);
//        frame.setVisible(true);
//    }

    public static void main(String... args) throws InterruptedException, IOException {
        Thread matchmakerThread = new Thread(matchmaker);
        matchmakerThread.start();

        Scanner sc = new Scanner(System.in);
        String input = sc.next();
        synchronized (matchmaker) {
            while (!matchmaker.isReady()) {
                matchmaker.wait();
            }
        }
        System.out.println(matchmaker.getMatches(input));
    }
}
