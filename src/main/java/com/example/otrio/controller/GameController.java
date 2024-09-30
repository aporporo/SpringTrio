package com.example.otrio.controller;

import com.example.otrio.model.Piece;
import com.example.otrio.model.Player;
import com.example.otrio.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
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
