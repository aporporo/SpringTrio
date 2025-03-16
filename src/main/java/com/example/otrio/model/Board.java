package com.example.otrio.model;

import lombok.Getter;

@Getter
public class Board {

    private Piece[][][] board;
    private int gameWon = 0;
    private String winnerColor;

    // makes the 3x3x3 board
    public Board() {
        board = new Piece[3][3][3];
    }

    // This will place a piece on the board if the conditions are met
    public boolean placePiece(int row, int col, int size, Player player) {
        // Checks if the game is already won or if the position is invalid
        if (gameWon == 1 || row < 0 || row >= 3 || col < 0 || col >= 3 || size < 0 || size >= 3) {
            return false;
        }

        // Checks if there is a piece already placed in the position of this new piece
        Piece existingPiece = board[row][col][size];
        if (existingPiece != null) {
            return false;
        }

        // Checks if the player has available pieces of the given size
        if (!player.checkPieces(size)) {
            System.out.println("Too many pieces of size " + size);
            return false;
        }

        // Checks if user has used all of their pieces
        int count = 0;
        while (player.getPiece()[count] != null) {
            count++;
            if (count >= player.getPiece().length) {
                System.out.println("Player piece array is full!");
                return false;
            }
        }

        // Adds the piece to the players list of used pieces
        player.getPiece()[count] = new Piece(player.getColor(), size);
        System.out.println("Piece added to player");

        // Adds the piece to the board
        board[row][col][size] = player.getPiece()[count];
        System.out.println("Piece added to board");

        // Calls the checkWin method to see if the player has met a winning condition
        if (checkWin(player)) {
            gameWon = 1;
            winnerColor = player.getColor();
            System.out.println(player.getColor() + " HAS WON");
            player.playerWon();
        }

        return true;
    }

    /*
     * Checks if the player has met a winning condition by calling helper functions to check rows, columns, diagonals,
     * or stacked winning combinations
    */
    public boolean checkWin(Player player) {
        return checkRows(player) || checkColumns(player) || checkDiagonals(player) || checkStackedWin(player);
    }

    /*
    * Checks if the player has a winning combination in a row
    * This can be 3 pieces of the same size in a row or pieces in ascending or descending order by size in a row
    * */
    private boolean checkRows(Player player) {
        // iterate through each row
        for (int row = 0; row < 3; row++) {
            // variables to track column positions of players pieces for diff sizes
            int size1Col = -1, size2Col = -1, size3Col = -1;
            int size1Col2 = -2, size2Col2 = -2, size3Col2 = -2;
            int size1Col3 = -3, size2Col3 = -3, size3Col3 = -3;

            // iterate through each column in the current row
            for (int col = 0; col < 3; col++) {

                // check if player has placed a small piece in this row
                if (board[row][col][0] != null && board[row][col][0].color().equals(player.getColor())) {
                    if (size1Col == -1) {
                        size1Col = col;
                    } else if (size1Col != col && size1Col2 == -2) {
                        size1Col2 = col;
                    } else if (size1Col2 != -2 && size1Col != col && size1Col2 != col && size1Col3 == -3){
                        size1Col3 = col;
                    }
                }

                // check if player has placed a medium piece
                if (board[row][col][1] != null && board[row][col][1].color().equals(player.getColor())) {
                    if (size2Col == -1) {
                        size2Col = col;
                    } else if (size2Col != col && size2Col2 == -2) {
                        size2Col2 = col;
                    } else if (size2Col2 != -2 && size2Col != col && size2Col2 != col && size2Col3 == -3){
                        size2Col3 = col;
                    }

                }

                // check if player placed a large piece
                if (board[row][col][2] != null && board[row][col][2].color().equals(player.getColor())) {
                    if (size3Col == -1) {
                        size3Col = col;
                    } else if (size3Col != col && size3Col2 == -2) {
                        size3Col2 = col;
                    } else if (size3Col2 != -2 && size3Col != col && size3Col2 != col && size3Col3 == -3){
                        size3Col3 = col;
                    }
                }
            }

            // check if the row contains a winning combination
            if (size1Col == 0 && size2Col == 1 && size3Col == 2) {
                System.out.println("Acesnd");
                return true;
            } else if (size1Col == 2 && size2Col == 1 && size3Col == 0) {
                System.out.println("Decsnd");
                return true;
            } else if (size1Col == 0 && size1Col2 == 1 && size1Col3 == 2) {
                System.out.println("small row");
                return true;
            } else if (size2Col == 0 && size2Col2 == 1 && size2Col3 == 2) {
                System.out.println("medium row");
                return true;
            } else if (size3Col == 0 && size3Col2 == 1 && size3Col3 == 2) {
                System.out.println("large row");
                return true;
            }
        }
        return false;
    }


    /*
     * checks if player has a winning combo in any column
     * logic is the same as checkRows but will check vertically
     */
    private boolean checkColumns(Player player) {
        for (int col = 0; col < 3; col++) {
            int size1Row = -1, size2Row = -1, size3Row = -1;
            int size1Row2 = -2, size2Row2 = -2, size3Row2 = -2;
            int size1Row3 = -3, size2Row3 = -3, size3Row3 = -3;

            for (int row = 0; row < 3; row++) {
                if (board[row][col][0] != null && board[row][col][0].color().equals(player.getColor())) {
                    if (size1Row == -1) {
                        size1Row = row;
                    } else if (size1Row != row && size1Row2 == -2) {
                        size1Row2 = row;
                    } else if (size1Row2 != -2 && size1Row != row && size1Row2 != row && size1Row3 == -3) {
                        size1Row3 = row;
                    }
                }
                if (board[row][col][1] != null && board[row][col][1].color().equals(player.getColor())) {
                    if (size2Row == -1) {
                        size2Row = row;
                    } else if (size2Row != row && size2Row2 == -2) {
                        size2Row2 = row;
                    } else if (size2Row2 != -2 && size2Row != row && size2Row2 != row && size2Row3 == -3) {
                        size2Row3 = row;
                    }
                }
                if (board[row][col][2] != null && board[row][col][2].color().equals(player.getColor())) {
                    if (size3Row == -1) {
                        size3Row = row;
                    } else if (size3Row != row && size3Row2 == -2) {
                        size3Row2 = row;
                    } else if (size3Row2 != -2 && size3Row != row && size3Row2 != row && size3Row3 == -3) {
                        size3Row3 = row;
                    }
                }
            }

            if (size1Row == 0 && size2Row == 1 && size3Row == 2) {
                System.out.println("Ascending");
                return true;
            } else if (size1Row == 2 && size2Row == 1 && size3Row == 0) {
                System.out.println("Descending");
                return true;
            } else if (size1Row == 0 && size1Row2 == 1 && size1Row3 == 2) {
                System.out.println("Small column");
                return true;
            } else if (size2Row == 0 && size2Row2 == 1 && size2Row3 == 2) {
                System.out.println("Medium column");
                return true;
            } else if (size3Row == 0 && size3Row2 == 1 && size3Row3 == 2) {
                System.out.println("Large column");
                return true;
            }
        }

        return false;
    }

    /*
     * checks if player has a winning diagonal combination
     * same idea as checkRows and checkColumns, a winning combo can be 3 pieces of same size or pieces is ascending
     * or descending order in a diagonal
     */
    private boolean checkDiagonals(Player player) {
        int size1Index = -1, size2Index = -1, size3Index = -1;
        int size1Index2 = -2, size2Index2 = -2, size3Index2 = -2;
        int size1Index3 = -3, size2Index3 = -3, size3Index3 = -3;

        // this will check the main diagonal, top left to bottom right
        for (int i = 0; i < 3; i++) {
            if (board[i][i][0] != null && board[i][i][0].color().equals(player.getColor())) {
                if (size1Index == -1) {
                    size1Index = i;
                } else if (size1Index != i && size1Index2 == -2) {
                    size1Index2 = i;
                } else if (size1Index2 != -2 && size1Index != i && size1Index2 != i && size1Index3 == -3) {
                    size1Index3 = i;
                }
            }
            if (board[i][i][1] != null && board[i][i][1].color().equals(player.getColor())) {
                if (size2Index == -1) {
                    size2Index = i;
                } else if (size2Index != i && size2Index2 == -2) {
                    size2Index2 = i;
                } else if (size2Index2 != -2 && size2Index != i && size2Index2 != i && size2Index3 == -3) {
                    size2Index3 = i;
                }
            }
            if (board[i][i][2] != null && board[i][i][2].color().equals(player.getColor())) {
                if (size3Index == -1) {
                    size3Index = i;
                } else if (size3Index != i && size3Index2 == -2) {
                    size3Index2 = i;
                } else if (size3Index2 != -2 && size3Index != i && size3Index2 != i && size3Index3 == -3) {
                    size3Index3 = i;
                }
            }
        }


        if ((size1Index == 0 && size2Index == 1 && size3Index == 2) ||
                (size1Index == 2 && size2Index == 1 && size3Index == 0) ||
                (size1Index == 0 && size1Index2 == 1 && size1Index3 == 2) ||
                (size2Index == 0 && size2Index2 == 1 && size2Index3 == 2) ||
                (size3Index == 0 && size3Index2 == 1 && size3Index3 == 2)) {
            return true;
        }

        // resets the variables and begins to check the other diagonal, bottom left to top right
        size1Index = -1; size2Index = -1; size3Index = -1;
        size1Index2 = -2; size2Index2 = -2; size3Index2 = -2;
        size1Index3 = -3; size2Index3 = -3; size3Index3 = -3;

        for (int i = 0; i < 3; i++) {
            if (board[i][2 - i][0] != null && board[i][2 - i][0].color().equals(player.getColor())) {
                if (size1Index == -1) {
                    size1Index = i;
                } else if (size1Index != i && size1Index2 == -2) {
                    size1Index2 = i;
                } else if (size1Index2 != -2 && size1Index != i && size1Index2 != i && size1Index3 == -3) {
                    size1Index3 = i;
                }
            }
            if (board[i][2 - i][1] != null && board[i][2 - i][1].color().equals(player.getColor())) {
                if (size2Index == -1) {
                    size2Index = i;
                } else if (size2Index != i && size2Index2 == -2) {
                    size2Index2 = i;
                } else if (size2Index2 != -2 && size2Index != i && size2Index2 != i && size2Index3 == -3) {
                    size2Index3 = i;
                }
            }
            if (board[i][2 - i][2] != null && board[i][2 - i][2].color().equals(player.getColor())) {
                if (size3Index == -1) {
                    size3Index = i;
                } else if (size3Index != i && size3Index2 == -2) {
                    size3Index2 = i;
                } else if (size3Index2 != -2 && size3Index != i && size3Index2 != i && size3Index3 == -3) {
                    size3Index3 = i;
                }
            }
        }

        if ((size1Index == 0 && size2Index == 1 && size3Index == 2) ||
                (size1Index == 2 && size2Index == 1 && size3Index == 0) ||
                (size1Index == 0 && size1Index2 == 1 && size1Index3 == 2) ||
                (size2Index == 0 && size2Index2 == 1 && size2Index3 == 2) ||
                (size3Index == 0 && size3Index2 == 1 && size3Index3 == 2)) {
            return true;
        }

        return false;
    }

    /*
     * Checks if player has stacked all three sizes in a single spot on the board
     * 1 small, 1 medium, 1 large piece of the same colour in the same spot would be a winning combo
     */
    private boolean checkStackedWin(Player player) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col][0] != null && board[row][col][1] != null && board[row][col][2] != null) {
                    if (board[row][col][0].color().equals(player.getColor()) &&
                            board[row][col][1].color().equals(player.getColor()) &&
                            board[row][col][2].color().equals(player.getColor())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
