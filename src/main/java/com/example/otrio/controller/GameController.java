package com.example.otrio.controller;

import com.example.otrio.controller.dto.ConnectRequest;
import com.example.otrio.controller.dto.GameMoveRequest;
import com.example.otrio.exception.InvalidGameException;
import com.example.otrio.exception.InvalidParamException;
import com.example.otrio.exception.NotFoundException;
import com.example.otrio.model.Game;
import com.example.otrio.model.Player;
import com.example.otrio.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game") // base path for all endpoints
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * This will start a new game with the player as the host
     * @param request The player initiating the game
     * @return The created game
     */
    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody ConnectRequest request) throws InvalidParamException {
        log.info("start game request: {}", request);
//        return ResponseEntity.ok(gameService.createGame(request.getPlayerName(), request.getGameId()));
        Game game = gameService.createGame(request.getPlayerName(), request.getGameId());

        // Broadcast new game creation
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);

        return ResponseEntity.ok(game);
    }

    /**
     * Connects a player to an existing game using a ConnectRequest which contains playerName and gameId
     * @param request Contains playerName and gameId
     * @return The updated game state
     * @throws InvalidParamException If params are incorrect
     * @throws InvalidGameException If the game is incorrect or doesn't exist
     */
    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
        log.info("connect request: {}", request);
//        return ResponseEntity.ok(gameService.connectToGame(request.getPlayerName(), request.getGameId()));
        Game game = gameService.connectToGame(request.getPlayerName(), request.getGameId());

        // Broadcast to all clients that a new player has joined
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);

        return ResponseEntity.ok(game);
    }

    /**
     * Connects a player to a random available game
     * @param player the player trying to join
     * @return The updated game state
     * @throws NotFoundException If no available games are found
     */
    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
        log.info("connect random {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }

    /**
     * This will process a player's move and update the game state
     * Sends an update through WebSockets to notify other players
     * @param moveRequest Contains player, gameId, playerId, row, column, size
     * @return the updated game state
     * @throws InvalidParamException If move params are incorrect
     * @throws InvalidGameException if the move is not allowed
     */
    @PostMapping("/gameMove")
    public ResponseEntity<Game> gameMove(@RequestBody GameMoveRequest moveRequest)
            throws InvalidParamException, InvalidGameException {
        // Get game and player details
        Game game = gameService.getGameById(moveRequest.getGameId());
        Player player = gameService.findPlayerById(game, moveRequest.getPlayerId());

        // Process move
        gameService.gameMove(player, moveRequest.getGameId(), moveRequest.getRow(), moveRequest.getCol(), moveRequest.getSize());

        // send real-time update to subscribed clients
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);

        return ResponseEntity.ok(game);
    }

    /**
     * Reset the game and notifies all players in real-time
     * @return the reset game state
     */
    @PostMapping("/reset")
    public ResponseEntity<Game> reset(@RequestBody GameMoveRequest moveRequest) {
        Game game = gameService.getGameById(moveRequest.getGameId());
        gameService.reset(moveRequest.getGameId());

        // Update clients about reset
        simpMessagingTemplate.convertAndSend("/topic/game-reset/" + game.getGameId(), game);

        return ResponseEntity.ok(game);

    }


    @GetMapping("/checkWin")
    public String getWinner() {
        String player = gameService.getWinner();
        if (player == null) {
            return null;
        }
        return player;
    }


}
