public class Board {
    private Square[][] board;
    private char turn;
    String errorMessage = "";

    /*
        Konstruktor
     */
    public Board() {
        generateBoard();
        turn = 'w';
    }

    /*
        Fills board with Square-Objects, containing Piece-Objects
     */
    private void generateBoard() {
        board = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch(i) {
                    case 2: case 3: case 4: case 5:
                        board[i][j] = new Square(); break;
                    case 1:
                        board[i][j] = new Square(new Piece('p', 'w')); break;
                    case 6:
                        board[i][j] = new Square(new Piece('p', 'b')); break;
                    case 0:
                        switch (j) {
                            case 0, 7 -> board[i][j] = new Square(new Piece('R', 'w'));
                            case 1, 6 -> board[i][j] = new Square(new Piece('N', 'w'));
                            case 2, 5 -> board[i][j] = new Square(new Piece('B', 'w'));
                            case 3 -> board[i][j] = new Square(new Piece('Q', 'w'));
                            case 4 -> board[i][j] = new Square(new Piece('K', 'w'));
                        }
                        break;
                    case 7:
                        switch (j) {
                            case 0, 7 -> board[i][j] = new Square(new Piece('R', 'b'));
                            case 1, 6 -> board[i][j] = new Square(new Piece('N', 'b'));
                            case 2, 5 -> board[i][j] = new Square(new Piece('B', 'b'));
                            case 3 -> board[i][j] = new Square(new Piece('Q', 'b'));
                            case 4 -> board[i][j] = new Square(new Piece('K', 'b'));
                        }
                        break;
                }
            }
        }
    }

    /*
        Prints out the board in the console
     */
    public void debugPrint() {
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board.length; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println(errorMessage);
        errorMessage = "";
    }

    /*
        Moves a piece from coordinates pStart to pTarget
     */
    public void movePiece(int[] pStart, int[] pTarget) {
        Square start = board[pStart[0]][pStart[1]];
        Square target = board[pTarget[0]][pTarget[1]];
        Piece selectedPiece = start.getPiece();
        int[] vector = {Math.abs(pTarget[0] - pStart[0]), Math.abs(pTarget[1] - pStart[1])};
        errorMessage = "ERROR on moving " + selectedPiece.getColor() + "" + selectedPiece.getType() + " at " +
                pStart[0] + " " + pStart[1] + " to " + pTarget[0] + " " + pTarget[1] + ": ";

        if (selectedPiece != null) {
            //IF THE TURN COLOR AND SELECTED PIECE IS IDENTICAL
            if (target.getPiece() == null || target.getPiece().getColor() != turn) {
                if (selectedPiece.getColor() == turn) {
                    //MOVING PIECE IS A PAWN
                    if (selectedPiece.getType() == 'p') {
                        //JUST FORWARD
                        if (pStart[1] == pTarget[1]) {
                            //IF COLOR AND DIRECTION IS LEGAL
                            if ((selectedPiece.getColor() == 'w' && pTarget[0] - pStart[0] > 0) ||
                                    (selectedPiece.getColor() == 'b' && pTarget[0] - pStart[0] < 0)) {
                                //FIRST MOVE = 1 OR 2 STEPS FORWARD
                                //NOT FIRST MOVE = ONLY 1 STEP FORWARD
                                if ((selectedPiece.getFirstMove() && (Math.abs(pTarget[0] - pStart[0]) == 2) ||
                                        Math.abs(pTarget[0] - pStart[0]) == 1) ||
                                        (!selectedPiece.getFirstMove() && Math.abs(pTarget[0] - pStart[0]) == 1)) {
                                    //EXECUTE MOVE
                                    target.setPiece(selectedPiece);
                                    start.setPiece(null);
                                    errorMessage = "";
                                    turn = (turn == 'w') ? 'b' : 'w';
                                    selectedPiece.firstMoveDone();
                                } else {
                                    errorMessage += "NOT FIRST MOVE";
                                }
                            } else {
                                errorMessage += "PAWNS CANT MOVE BACKWARDS";
                            }
                            //TAKING A PIECE DIAGONALLY
                        } else {
                            //IF THE MOVE IS DIAGONAL, WITH A DISTANCE OF 1
                            if (Math.abs(pStart[1] - pTarget[1]) == 1 && Math.abs(pStart[0] - pTarget[0]) == 1) {
                                //IF PIECE EXISTS ON TARGET SQUARE
                                if (target.getPiece() != null) {
                                    //IF TARGET IS NOT THE SAME COLOR
                                    if (target.getPiece().getColor() != selectedPiece.getColor()) {
                                        //EXECUTE MOVE
                                        target.setPiece(selectedPiece);
                                        start.setPiece(null);
                                        errorMessage = "";
                                        turn = (turn == 'w') ? 'b' : 'w';
                                        selectedPiece.firstMoveDone();
                                    } else {
                                        errorMessage += "TARGET SAME COLOR AS PAWN";
                                    }
                                } else {
                                    errorMessage += "NOTHING TO TAKE";
                                }
                            } else {
                                errorMessage += "PAWNS CANT TAKE DIAGONALLY MORE THAN 1 SQUARE";
                            }
                        }
                    }
                    //MOVING PIECE IS A BISHOP, ROOK OR QUEEN
                    else if (selectedPiece.getType() == 'Q' ||
                            selectedPiece.getType() == 'B' ||
                            selectedPiece.getType() == 'R') {
                        //MOVING PIECE IS A BISHOP OR QUEEN
                        if (selectedPiece.getType() != 'R') {
                            if (vector[0] == vector[1]) {
                                if (raytrace(pStart, pTarget)) {
                                    //EXECUTE MOVE
                                    target.setPiece(selectedPiece);
                                    start.setPiece(null);
                                    errorMessage = "";
                                    turn = (turn == 'w') ? 'b' : 'w';
                                    selectedPiece.firstMoveDone();
                                } else {
                                    errorMessage += "PIECE IN THE WAY";
                                }
                            }
                        }
                        //MOVING PIECE IS A ROOK OR QUEEN
                        if (selectedPiece.getType() != 'B') {
                            if (vector[0] == 0 || vector[1] == 0) {
                                if (raytrace(pStart, pTarget)) {
                                    //EXECUTE MOVE
                                    target.setPiece(selectedPiece);
                                    start.setPiece(null);
                                    errorMessage = "";
                                    turn = (turn == 'w') ? 'b' : 'w';
                                    selectedPiece.firstMoveDone();
                                } else {
                                    errorMessage += "PIECE IN THE WAY";
                                }
                            }
                        }
                    }
                    //MOVING PIECE IS A KNIGHT
                    else if (selectedPiece.getType() == 'N') {
                        //MOBILITY OF A KNIGHT
                        if ((vector[0] == 2 && vector[1] == -1) ||
                                (vector[0] == 2 && vector[1] == 1) ||
                                (vector[0] == -2 && vector[1] == -1) ||
                                (vector[0] == -2 && vector[1] == 1) ||
                                (vector[0] == 1 && vector[1] == 2) ||
                                (vector[0] == 1 && vector[1] == -2) ||
                                (vector[0] == -1 && vector[1] == 2) ||
                                (vector[0] == -1 && vector[1] == -2)) {
                            //EXECUTE MOVE
                            target.setPiece(selectedPiece);
                            start.setPiece(null);
                            errorMessage = "";
                            turn = (turn == 'w') ? 'b' : 'w';
                            selectedPiece.firstMoveDone();
                        } else {
                            errorMessage += "KNIGHT CANT JUMP THAT WAY";
                        }
                        //MOVING PIECE IS A KING
                    } else if (selectedPiece.getType() == 'K') {
                        //IF MOVING DISTANCE IS JUST 1 SQUARE (DIAGONAL IS SQRT(2), THATS WHY I CHOSE < 2)
                        if (Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2)) < 2) {
                            if (target.getPiece() == null) {
                                //EXECUTE MOVE
                                target.setPiece(selectedPiece);
                                start.setPiece(null);
                                errorMessage = "";
                                turn = (turn == 'w') ? 'b' : 'w';
                                selectedPiece.firstMoveDone();
                            } else {
                                errorMessage += "PIECE IN THE WAY";
                            }
                        } else {
                            errorMessage += "KINGS CANT MOVE MORE THAN 1 SQUARE";
                        }
                    }
                } else {
                    errorMessage += "ITS " + turn + " TURN";
                }
            } else {
                errorMessage += "TARGET IS ALLIED PIECE";
            }
            debugPrint();
        }
    }

    /*
        Traces the squares between pStart to pTarget for
        pieces in the way.
     */
    public boolean raytrace(int[] pStart, int[] pTarget) {
        //Define Vector direction
        int[] vector = generateUnitVector(pStart, pTarget);
        //Scan for Pieces on the way
        while (!(pStart[0] == pTarget[0] && pStart[1] == pTarget[1])) {
            pStart[0] += vector[0];
            pStart[1] += vector[1];
            if (board[pStart[0]][pStart[1]].getPiece() != null) {
                if (!(pStart[0] == pTarget[0] && pStart[1] == pTarget[1])) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
        Generates a vector for a probe to move from a starting square
        to the target square, in order to scan for Pieces on the way
     */
    public int[] generateUnitVector(int[] pStart, int[] pTarget) {
        int[] vector = {pTarget[0] - pStart[0], pTarget[1] - pStart[1]};
        if (vector[0] == 0 && vector[1] != 0) {
            vector[1] = (vector[1] < 0) ? -1 : 1;
        } else if (vector[0] != 0 && vector[1] == 0) {
            vector[0] = (vector[0] < 0) ? -1 : 1;
        } else if (vector[0] != 0 && vector[1] != 0) {
            vector[0] = (vector[0] < 0) ? -1 : 1;
            vector[1] = (vector[1] < 0) ? -1 : 1;
        }
        return vector;
    }

    /*
        Translates chess-conventional terminology into array indices,
        then moves a piece from start to target.
     */
    public void movePieceTranslated(String start, String target) {
        char[] letters = {'a','b','c','d','e','f','g','h'};
        int[] startSquare = new int[2];
        int[] targetSquare = new int[2];
        startSquare[0] = Character.getNumericValue(start.charAt(1)) - 1;
        targetSquare[0] = Character.getNumericValue(target.charAt(1)) - 1;
        for (int i = 0; i < letters.length; i++) {
            if (start.charAt(0) == letters[i]) {
                startSquare[1] = i;
            }
            if (target.charAt(0) == letters[i]) {
                targetSquare[1] = i;
            }
        }
        movePiece(startSquare, targetSquare);
    }
}