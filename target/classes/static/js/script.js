async function resetGame() {
    try {
        // Call the backend to reset the game state
        const response = await fetch('/game/reset', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                gameId: gameId,
                playerId: 0,
                row: 0,
                col: 0,
                size: 1

            })
        });

        if (response.ok) {
            console.log('Game has been reset on the server.');

            // // Clear all cells on the game board
            // document.querySelectorAll('.cell').forEach(cell => {
            //     cell.innerHTML = '';  // Clear the cell content
            // });

            // Reset the pieces to their original positions
            const result = await response.json();
            console.log(result);
            resetPieces();
            updateBoard(result);



            console.log('Game has been reset on the frontend.');
        } else {
            console.error('Failed to reset the game on the server.');
        }
    } catch (error) {
        console.error('Error resetting the game:', error);
    }
}

function resetPieces() {
    // Select all pieces in the document
    const allPieces = document.querySelectorAll('.piece');

    allPieces.forEach(piece => {
        const id = piece.id;

        if (/^[a-z]+-[a-z]+-\d+$/.test(id)) { // Matches "color-size-number"
            if (id.startsWith('board-')) {
                piece.classList.add('black'); // Add hidden for "board-size-number"
            } else {
                piece.classList.remove('hidden'); // Remove hidden for "color-size-number"
            }
        }
    });

    console.log('All pieces have been reset.');
}

function rules() {
    alert("Winning Combinations:\n3 pieces of the same size in a row\n3 pieces in ascending or descending order\n3 pieces stacked in the same spot");
}
document.addEventListener('DOMContentLoaded', function () {
    const board = document.getElementById('board');
    const pieces = document.querySelectorAll('.piece');
    const scoreboard = {
        blue: 0,
        red: 0,
        green: 0,
        yellow: 0
    };
    let currentTurn = 'blue'; // Start with blue player
    updateTurnIndicator();

    // Function to update the scoreboard
    function updateScoreboard(player) {
        if (scoreboard.hasOwnProperty(player)) {

            document.getElementById(`${player}-score`).innerText = scoreboard[player];  // Assuming you have elements for displaying scores
        }
    }



    async function checkWinAndUpdateScore() {
        try {
            const response = await fetch('/game/checkWin');
            let winner = await response.text();

            if (winner) {
                let winnerName = "";
                if (winner === "blue") {
                    updateScoreboard("blue");
                    winnerName = document.getElementById("blue-name").innerText;
                } else if (winner === "red") {
                    updateScoreboard("red");
                    winnerName = document.getElementById("red-name").innerText;
                } else if (winner === "green") {
                    updateScoreboard("green");
                    winnerName = document.getElementById("green-name").innerText;
                } else if (winner === "yellow") {
                    updateScoreboard("yellow");
                    winnerName = document.getElementById("yellow-name").innerText;
                }
                alert(`${winner} has won!`);
                winner = null;
                await resetGame();
            } else {
                console.log("No winner yet");
            }
        } catch (error) {
            console.error("Error fetching winner:", error);
        }
    }

    function hideEmptyPlayerRows(data) {
        // Hide player rows for players that don't exist
        document.getElementById("red-player").style.display = data.player2 ? "flex" : "none";
        document.getElementById("green-player").style.display = data.player3 ? "flex" : "none";
        document.getElementById("yellow-player").style.display = data.player4 ? "flex" : "none";
    }
    function updateBoard(data) {
        const board = data.board.board;
        const playerTurn = data.currentTurn;

        const player1Score = data.player1 ? data.player1.wins : 0;
        const player2Score = data.player2 ? data.player2.wins : 0;
        const player3Score = data.player3 ? data.player3.wins : 0;
        const player4Score = data.player4 ? data.player4.wins : 0;

        // Get player names
        const player1Name = data.player1 ? data.player1.playerName : "Player 1";
        const player2Name = data.player2 ? data.player2.playerName : "Player 2";
        const player3Name = data.player3 ? data.player3.playerName : "Player 3";
        const player4Name = data.player4 ? data.player4.playerName : "Player 4";

        advanceTurn(playerTurn);


            document.getElementById(`blue-score`).innerText = player1Score;
            document.getElementById(`red-score`).innerText = player2Score;
            document.getElementById(`green-score`).innerText = player3Score;
            document.getElementById(`yellow-score`).innerText = player4Score;
        // Update player names
        document.getElementById(`blue-name`).innerText = player1Name;
        document.getElementById(`red-name`).innerText = player2Name;
        document.getElementById(`green-name`).innerText = player3Name;
        document.getElementById(`yellow-name`).innerText = player4Name;


        // First, reset all board pieces to black (removing all color classes)
        document.querySelectorAll('[id^="board-"]').forEach(pieceElement => {
            // Remove all color classes
            pieceElement.classList.remove("blue", "red", "green", "yellow");
            // Add black class
            pieceElement.classList.add("black");
        });

        board.forEach((row, rowIndex) => {
            row.forEach((col, colIndex) => {
                col.forEach((cell, cellIndex) => {
                    if (cell) {
                        const { color, size } = cell;
                        const sizeClass = size === 2 ? "large" : size === 1 ? "medium" : "small";
                        const index = rowIndex * 3 + colIndex; // Mapping row and col to data-position

                        // Find the corresponding HTML element
                        const pieceElement = document.querySelector(`#board-${sizeClass}-${index}`);
                        if (pieceElement) {
                            // Remove the "black" class and add the color class
                            pieceElement.classList.remove("black");
                            pieceElement.classList.add(color);
                        }
                    }
                });
            });
        });

        hideEmptyPlayerRows(data);
    }
    window.updateBoard = updateBoard;

    // Track original positions
    const originalPositions = {};

    // Add dragstart event for all pieces
    pieces.forEach(piece => {
        piece.addEventListener('dragstart', (e) => {
            const pieceId = e.target.id;
            const parentCell = e.target.closest('.cell');

            // Store the original position
            if (parentCell) {
                const originalPosition = parentCell.dataset.position;
                originalPositions[pieceId] = {
                    cell: parentCell,
                    position: originalPosition
                };
                console.log('Original position stored:', pieceId, originalPosition);
            } else {
                console.warn('Parent cell not found for piece:', pieceId);
            }

            console.log('Dragging piece:', pieceId);
            e.dataTransfer.setData('piece-id', pieceId);
            e.dataTransfer.effectAllowed = 'move';
        });
    });

    // Allow drop on the board
    board.addEventListener('dragover', (e) => {
        e.preventDefault();
        e.dataTransfer.dropEffect = 'move';
    });

    // Handle the drop event
    board.addEventListener('drop', async (e) => {
        e.preventDefault();

        const pieceId = e.dataTransfer.getData('piece-id');
        const cell = e.target.closest('.cell'); // Drop target

        if (cell && pieceId) {
            const piece = document.getElementById(pieceId);

            if (piece) {
                console.log('Dropped piece:', pieceId, 'on cell:', cell);

                const playerPieceId = piece.classList.contains('blue') ? 1 :
                    piece.classList.contains('red') ? 2 :
                        piece.classList.contains('green') ? 3 :
                            piece.classList.contains('yellow') ? 4 : 'unknown';

// Ensure player is moving their own piece
                if (playerPieceId !== playerId) {
                    alert("You can only move your own pieces!");
                    movePieceBackToOriginalPosition(pieceId, piece, cell);
                    return;
                }


                // Remove the piece from its previous parent
                if (piece.parentElement) {
                    piece.classList.add("hidden");
                }

                // Place the original piece in the new cell
                // cell.appendChild(piece);

                // Center the piece within the cell
                // centerPiece(piece, cell);

                console.log('Piece placed in cell:', cell);

                // Extract player and size information from the piece's classes
                // const playerId = piece.classList.contains('blue') ? 1 :
                //     piece.classList.contains('red') ? 2 :
                //         piece.classList.contains('green') ? 3 :
                //             piece.classList.contains('yellow') ? 4 : 'unknown';

                const size = piece.classList.contains('large') ? 2 :
                    piece.classList.contains('medium') ? 1 :
                        piece.classList.contains('small') ? 0 : 'unknown';

                // Extract row and column from cell's dataset
                const col = cell.dataset.position % 3;
                const row = Math.floor(cell.dataset.position / 3);

                try {
                    // Send the move to the server
                    const response = await fetch('/game/gameMove', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            gameId: gameId,
                            playerId: playerId,
                            row: row,
                            col: col,
                            size: size
                        })
                    });

                    const result = await response.json();
                    // displayResponse(result);
                    updateBoard(result);
                    // advanceTurn();
                    console.log('Move result:', result);

                    // Broadcast the move to other players via WebSocket
                    stompClient.send(`/app/game/${gameId}/move`, {}, JSON.stringify({
                        pieceId: pieceId,
                        playerId: playerId,
                        row: row,
                        col: col,
                        size: size
                    }));

                    // Check win conditions and update the scoreboard
                    checkWinAndUpdateScore();

                } catch (error) {
                    console.error('Error making move:', error);

                    // Move the piece back to its original position on error
                     movePieceBackToOriginalPosition(pieceId, piece, cell);
                }
            } else {
                console.warn('Piece not found with ID:', pieceId);
            }
        }
    });

// Helper function to move the piece back to its original position
    function movePieceBackToOriginalPosition(pieceId, piece, cell) {
        const originalPositionData = originalPositions[pieceId];
        if (originalPositionData) {
            const { cell: originalCell, position } = originalPositionData;
            if (originalCell) {
                console.log('Moving piece back to:', originalCell);

                // Insert the piece back into the original cell
                piece.classList.remove("hidden");

                // Center the piece in its original cell
                centerPiece(piece, originalCell);
            } else {
                console.warn('Original cell not found with position:', position);
            }
        } else {
            console.warn('Original position not found for piece:', pieceId);
        }
    }

    function centerPiece(piece, cell) {
        piece.style.position = 'absolute';
        piece.style.top = '50%';
        piece.style.left = '50%';
        piece.style.transform = 'translate(-50%, -50%)';
        piece.style.zIndex = '1'; // Ensures it displays above the board
    }

    function updateTurnIndicator() {
        // First, remove the active class from all player rows
        document.querySelectorAll('.player-score').forEach(playerRow => {
            playerRow.classList.remove('active');
        });

        // Add the active class to the current player's row
        const currentPlayerRow = document.getElementById(`${currentTurn}-player`);
        if (currentPlayerRow) {
            currentPlayerRow.classList.add('active');
        }

        console.log('Current turn updated to:', currentTurn);
    }

// Function to advance to the next player's turn
    function advanceTurn(playerTurn) {
        const players = ['blue', 'red', 'green', 'yellow'];
        const index = playerTurn - 1;
        currentTurn = players[index];
        updateTurnIndicator();
    }

});