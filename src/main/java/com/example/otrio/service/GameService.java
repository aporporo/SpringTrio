package com.example.otrio.service;

import com.example.otrio.exception.InvalidGameException;
import com.example.otrio.exception.InvalidParamException;
import com.example.otrio.exception.NotFoundException;
import com.example.otrio.model.Board;
import com.example.otrio.model.Game;
import com.example.otrio.model.Player;
import com.example.otrio.storage.GameStorage;
import org.springframework.stereotype.Service;

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

    public String getGameId() {
        return gameId;
    }

    public String gameId;

    public void setGame(Game game) {
        this.game = game;
    }

    public Game game;


    public GameService(Player player) {
        board = new Board();
        playerTurn = 1;
        this.player1 = player;
//        gameId = UUID.randomUUID().toString();
        gameId = "1";
        this.game = new Game();
        game.setBoard(board);
        game.setGameId(gameId);
        game.setPlayer1(player);
        game.setStatus(NEW);
        game.setCurrentTurn(1);
        GameStorage.getInstance().setGame(game);
    }

    public GameService() {
        board = new Board();
        playerTurn = 1;
        this.player1 = new Player("blue", "P1", 1);
        this.player2 = new Player("red", "P2", 2);
        this.player3 = new Player("green", "P3", 3);
        this.player4 = new Player("yellow", "P4", 4);

    }



    public Boolean makeMove(int row, int col, int size, Player player) throws NotFoundException, InvalidGameException {
//        System.out.println("Player ID = " + player.playerId + ", Player Turn = " + playerTurn);
//        if (player.playerId == playerTurn && board.placePiece(row, col, size, player)) {
//        if (!GameStorage.getInstance().getGames().containsKey(this.getGameId())) {
//            throw new NotFoundException("Game not found");
//        }

        Game game = GameStorage.getInstance().getGames().get(this.getGameId());
//        if (game.getStatus().equals(FINISHED)) {
//            throw new InvalidGameException("Game is already finished");
//        }
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

    public Game reset(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);

        game.setBoard(new Board());
        game.resetPieces(game.getPlayer1());
        game.resetPieces(game.getPlayer2());
        game.resetPieces(game.getPlayer3());
        game.resetPieces(game.getPlayer4());
        if (winner.equals("blue")) {
            game.setCurrentTurn(1);
            System.out.println("reset turn to 1");
        } else if (winner.equals("red")) {
            game.setCurrentTurn(2);
            System.out.println("reset turn to 2");
        } else if (winner.equals("green")) {
            game.setCurrentTurn(3);
            System.out.println("reset turn to 3");
        } else if (winner.equals("yellow")) {
            game.setCurrentTurn(4);
            System.out.println("reset turn to 4");
        }

        System.out.println("reset test: winner" + this.winner);
        
        this.winner = null;
        System.out.println("reset test: after winner" + this.winner);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game gameMove (Player player, String gameId, int row, int col, int size) throws InvalidParamException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Cannot find game with matching game id");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);


        if (player.getPlayerId() == game.getCurrentTurn() && game.getBoard().placePiece(row, col, size, player)) {
            game.setLastMove(new String[]{
                    String.valueOf(row), String.valueOf(col), String.valueOf(size), player.color
            });
            if (game.getBoard().gameWon == 1) {
                winner = game.getBoard().winnerColor;
                System.out.println(winner);
                //game.setBoard(new Board());
            }

            if (game.getCurrentTurn() == 4) {
                playerTurn = 1;
                game.setCurrentTurn(playerTurn);
            } else {
                playerTurn++;
                game.setCurrentTurn(playerTurn);
            }
        } else {
            throw new InvalidGameException("Error in gameMove method");
        }

        GameStorage.getInstance().setGame(game);
        return game;
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

        GameService gameService = new GameService(player);

        return gameService.getGame();
    }

    public Player findPlayerById(Game game, int playerId) throws InvalidParamException {
        if (game.getPlayer1() != null && game.getPlayer1().getPlayerId() == playerId) {
            return game.getPlayer1();
        } else if (game.getPlayer2() != null && game.getPlayer2().getPlayerId() == playerId) {
            return game.getPlayer2();
        } else if (game.getPlayer3() != null && game.getPlayer3().getPlayerId() == playerId) {
            return game.getPlayer3();
        } else if (game.getPlayer4() != null && game.getPlayer4().getPlayerId() == playerId) {
            return game.getPlayer4();
        }
        throw new InvalidParamException("Player not found in the game");
    }

    public Game getGame() {
        return game;
    }

    public Game getGameById(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        return game;
    }

    public Game connectToGame(String playerName, String gameId) throws InvalidParamException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Cannot find game with matching game id");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayer4() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        } else if (game.getPlayer3() != null) {
            Player player = new Player("yellow", playerName, 4);
            game.setPlayer4(player);
        } else if (game.getPlayer2() != null) {
            Player player = new Player("green", playerName, 3);
            game.setPlayer3(player);
        } else {
            Player player = new Player("red", playerName, 2);
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
