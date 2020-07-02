package ru.vsu.cs.course1.graph.demo;


import static java.awt.Frame.MAXIMIZED_BOTH;

import javax.swing.JFrame;


public class Program {

    public static void main(String[] args) {
        JFrame mainFrame = new Form();
        mainFrame.setVisible(true);
        mainFrame.setExtendedState(MAXIMIZED_BOTH);
    }
}
