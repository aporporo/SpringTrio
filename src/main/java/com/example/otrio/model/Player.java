package com.example.otrio.model;

import lombok.Data;

// test for github

@Data
public class Player {
    private Piece[] piece;
    private String playerName;
    private String color;
    private int wins;
    private int playerId;

    public Player (String color, String playerName, int playerId) {
        this.playerName = playerName;
        this.color = color;
        this.piece = new Piece[9];
        this.playerId = playerId;
        wins = 0;
    }

    public void playerWon() {
        wins += 1;
    }

    public void setPieces(Piece[] pieces) {
        if (pieces == null || pieces.length != 9) {
            throw new IllegalArgumentException("Pieces array must have exactly 9 elements.");
        }
        this.piece = pieces;
    }

    public boolean checkPieces(int size) {
        int count = 0;

        for (Piece piece : piece) {
            if (piece != null && piece.size() == size) {
                count++;
                if (count >= 3) {
                    System.out.println(count);
                    System.out.println("CHECK PIECE FALSE");
                    return false;
                }
            }
        }

        System.out.println(count);
        System.out.println("CHECK PIECE TRUE");
        return true;
    }
}
