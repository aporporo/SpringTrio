package com.example.otrio.controller;

import com.example.otrio.controller.dto.ConnectRequest;
import com.example.otrio.exception.InvalidGameException;
import com.example.otrio.exception.InvalidParamException;
import com.example.otrio.exception.NotFoundException;
import com.example.otrio.model.Game;
import com.example.otrio.model.Piece;
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
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

//    public GameController(GameService gameService) {
//        this.gameService = gameService;
//    }

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
        log.info("connect request: {}", request);
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameId()));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
        log.info("connect random {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }

    @PostMapping("/move")
    public Boolean makeMove(@RequestParam int row, @RequestParam int col, @RequestParam String color, @RequestParam int size) {

        Player player = gameService.getPlayerByColor(color);
        return gameService.makeMove(row, col, size, player);
    }

    @GetMapping("/checkWin")
    public String getWinner() {
        String player = gameService.getWinner();
        if (player == null) {
            return null;
        }
        return player;
    }

//    @GetMapping("/status")
//    @ResponseBody
//    public Piece[][] getBoard() {
//        return gameService.getBoard();
//    }

    @PostMapping("/reset")
    public String resetGame() {
        gameService.reset();
        return "Game has been reset.";
    }

//    @GetMapping("/currentTurn")
//    public String getCurrentTurn() {
//        return gameService.getCurrentTurn();
//    }
}
