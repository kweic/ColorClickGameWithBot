package ccg_Logic;

import ccg_Display.BoardGenerator;
import ccg_Display.DisplayTimer;
import java.util.ArrayList;


public class BoardMovement {

    int[][] board;
    int height;
    int width;
    BoardGenerator boardGenerator;
    DisplayTimer displayTimer;
    boolean colsUpdated;
    boolean rowsUpdated;

    public BoardMovement(BoardGenerator boardGenerator, int height, int width) {
        this.boardGenerator = boardGenerator;
        //To Do: adjust methods to not need -1 here to function
        this.height = height - 1;
        this.width = width - 1;
        colsUpdated = true;
        rowsUpdated = true;

    }
    
    public void attachTimer(DisplayTimer displayTimer){
        this.displayTimer = displayTimer;
    }
    
    public void setBoard(int[][] board){
        this.board = board;
    }
    
    public int[][] getBoard(){
        return board;
    }

    public boolean checkClear() {
        //return true, clear if board final spot is empty
        return board[0][height] == 0;
    }

    public boolean colsUpdated() {
        return colsUpdated;
    }
    
    public boolean rowsUpdated(){
        return rowsUpdated;
    }
    
    public boolean boardUpdated(){
        return colsUpdated && rowsUpdated;
    }

    public void updateCols() {
        if (!colsUpdated) {
            dropColumns();
            colsUpdated = true;
            displayTimer.setUpdateFalse();
        }
    }

    public void updateRows() {

        if (colsUpdated && !rowsUpdated && emptyColumn()) {
            slideRows2();
            rowsUpdated = true;
            displayTimer.setUpdateFalse();
        } else if (colsUpdated && !rowsUpdated) {
            rowsUpdated = true;
        }
    }

    public void updateBoardQuick() {
        dropColumns();
        if (emptyColumn()) {
            slideRows2();
        }
    }

    public void updateBoard() {
        //System.out.println("board update called in BoardMovement");
        //make new thread here to display board while running this
        colsUpdated = false;
        rowsUpdated = false;
    }

    public boolean blankSpot(int y, int x) {
        return board[x][y] == 0;
    }

    public boolean emptyColumn() { //used by updateBoard
        //go through each column, if a whole column is empty return true
        int count;
        for (int x = 0; x <= width; x++) {
            count = 0;
            for (int y = 0; y <= height; y++) {
                if (board[x][y] == 0) {
                    count++;
                }
                if (count == height + 1 && solidNextTo(y, x)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void dropColumns() { //movement
        //add thread wait in this part
        for (int x = 0; x <= width; x++) {
            for (int y = height; y >= 0; y--) {
                if (board[x][y] == 0 && solidAbove(y, x)) {

                    dropColumn(y, x);
                    x = 0; //step back again to check the column
                    y = height + 1;
                }
            }
        }
    }

    public void slideRows2() { //used by updateboard, movement
        //find the gap, find how big it is
        //add thread wait here
        int gapWid = 0;
        int leftIndex = 0;
        int startIndex;
        ArrayList<Integer> startIndexes = new ArrayList();
        for (int i = 0; i <= width; i++) {
            if (gapWid > 0 && board[i][height] != 0) {
                leftIndex = i;
                i = width + 1; //end the check
            } else if (board[i][height] == 0) {
                gapWid++;
            }
            if (gapWid == 1) {
                startIndexes.add(i);
            }
        }
        startIndex = startIndexes.get(0);

        copyLeftOfGapOver(leftIndex, startIndex);
        fillRightGap(gapWid);
        checkForRemainingRowGap();
    }

    private void copyLeftOfGapOver(int leftIndex, int startIndex) {
        //save board left of the gap
        for (int x = leftIndex; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                //System.out.println("x: "+x+" + S: "+startIndex+" - L: "+leftIndex);
                board[x + startIndex - leftIndex][y] = board[x][y];
            }
        }
    }

    private void checkForRemainingRowGap() {
        //check again if any other gap remains
        //if no other gap remains, return
        //else recurse
        int finalCheck = 0;
        for (int x = 0; x <= width; x++) {
            if (finalCheck > 0 && board[x][height] != 0) {
                slideRows2();
                break;
            }
            if (board[x][height] == 0) {
                finalCheck++;
            }
        }
    }

    private void fillRightGap(int gapWid) {
        //place all at x-gapsize
        //fill gapsize on right
        for (int x = width - gapWid + 1; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                board[x][y] = 0;
            }
        }
    }

    public boolean solidNextTo(int y, int x) { //movement, used by empty column
        //System.out.println("doint solidNextTo");
        for (int i = x; i <= width; i++) {
            if (board[i][y] != 0) {
                //System.out.println("return true");
                return true;
            }
        }
        //System.out.println("return false");
        return false;
    }

    public boolean solidAbove(int y, int x) { //used by movement
        //go through and check if the blank spot has anything solid above it
        for (int i = y; i >= 0; i--) {
            if (board[x][i] != 0) {
                return true;
            }
        }
        return false;
    }

    public void dropColumn(int y, int x) { //used by movement
        //drop single column by one space

        for (int i = y; i > 0; i--) {
            board[x][i] = board[x][i - 1];
        }
        board[x][0] = 0;
    }

}
