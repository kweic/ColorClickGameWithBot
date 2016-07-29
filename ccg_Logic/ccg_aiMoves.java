package ccg_Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author kw
 */
public class ccg_aiMoves {

    int[][] board;
    int[][] simBoard;
    int[][] boardHold;
    ArrayList<int[]> finalMoves;
    ArrayList<int[]> recordedBest = new ArrayList();
    boolean winFound;
    //boolean usingHighScore = false;
    int height;
    int width;
    Random random;
    BoardMoveHandler moveHandler;
    BoardMovement boardMovement;
    int failLimit;

    public ccg_aiMoves(int[][] board, int height, int width, BoardMovement boardMovement, BoardMoveHandler moveHandler) {
        this.board = board; //usages: 
        this.height = height;
        this.width = width;
        this.boardMovement = boardMovement;
        this.moveHandler = moveHandler;

        simBoard = new int[width][height];
        boardHold = new int[width][height];
        winFound = false;
        random = new Random();
        finalMoves = new ArrayList();
        recordedBest = new ArrayList();
        failLimit = 600;
    }

    public void doAIMoves() {
        simBoard = copyBoardArray(simBoard, board); //be the board to simulate on
        boardHold = copyBoardArray(boardHold, board);
        moveHandler.setBoard(simBoard); //pass simulation board into moveHandler
        boardMovement.setBoard(simBoard);

        
        recordedBest.clear();
        simulationLoop();
        
        System.out.println("OUT OF AI LOOP");

        board = copyBoardArray(board, boardHold);
        moveHandler.setBoard(board);
        boardMovement.setBoard(board);

        if (winFound) {
            System.out.println("move called");
            answerMove(finalMoves);
        } else {
            answerMove(recordedBest);
        }
    }
    
    public void simulationLoop(){
        int[] moves;
        int xMove;
        int yMove;
        int highScore = 0;
        int fail = 0;
        
        while (!winFound) {
            //To Do: add randomness to the order in which to find "best moves" so it's not always scanning from the top
            moves = getRandomBestMove(); //2054
            //moves = getRandomNonDuplicateMove();

            xMove = moves[0];
            yMove = moves[1];

            if (yMove == -1 && countBlanks() > highScore) { //count blanks when reach end and save move if new high score
                highScore = countBlanks();
                System.out.println("best: " + highScore);
                recordedBest = copyIntArray(finalMoves);
            }

            if (fail > failLimit) {
                return;
            }

            if (yMove == -1) {
                fail++;
                System.out.println("fail: " + fail);
                finalMoves.clear();
                //reached the end without beating it
                //reset the board and simboard
                board = copyBoardArray(board, boardHold);
                simBoard = copyBoardArray(simBoard, board);
            } else { //6-23
                finalMoves.add(moves);
                moveHandler.setBoard(board);
                boardMovement.setBoard(board);
                //System.out.println("   doing click at: " + xMove + " " + yMove);
                moveHandler.simulateClick(board[xMove][yMove], xMove, yMove); //done, don't tweak
                boardMovement.updateBoardQuick();
            }

            if (boardMovement.checkClear()) {
                winFound = true;
                return;
            }
        }
    }

    public void setMoveHandler(BoardMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    public void setUpdatedBoard(int[][] board) {
        this.board = board;
    }

    public int[][] copyBoardArray(int[][] toBoard, int[][] fromBoard) { //used by Get Best(simulate each move) and Get non duplicate (mark out moves)
        for (int x = 0; x < fromBoard.length; x++) {
            toBoard[x] = Arrays.copyOf(fromBoard[x], fromBoard[x].length);
        }

        /*
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                toBoard[x][y] = fromBoard[x][y];
            }
        }
         */
        return toBoard;
    }

    public static ArrayList<int[]> copyIntArray(ArrayList<int[]> moves) { //used from Main to save best moves
        ArrayList<int[]> copyMoves = new ArrayList();
        for (int[] move : moves) {
            copyMoves.add(move);
        }

        return copyMoves;
    }

    public int countBlanks() { //used by main to check for best game
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (simBoard[x][y] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public void answerMove(ArrayList<int[]> movesLoaded) { //prints the first move from the list of best moves found
        System.out.println("move Answered ####################");
        //System.out.println(movesLoaded.get(0)[0] + " " + movesLoaded.get(0)[1]); //this y, x right now
        moveHandler.click(movesLoaded.get(0)[0], movesLoaded.get(0)[1]);
        movesLoaded.remove(0);
    }

    //used by AI
    /*
    public int[] getRandomNonDuplicateMove() { //not used right now, random from non duplicates
        ArrayList<int[]> uniqueMoves = getNonDuplicateMoves();
        return uniqueMoves.get(random.nextInt(uniqueMoves.size()));
    }
     */
    //used by AI
    public ArrayList<int[]> getNonDuplicateMoves() { //called by get best move
        ArrayList<int[]> validUniqueMoves = new ArrayList();
        int[] moves;
        int[][] tempBoard = new int[width][height];
        tempBoard = copyBoardArray(tempBoard, board);
        moveHandler.setBoard(tempBoard);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //color = simBoard[x][y];
                if (tempBoard[x][y] != 0 && moveHandler.matchCount(x, y) > 0) {
                    moves = new int[2];
                    moves[0] = x;
                    moves[1] = y;
                    validUniqueMoves.add(moves);
                    //System.out.println("   added move: " + x + " " + y);
                    moveHandler.simulateClick(simBoard[x][y], x, y); //check X Y swap here

                }
            }
        }
        moveHandler.setBoard(simBoard);

        /*
        //not needed here when being called by getBestMoves()
        if (validUniqueMoves.isEmpty()) {
            moves[0] = -1;
            moves[1] = -1;
            validUniqueMoves.add(moves);
        }
         */
        return validUniqueMoves;
    }

    //used by AI
    public int[] getRandomBestMove() { //called by main
        ArrayList<int[]> bestMoves = getBestMoves();
        int[] move = new int[2];
        if (bestMoves.isEmpty()) {
            move[0] = -1;
            move[1] = -1;
            return move;
        }
        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

    /*
    //for debugging
    public void previewMoves(ArrayList<int[]> validMoves) {
        System.out.println("got valid Moves: " + validMoves.size());
        //preview valid
        for (int[] moves : validMoves) {
            System.out.println(moves[0] + " " + moves[1]);
        }
    }
     */
    public void resetBoard(int[][] boardToReset) {
        simBoard = copyBoardArray(boardToReset, board);
        moveHandler.setBoard(boardToReset);
        boardMovement.setBoard(boardToReset);
    }

    //used by AI
    public ArrayList<int[]> getBestMoves() { //called by getRandomBestMove

        ArrayList<int[]> validMoves = getNonDuplicateMoves();

        simBoard = copyBoardArray(simBoard, board);
        ArrayList<int[]> bestMovesList = new ArrayList();
        int[] bestMove = new int[2];

        if (validMoves.isEmpty()) {
            bestMove[0] = -1;
            bestMove[1] = -1;
            bestMovesList.add(bestMove);
            return bestMovesList;
        }

        int lowestImpact = Integer.MAX_VALUE;
        int currentImpact;
        //System.out.println("best move, Impact started");

        for (int[] move : validMoves) {
            if (move[0] != -1) {
                bestMove = new int[2];
                resetBoard(simBoard);
                moveHandler.simulateClick(simBoard[move[0]][move[1]], move[0], move[1]); //check X,Y swapped here, //changed board to boardcopy
                boardMovement.updateBoardQuick();

                currentImpact = countSingleColors();

                if (currentImpact <= lowestImpact) {
                    lowestImpact = currentImpact;
                    bestMove[0] = move[0]; //6-19 swapped these
                    bestMove[1] = move[1];
                    bestMovesList.add(bestMove);

                }

            }
        }
        return bestMovesList;
    }

    public int countSingleColors() { //used by getBestMoves to check impact of each test
        int colorCount = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (moveHandler.getColor(x, y) != 0 && moveHandler.matchCount(x, y) == 0) {
                    colorCount++;
                }
            }
        }
        return colorCount;
    }

    public void printBoard(int[][] boardToPrint, String message) { //only used for programming, testing, debugging
        System.out.println("\n" + message);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(boardToPrint[x][y]);
            }
            System.out.println();
        }
    }

}
