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
    private String winner;
    private String gameId;
    private Game game;

    public GameService(Player player, String gameId) {
        board = new Board();
        this.gameId = gameId;

        this.game = new Game();
        game.addActivePlayer(player.getPlayerId());
        game.setBoard(board);
        game.setGameId(gameId);
        game.setPlayer1(player);
        game.setStatus(NEW);
        game.setCurrentTurn(1);

        GameStorage.getInstance().setGame(game);
    }

    public GameService() {
        board = new Board();

    }

    public Game reset(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);

        // reset board and player pieces
        game.setBoard(new Board());
        game.resetPieces(game.getPlayer1());
        game.resetPieces(game.getPlayer2());
        game.resetPieces(game.getPlayer3());
        game.resetPieces(game.getPlayer4());

        // set turn to previous winner if not null
        if (winner != null) {
            switch (winner) {
                case "blue" -> {
                    game.setCurrentTurn(1);
                }
                case "red" -> {
                    game.setCurrentTurn(2);
                }
                case "green" -> {
                    game.setCurrentTurn(3);
                }
                case "yellow" -> {
                    game.setCurrentTurn(4);
                }
            }
        }

        // clear winner
        this.winner = null;

        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game gameMove (Player player, String gameId, int row, int col, int size) throws InvalidParamException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Cannot find game with matching game id");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        // first half of if statement validates correct player trying to make move, second half returns boolean if the move is valid
        if (player.getPlayerId() == game.getCurrentTurn() && game.getBoard().placePiece(row, col, size, player)) {
            // records the last move made by placePiece in the if statement
            game.setLastMove(new String[]{
                    String.valueOf(row), String.valueOf(col), String.valueOf(size), player.getColor()
            });

            // check for a winner
            if (game.getBoard().getGameWon() == 1) {
                winner = game.getBoard().getWinnerColor();
            }

            // checks list of active players, gets the id of current player, finds position of the current player in the list
            int currentIndex = game.getActivePlayerIds().indexOf(game.getCurrentTurn());

            // moves to the next player, then ensures it wraps around when it reaches the end of the list
            int nextIndex = (currentIndex + 1) % game.getActivePlayerIds().size();

            // gets playerId at nextIndex, so they are the next player to take a turn
            int nextTurn = game.getActivePlayerIds().get(nextIndex);
            game.setCurrentTurn(nextTurn);
        } else {
            throw new InvalidGameException("Error in gameMove method");
        }

        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game createGame (String playerName, String gameId) throws InvalidParamException {
        if (GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("This gameId is already in use");
        }

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

    public Game getGameById(String gameId) {
        return GameStorage.getInstance().getGames().get(gameId);
    }

    public Game connectToGame(String playerName, String gameId) throws InvalidParamException, InvalidGameException {
        Game game = getGameById(gameId);
        if (game == null) {
            throw new InvalidParamException("Cannot find game with matching game id");
        }

        // check if player is rejoining, if so rejoin game from existing player game state
        Player existingPlayer = findExistingPlayer(game, playerName);
        if (existingPlayer != null) {

            if (!game.getActivePlayerIds().contains(existingPlayer.getPlayerId())) {
                game.addActivePlayer(existingPlayer.getPlayerId());
            }
            GameStorage.getInstance().setGame(game);
            return game;
        }

        // add new player to the game
        Player player;
        if (game.getPlayer4() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        } else if (game.getPlayer3() != null) {
            player = new Player("yellow", playerName, 4);
            game.setPlayer4(player);
            game.setStatus(IN_PROGRESS);
        } else if (game.getPlayer2() != null) {
            player = new Player("green", playerName, 3);
            game.setPlayer3(player);
        } else {
            player = new Player("red", playerName, 2);
            game.setPlayer2(player);
        }

        game.addActivePlayer(player.getPlayerId());

        GameStorage.getInstance().setGame(game);
        return game;

    }

    private Player findExistingPlayer(Game game, String playerName) {
        // Check player slots in order
        if (game.getPlayer1() != null && game.getPlayer1().getPlayerName().equals(playerName)) {
            return game.getPlayer1();
        }
        if (game.getPlayer2() != null && game.getPlayer2().getPlayerName().equals(playerName)) {
            return game.getPlayer2();
        }
        if (game.getPlayer3() != null && game.getPlayer3().getPlayerName().equals(playerName)) {
            return game.getPlayer3();
        }
        if (game.getPlayer4() != null && game.getPlayer4().getPlayerName().equals(playerName)) {
            return game.getPlayer4();
        }
        return null;
    }

    public Game connectToRandomGame(String playerName) throws NotFoundException, InvalidGameException {
        // Find a game that is not full (has at least one empty player slot)
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(NEW))
                .filter(it -> it.getPlayer4() == null) // Filter games that are not full
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No open games found. Please create a new game."));

        // Add player to first available slot
        Player player;
        if (game.getPlayer3() != null) {
            player = new Player("yellow", playerName, 4);
            game.setPlayer4(player);
            game.setStatus(IN_PROGRESS);
        } else if (game.getPlayer2() != null) {
            player = new Player("green", playerName, 3);
            game.setPlayer3(player);
        } else if (game.getPlayer1() != null) {
            player = new Player("red", playerName, 2);
            game.setPlayer2(player);
        } else {
            throw new InvalidGameException("Invalid game state: No players in game");
        }

        // Add player to active players list
        game.addActivePlayer(player.getPlayerId());

        // Update game in storage
        GameStorage.getInstance().setGame(game);
        return game;
    }
}
