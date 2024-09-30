package com.example.otrio.model;

public class Piece {
    private final String color;
    private final int size;  // Size of the piece (e.g., 1, 2, or 3)

    public Piece(String color, int size) {
        this.color = color;
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {

        return color + size;
    }
}
