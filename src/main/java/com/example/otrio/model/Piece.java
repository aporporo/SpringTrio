package com.example.otrio.model;

/**
 * @param size Size of the piece (e.g., 1, 2, or 3)
 */

public record Piece(String color, int size) {

    @Override
    public String toString() {

        return color + size;
    }
}
