package com.valevich;

import javax.swing.*;
import java.awt.*;

public class MyToolBar extends JToolBar{
    private Color color;

    MyToolBar (String name, int orientation, Color color) {
        super(name, orientation);
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
