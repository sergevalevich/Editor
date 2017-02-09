package com.valevich;

import javax.swing.*;
import java.awt.*;

public class MyMenuBar extends JMenuBar {

    private Color color;

    MyMenuBar (Color color) {
        this.color = color;
    }

    void setColor(Color color) {
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillRect(0, 0, getWidth(), getHeight());

    }
}
