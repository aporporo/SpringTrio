package com.example.otrio.controller.dto;

import com.example.otrio.model.Player;
import lombok.Data;

@Data
public class GameMoveRequest {
    private Player player;
    private String gameId;
    private int row;
    private int col;
    private int size;
    private int playerId;

}
