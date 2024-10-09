package com.example.otrio.controller.dto;

import com.example.otrio.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
