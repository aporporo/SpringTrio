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


//    private Board board;
//    private int playerTurn;
//    public Player player1;
//    public Player player2;
//    public Player player3;
//    public Player player4;
//
//    public Game() {
//        board = new Board();
//        playerTurn = 1;
//        this.player1 = new Player("blue", "P1", 1);
//        this.player2 = new Player("red", "P2", 2);
//        this.player3 = new Player("green", "P3", 3);
//        this.player4 = new Player("yellow", "P4", 4);
//
//    }
//
//    public boolean makeMove(int row, int col, int size, Player player) {
////        System.out.println("Player ID = " + player.playerId + ", Player Turn = " + playerTurn);
////        if (player.playerId == playerTurn && board.placePiece(row, col, size, player)) {
//        if (board.placePiece(row, col, size, player)) {
//
//            if (playerTurn == 4) {
//                playerTurn = 1;
//            } else {
//                playerTurn++;
//            }
//            return true;
//        } else {
////            System.out.println("Error in GAME makeMove");
//            return false;
//        }
//
//    }
//
//
//
//    public Board getBoard() {
//        return board;
//    }
//
//    public int isPlayerTurn() {
//        return playerTurn;
//    }
//
//    public void reset() {
//        board = new Board();
//        playerTurn = 1;
//    }
}
