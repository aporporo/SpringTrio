const url = 'http://localhost:8080';
let stompClient;
let gameId;
let playerType;

function connectToSocket(gameId) {

    console.log("connecting to the game");
    console.log("before /game/gameMove in connectToSocket");
    let socket = new SockJS(url + "/game/gameMove");
    console.log("after /game/gameMove in connectToSocket");
    stompClient = Stomp.over(socket);
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
        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "playerName": login,
                "color": "blue",
                "playerId": 1
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'X';
                // reset();
                connectToSocket("1");
                alert("Your created a game. Game id is: " + data.gameId);
                gameOn = true;
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
        $.ajax({
            url: url + "/game/connect/random",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                // reset();
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1.login);
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
        let gameId = document.getElementById("game_id").value;
        if (gameId == null || gameId === '') {
            alert("Please enter game id");
        }
        $.ajax({
            url: url + "/game/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "playerName": login,
                "gameId": "1"
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                // reset();
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1.playerName);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}