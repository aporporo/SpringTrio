package com.example.otrio.service;

import com.example.otrio.exception.InvalidGameException;
import com.example.otrio.exception.InvalidParamException;
import com.example.otrio.exception.NotFoundException;
import com.example.otrio.model.Board;
import com.example.otrio.model.Game;
import com.example.otrio.model.Piece;
import com.example.otrio.model.Player;
import com.example.otrio.storage.GameStorage;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.otrio.model.GameStatus.*;

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

    public Game createGame (Player player) {
        Game game = new Game();
        board = new Board();
        game.setBoard(board);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(NEW);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToGame(Player player, String gameId) throws InvalidParamException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Cannot find game with matching game id");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayer4() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        } else if (game.getPlayer3() != null) {
            game.setPlayer4(player);
        } else if (game.getPlayer2() != null) {
            game.setPlayer3(player);
        } else {
            game.setPlayer2(player);
        }
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;

    }

    public Game connectToRandomGame(Player player) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(NEW))
                .findFirst().orElseThrow(() -> new NotFoundException("Game not found"));

        if (game.getPlayer3() != null) {
            game.setPlayer4(player);
        } else if (game.getPlayer2() != null) {
            game.setPlayer3(player);
        } else {
            game.setPlayer2(player);
        }
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }


}
