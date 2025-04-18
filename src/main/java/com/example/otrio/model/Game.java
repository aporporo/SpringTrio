package com.example.otrio.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    private int currentTurn;
    private List<Integer> activePlayerIds = new ArrayList<>();

    public void resetPieces(Player player) {
        if (player != null) {
            player.setPiece(new Piece[9]);
        }

    }

    // Update when players join
    public void addActivePlayer(int playerId) {
        this.activePlayerIds.add(playerId);
    }
}
