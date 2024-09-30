package com.example.otrio.model;

import java.util.Arrays;

public class Board {
    private Piece[][][] board;
    public int gameWon = 0;
    public String winnerColor;

    public Board() {
        board = new Piece[3][3][3];
        reset();  // Initialize the board to the default state
    }

    // Reset the board to its initial empty state
    public void reset() {
        gameWon = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int size = 0; size < 3; size++) {
                    board[row][col][size] = null;  // Clear each cell
                }

            }
        }
    }

    // Place a piece on the board at the specified row and column
    public boolean placePiece(int row, int col, int size, Player player) {
        int count = 0;
        Piece piece;

        if (gameWon == 1 || row < 0 || row >= 3 || col < 0 || col >= 3 || size < 0 || size >= 3) {
            return false;  // Invalid position
        }

        Piece existingPiece = board[row][col][size];
        if (existingPiece == null) {

            if (size == 0 && player.checkPieces(1)) {
                while (player.piece[count] != null) {
                    count++;
                    if (count >= 9) {
                        return false;
                    }
                }


                player.piece[count] = new Piece(player.color, 1);
            } else if (size == 1 && player.checkPieces(2)) {
                while (player.piece[count] != null) {
                    count++;
                    if (count >= 9) {
                        return false;
                    }
                }
                player.piece[count] = new Piece(player.color, 2);
            } else if (size == 2 & player.checkPieces(3)) {
                while (player.piece[count] != null) {
                    count++;
                    if (count >= 9) {
                        return false;
                    }
                }
                player.piece[count] = new Piece(player.color, 3);
            }
            board[row][col][size] = player.piece[count];
            if (checkWin(player)) {
                gameWon = 1;
                winnerColor = player.color;

                System.out.println(player.color + "HAS WON");
            }
            return true;
        }

        return false;  // Cell is occupied by a piece of the same size
    }

        // Check if there's a winning condition on the board
    public boolean checkWin(Player player) {
//        return checkRows(player) || checkColumns(player) || checkDiagonals(player) || checkStackedWin(player);
        return checkRows(player) || checkColumns(player) || checkDiagonals(player) || checkStackedWin(player);
    }


    // Check if a player has won by placing pieces of size 1, 2, and 3 in distinct columns of the same row
    private boolean checkRows(Player player) {
        for (int row = 0; row < 3; row++) {
            int size1Col = -1, size2Col = -1, size3Col = -1;
            int size1Col2 = -2, size2Col2 = -2, size3Col2 = -2;
            int size1Col3 = -3, size2Col3 = -3, size3Col3 = -3;

            // Check all columns in this row for pieces of sizes 1, 2, and 3
            for (int col = 0; col < 3; col++) {
                if (board[row][col][0] != null && board[row][col][0].getColor().equals(player.color)) {
                    if (size1Col == -1) {
                        size1Col = col;  // Found size 1 piece for this player
//                        System.out.println("size1col = " + size1Col + " " + col);
                    } else if (size1Col != col && size1Col2 == -2) {
                        size1Col2 = col;
//                        System.out.println("size1col2 = " + size1Col2 + " " + col);
                    } else if (size1Col2 != -2 && size1Col != col && size1Col2 != col && size1Col3 == -3){
                        size1Col3 = col;
//                        System.out.println("size1col3 = " + size1Col3 + " " + col);
                    }



                }
                if (board[row][col][1] != null && board[row][col][1].getColor().equals(player.color)) {
                    if (size2Col == -1) {
                        size2Col = col;  // Found size 1 piece for this player
                    } else if (size2Col != col && size2Col2 == -2) {
                        size2Col2 = col;
//                        System.out.println("size1col2 = " + size1Col2 + " " + col);
                    } else if (size2Col2 != -2 && size2Col != col && size2Col2 != col && size2Col3 == -3){
                        size2Col3 = col;
//                        System.out.println("size1col3 = " + size1Col3 + " " + col);
                    }

                }
                if (board[row][col][2] != null && board[row][col][2].getColor().equals(player.color)) {
                    if (size3Col == -1) {
                        size3Col = col;  // Found size 1 piece for this player
                    } else if (size3Col != col && size3Col2 == -2) {
                        size3Col2 = col;
//                        System.out.println("size1col2 = " + size1Col2 + " " + col);
                    } else if (size3Col2 != -2 && size3Col != col && size3Col2 != col && size3Col3 == -3){
                        size3Col3 = col;
//                        System.out.println("size1col3 = " + size1Col3 + " " + col);
                    }

                }
            }

            // Check if size1, size2, and size3 are found and placed in different columns
//            if (size1Col != -1 && size2Col != -1 && size3Col != -1 &&
//                    size1Col != size2Col && size1Col != size3Col && size2Col != size3Col) {
//                return true;  // Player wins if all sizes are placed in distinct columns in the same row
//            }
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




    private boolean checkColumns(Player player) {
        for (int col = 0; col < 3; col++) {
            int size1Row = -1, size2Row = -1, size3Row = -1;
            int size1Row2 = -2, size2Row2 = -2, size3Row2 = -2;
            int size1Row3 = -3, size2Row3 = -3, size3Row3 = -3;

            // Check all rows in this column for pieces of sizes 1, 2, and 3
            for (int row = 0; row < 3; row++) {
                if (board[row][col][0] != null && board[row][col][0].getColor().equals(player.color)) {
                    if (size1Row == -1) {
                        size1Row = row;  // Found size 1 piece for this player
                    } else if (size1Row != row && size1Row2 == -2) {
                        size1Row2 = row;
                    } else if (size1Row2 != -2 && size1Row != row && size1Row2 != row && size1Row3 == -3) {
                        size1Row3 = row;
                    }
                }
                if (board[row][col][1] != null && board[row][col][1].getColor().equals(player.color)) {
                    if (size2Row == -1) {
                        size2Row = row;  // Found size 2 piece for this player
                    } else if (size2Row != row && size2Row2 == -2) {
                        size2Row2 = row;
                    } else if (size2Row2 != -2 && size2Row != row && size2Row2 != row && size2Row3 == -3) {
                        size2Row3 = row;
                    }
                }
                if (board[row][col][2] != null && board[row][col][2].getColor().equals(player.color)) {
                    if (size3Row == -1) {
                        size3Row = row;  // Found size 3 piece for this player
                    } else if (size3Row != row && size3Row2 == -2) {
                        size3Row2 = row;
                    } else if (size3Row2 != -2 && size3Row != row && size3Row2 != row && size3Row3 == -3) {
                        size3Row3 = row;
                    }
                }
            }

            // Check if size1, size2, and size3 are found and placed in different rows
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



    //    // Check if a player has won by placing pieces of size 1, 2, and 3 diagonally
//    private boolean checkDiagonals(Player player) {
//        // Check top-left to bottom-right diagonal
//        int size1Row = -1, size2Row = -1, size3Row = -1;
//
//        for (int i = 0; i < 3; i++) {
//            if (board[i][i][0] != null && board[i][i][0].getColor().equals(player.color)) {
//                size1Row = i;  // Found size 1 piece in this diagonal for the player
//            }
//            if (board[i][i][1] != null && board[i][i][1].getColor().equals(player.color)) {
//                size2Row = i;  // Found size 2 piece in this diagonal for the player
//            }
//            if (board[i][i][2] != null && board[i][i][2].getColor().equals(player.color)) {
//                size3Row = i;  // Found size 3 piece in this diagonal for the player
//            }
//        }
//
//        // Check if all sizes are found and placed in distinct rows (diagonal from top-left to bottom-right)
//        if (size1Row != -1 && size2Row != -1 && size3Row != -1 &&
//                size1Row != size2Row && size1Row != size3Row && size2Row != size3Row) {
//            return true;  // Player wins if all sizes are placed in distinct rows in this diagonal
//        }
//
//        // Check top-right to bottom-left diagonal
//        size1Row = -1; size2Row = -1; size3Row = -1;
//
//        for (int i = 0; i < 3; i++) {
//            if (board[i][2 - i][0] != null && board[i][2 - i][0].getColor().equals(player.color)) {
//                size1Row = i;  // Found size 1 piece in this diagonal for the player
//            }
//            if (board[i][2 - i][1] != null && board[i][2 - i][1].getColor().equals(player.color)) {
//                size2Row = i;  // Found size 2 piece in this diagonal for the player
//            }
//            if (board[i][2 - i][2] != null && board[i][2 - i][2].getColor().equals(player.color)) {
//                size3Row = i;  // Found size 3 piece in this diagonal for the player
//            }
//        }
//
//        // Check if all sizes are found and placed in distinct rows (diagonal from top-right to bottom-left)
//        if (size1Row != -1 && size2Row != -1 && size3Row != -1 &&
//                size1Row != size2Row && size1Row != size3Row && size2Row != size3Row) {
//            return true;  // Player wins if all sizes are placed in distinct rows in this diagonal
//        }
//
//        return false;
//    }
    private boolean checkDiagonals(Player player) {
        // Check top-left to bottom-right diagonal
        int size1Index = -1, size2Index = -1, size3Index = -1;
        int size1Index2 = -2, size2Index2 = -2, size3Index2 = -2;
        int size1Index3 = -3, size2Index3 = -3, size3Index3 = -3;

        for (int i = 0; i < 3; i++) {
            if (board[i][i][0] != null && board[i][i][0].getColor().equals(player.color)) {
                if (size1Index == -1) {
                    size1Index = i;  // Found size 1 piece in this diagonal for the player
                } else if (size1Index != i && size1Index2 == -2) {
                    size1Index2 = i;
                } else if (size1Index2 != -2 && size1Index != i && size1Index2 != i && size1Index3 == -3) {
                    size1Index3 = i;
                }
            }
            if (board[i][i][1] != null && board[i][i][1].getColor().equals(player.color)) {
                if (size2Index == -1) {
                    size2Index = i;  // Found size 2 piece in this diagonal for the player
                } else if (size2Index != i && size2Index2 == -2) {
                    size2Index2 = i;
                } else if (size2Index2 != -2 && size2Index != i && size2Index2 != i && size2Index3 == -3) {
                    size2Index3 = i;
                }
            }
            if (board[i][i][2] != null && board[i][i][2].getColor().equals(player.color)) {
                if (size3Index == -1) {
                    size3Index = i;  // Found size 3 piece in this diagonal for the player
                } else if (size3Index != i && size3Index2 == -2) {
                    size3Index2 = i;
                } else if (size3Index2 != -2 && size3Index != i && size3Index2 != i && size3Index3 == -3) {
                    size3Index3 = i;
                }
            }
        }

        // Check if all sizes are found and placed in distinct indices (diagonal from top-left to bottom-right)
        if ((size1Index == 0 && size2Index == 1 && size3Index == 2) ||
                (size1Index == 2 && size2Index == 1 && size3Index == 0) ||
                (size1Index == 0 && size1Index2 == 1 && size1Index3 == 2) ||
                (size2Index == 0 && size2Index2 == 1 && size2Index3 == 2) ||
                (size3Index == 0 && size3Index2 == 1 && size3Index3 == 2)) {
            return true;  // Player wins if all sizes are placed in distinct positions in this diagonal
        }

        // Check top-right to bottom-left diagonal
        size1Index = -1; size2Index = -1; size3Index = -1;
        size1Index2 = -2; size2Index2 = -2; size3Index2 = -2;
        size1Index3 = -3; size2Index3 = -3; size3Index3 = -3;

        for (int i = 0; i < 3; i++) {
            if (board[i][2 - i][0] != null && board[i][2 - i][0].getColor().equals(player.color)) {
                if (size1Index == -1) {
                    size1Index = i;  // Found size 1 piece in this diagonal for the player
                } else if (size1Index != i && size1Index2 == -2) {
                    size1Index2 = i;
                } else if (size1Index2 != -2 && size1Index != i && size1Index2 != i && size1Index3 == -3) {
                    size1Index3 = i;
                }
            }
            if (board[i][2 - i][1] != null && board[i][2 - i][1].getColor().equals(player.color)) {
                if (size2Index == -1) {
                    size2Index = i;  // Found size 2 piece in this diagonal for the player
                } else if (size2Index != i && size2Index2 == -2) {
                    size2Index2 = i;
                } else if (size2Index2 != -2 && size2Index != i && size2Index2 != i && size2Index3 == -3) {
                    size2Index3 = i;
                }
            }
            if (board[i][2 - i][2] != null && board[i][2 - i][2].getColor().equals(player.color)) {
                if (size3Index == -1) {
                    size3Index = i;  // Found size 3 piece in this diagonal for the player
                } else if (size3Index != i && size3Index2 == -2) {
                    size3Index2 = i;
                } else if (size3Index2 != -2 && size3Index != i && size3Index2 != i && size3Index3 == -3) {
                    size3Index3 = i;
                }
            }
        }

        // Check if all sizes are found and placed in distinct indices (diagonal from top-right to bottom-left)
        if ((size1Index == 0 && size2Index == 1 && size3Index == 2) ||
                (size1Index == 2 && size2Index == 1 && size3Index == 0) ||
                (size1Index == 0 && size1Index2 == 1 && size1Index3 == 2) ||
                (size2Index == 0 && size2Index2 == 1 && size2Index3 == 2) ||
                (size3Index == 0 && size3Index2 == 1 && size3Index3 == 2)) {
            return true;  // Player wins if all sizes are placed in distinct positions in this diagonal
        }

        return false;
    }



    // Check if a player has a stacked win (all pieces of different sizes in the same cell)
    private boolean checkStackedWin(Player player) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col][0] != null && board[row][col][1] != null && board[row][col][2] != null) {
                    if (board[row][col][0].getColor().equals(player.color) &&
                            board[row][col][1].getColor().equals(player.color) &&
                            board[row][col][2].getColor().equals(player.color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Get the current state of the board
    public Piece[][][] getBoard() {
        return board;
    }
    public void printBoard() {
        board = getBoard();
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                for (int k = 0; k < 3; k++) {
//                    System.out.print(board[i][j][k] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }

        System.out.println(Arrays.deepToString(board));
    }

    public void visualizeBoard() {
        board = getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print("[" + board[i][j][0] + ", " + board[i][j][1] + ", " + board[i][j][2] + "]");
            }
            System.out.println();
        }
    }
}
