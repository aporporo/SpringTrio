package com.example.otrio.service;

import com.example.otrio.model.Board;
import com.example.otrio.model.Piece;
import com.example.otrio.model.Player;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private Board board;
    private int playerTurn;
    public Player player1;
    public Player player2;
    public Player player3;
    public Player player4;
    public String winner;

    public GameService() {
        board = new Board();
        playerTurn = 1;
        this.player1 = new Player("blue", "P1", 1);
        this.player2 = new Player("red", "P2", 2);
        this.player3 = new Player("green", "P3", 3);
        this.player4 = new Player("yellow", "P4", 4);

    }

    public Boolean makeMove(int row, int col, int size, Player player) {
//        System.out.println("Player ID = " + player.playerId + ", Player Turn = " + playerTurn);
//        if (player.playerId == playerTurn && board.placePiece(row, col, size, player)) {
        if (board.placePiece(row, col, size, player)) {
            if (board.gameWon == 1) {
                winner = board.winnerColor;
                System.out.println(winner);
            }

            if (playerTurn == 4) {
                playerTurn = 1;
            } else {
                playerTurn++;
            }
            return true;
        } else {
//            System.out.println("Error in GAME makeMove");
            return false;
        }

    }

    public String getWinner() {
        return winner;
    }

    public Board getBoard() {
        return board;
    }



    public int isPlayerTurn() {
        return playerTurn;
    }

    public void reset() {
        board = new Board();
        playerTurn = getPlayerByColor(getWinner()).playerId;
        winner = "";

    }

    public Player getPlayerByColor(String color) {
        if (color.equals(player1.color)) {
            return player1;
        } else if (color.equals(player2.color)) {
            return player2;
        } else if (color.equals(player3.color)) {
            return player3;
        } else {
            return player4;
        }


    }
}
