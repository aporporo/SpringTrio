package com.example.otrio.model;

public class Player {
    public Piece[] piece;
    public String playerName;
    public String color;
    public int wins;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int playerId;

    public Player (String color, String playerName, int playerId) {
        this.playerName = playerName;
        this.color = color;
        this.piece = new Piece[9];
        this.playerId = playerId;
        wins = 0;
    }

    public void printPiece() {
        for (int i = 0; i < 9; i++) {
            if (piece[i] == null) {
                System.out.print("null ");
            } else {
                System.out.print(piece[i].toString() + " ");
            }

        }

    }
    public void resetPieces() {
        this.piece = new Piece[9];
    }



    public void setPiece(Piece[] piece) {
        this.piece = piece;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Piece[] getPiece() {
        return piece;
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
            if (piece != null && piece.getSize() == size) {
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
