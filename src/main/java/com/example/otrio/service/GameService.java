package com.example.otrio.service;

import com.example.otrio.exception.InvalidGameException;
import com.example.otrio.exception.InvalidParamException;
import com.example.otrio.exception.NotFoundException;
import com.example.otrio.model.Board;
import com.example.otrio.model.Game;
import com.example.otrio.model.Player;
import com.example.otrio.storage.GameStorage;
import lombok.Data;
import org.springframework.stereotype.Service;

import static com.example.otrio.model.GameStatus.*;

@Service
@Data
public class GameService {
    private Board board;
    private int playerTurn;
    public Player player1;
    public Player player2;
    public Player player3;
    public Player player4;
    public String winner;
    public String gameId;
    public Game game;

    public GameService(Player player, String gameId) {
        board = new Board();
        playerTurn = 1;
        this.player1 = player;
//        gameId = UUID.randomUUID().toString();
        this.gameId = gameId;
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

    public Game reset(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);

        game.setBoard(new Board());
        game.resetPieces(game.getPlayer1());
        game.resetPieces(game.getPlayer2());
        game.resetPieces(game.getPlayer3());
        game.resetPieces(game.getPlayer4());
        switch (winner) {
            case "blue" -> {
                game.setCurrentTurn(1);
                playerTurn = 1;
                System.out.println("reset turn to 1");
            }
            case "red" -> {
                game.setCurrentTurn(2);
                playerTurn = 2;
                System.out.println("reset turn to 2");
            }
            case "green" -> {
                game.setCurrentTurn(3);
                playerTurn = 3;
                System.out.println("reset turn to 3");
            }
            case "yellow" -> {
                game.setCurrentTurn(4);
                playerTurn = 4;
                System.out.println("reset turn to 4");
            }
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

    public Game createGame (String playerName, String gameId) {
        Player player = new Player("blue", playerName, 1);

        GameService gameService = new GameService(player, gameId);

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

//    public Game getGame() {
//        return game;
//    }

    public Game getGameById(String gameId) {
        return GameStorage.getInstance().getGames().get(gameId);
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
