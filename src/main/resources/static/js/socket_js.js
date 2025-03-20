const url = 'https://springtrio.onrender.com/';
let stompClient;
let gameId;
let playerType;
let playerId;
let currentSocket = null;
let currentStompClient = null;

function connectToSocket(gameId) {

    disconnectFromSocket();

    console.log("connecting to the game");
    console.log("before /game/gameMove in connectToSocket");
    let socket = new SockJS(url + "/game/gameMove");
    currentSocket = socket;
    console.log("after /game/gameMove in connectToSocket");
    stompClient = Stomp.over(socket);
    currentStompClient = stompClient;
    console.log("after stompClient in connectToSocket");
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            updateBoard(data);

        })

        // Subscribe to game reset events
        stompClient.subscribe("/topic/game-reset/" + gameId, function (message) {
            const game = JSON.parse(message.body);
            console.log("Game reset received:", game);

            // Reset game state and update UI
            resetPieces();
            updateBoard(game);
        });
    })
}



function create_game() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        let game_Id = document.getElementById("game_id").value;
        gameId = game_Id;
        if (game_Id == null || game_Id === '') {
            alert("Please enter game id");
        }

        disconnectFromSocket();

        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "playerName": login,
                "gameId": gameId
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'X';
                // reset();
                connectToSocket(gameId);
                alert(login + " created a game. Game id is: " + data.gameId);
                playerId = 1;
                console.log(`Current Player ID set to: ${playerId}`)
                gameOn = true;
                updateBoard(data);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}


function connectToRandom() {
    let login = document.getElementById("login").value;

    if (login == null || login === '') {
        alert("Please enter login");
    } else {

        disconnectFromSocket();

        $.ajax({
            url: url + "/game/connect/random",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "playerName": login,
                "gameId": ""
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                // reset();
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1.login);

                // find the player object that matches the current player name
                for (let key of ["player1", "player2", "player3", "player4"]) {
                    if (data[key] && data[key].playerName == login) {
                        playerId = data[key].playerId;
                        console.log(`Current Player ID set to: ${playerId}`)
                        break;
                    }
                }
                updateBoard(data);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function connectToSpecificGame() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        let game_Id = document.getElementById("game_id").value;
        gameId = game_Id;
        if (gameId == null || gameId === '') {
            alert("Please enter game id");
        }

        disconnectFromSocket();

        $.ajax({
            url: url + "/game/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "playerName": login,
                "gameId": gameId
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                // reset();
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1.playerName);

                // find the player object that matches the current player name
                for (let key of ["player1", "player2", "player3", "player4"]) {
                    if (data[key] && data[key].playerName == login) {
                        playerId = data[key].playerId;
                        console.log(`Current Player ID set to: ${playerId}`)
                        break;
                    }
                }
                 updateBoard(data);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

// Function to explicitly disconnect
function disconnectFromSocket() {
    if (currentStompClient && currentStompClient.connected) {
        console.log("Disconnecting existing STOMP client");
        currentStompClient.disconnect(function() {
            console.log("Disconnected successfully");
        });
    }
    currentStompClient = null;
    currentSocket = null;
}