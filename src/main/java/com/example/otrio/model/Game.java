package com.example.otrio.model;

import lombok.Data;

@Data
public class Game {
    private String gameId;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Board board;
    private GameStatus status;
    private String winner;
    private String[] lastMove;

    public void resetPieces(Player player) {
        if (player != null) {
            player.piece = new Piece[9];
        }

    }
}
