package com.example.otrio.model;

public class Player {
    public Piece[] piece;
    public String playerName;
    public String color;
    public int playerId;

    public Player (String color, String playerName, int playerId) {
        this.playerName = playerName;
        this.color = color;
        this.piece = new Piece[9];
        this.playerId = playerId;
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

    public boolean checkPieces(int size) {
        int count = 0;
        Piece[] pieces = getPiece();
//        System.out.println(Arrays.deepToString(pieces));




        for (int i = 0; i < 9; i++) {
            if (pieces[i] != null) {
                if (pieces[i].getSize() == size) {
//                    System.out.println(count);
                    count++;
                }
            }

        }
        if (count < 3) {
//            System.out.println("CHECK PIECE TRUE");
            return true;
        } else {
//            System.out.println("CHECK PIECE FALSE");
            return false;
        }
    }
}
