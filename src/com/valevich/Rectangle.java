package com.valevich;

public class Rectangle {

    private int width;
    private int height;
    private int x;
    private int y;


    public Rectangle(){}

    public void reset() {
        width = 0;
        height = 0;
        x = 0;
        y = 0;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
