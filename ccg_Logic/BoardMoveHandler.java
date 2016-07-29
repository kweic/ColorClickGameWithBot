package ccg_Logic;

import ccg_Display.BoardGenerator;

/**
 *
 * @author kw
 */
public class BoardMoveHandler {
    //using this class
    //first use matchcount to see if there is a matching adjacent color
    //next use simulate click on that spot

    int[][] board;
    int height;
    int width;
    BoardGenerator boardGenerator;

    public BoardMoveHandler(BoardGenerator boardGenerator, int height, int width) {
        //this.board = board;
        this.boardGenerator = boardGenerator;
        //this.board = board;
        this.height = height - 1;
        this.width = width - 1;
        System.out.println("###############\n"
                + "#################\n"
                + "############\n"
                + "moveHandler created");
    }

    public void click(int x, int y) {
        //this.board = board;
        if (matchCount(x,y) > 0) {
            System.out.println("click passed");
            System.out.println("clicking in move handler " + x + " " + y);
            simulateClick(board[x][y], x, y);
            //printPreviewBoard();

            boardGenerator.updateBoard();
            //boardGenerator.clickRefresh();
            //System.out.println("click in BoardMoveHandler finished");
        } else {
            System.out.println("click failed @ " + x + " " + y+" match count: "+matchCount(x,y)+" board xy: "+board[x][y]);
            printPreviewBoard();
        }
    }

    public void printPreviewBoard() {
        System.out.println("preview from MovementHandler");
        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                System.out.print(board[x][y] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    public int[][] getBoard(){
        return board;
    }
    
    public int getColor(int x, int y){
        return board[x][y];
    }

    public void simulateClick(int color, int x, int y) {
        //System.out.println("doing sim: color: "+color);
        //int color = board[x][y];
        //System.out.println("recursed");
        if (color == 0) {
            return;
        }
        board[x][y] = 0; //clicked spot becomes blank

        //check for matches next to click and recurse
        if (y != 0 && sideMatch(color, x, y - 1)) { //above
            //System.out.println("recurse above");
            simulateClick(color, x, y - 1);
        }

        if (x != 0 && sideMatch(color, x - 1, y)) { //left
            //System.out.println("recurse left");
            simulateClick(color, x - 1, y);
        }

        if (y != height && sideMatch(color, x, y + 1)) { //below
            //System.out.println("recurse below");
            simulateClick(color, x, y + 1);
        }

        if (x != width && sideMatch(color, x + 1, y)) { //right
            //System.out.println("recurse right");
            simulateClick(color, x + 1, y);
        }

    }

    public int matchCount(int pX, int pY) { //removed char color
        //check initial click to see if it's valid
        //int color = board[pY][pX];
        if (board[pX][pY] == 0) { //changed line here, check back for errors
            return 0;
        }

        int count = 0;
        if (pY != 0 && sideMatch(board[pX][pY], pX, pY - 1)) { //top
            count++;
        }

        if (pX != 0 && sideMatch(board[pX][pY], pX - 1, pY)) { //left
            count++;
        }

        if (pY != height && sideMatch(board[pX][pY], pX, pY + 1)) { //bottom
            count++;
        }

        if (pX != width && sideMatch(board[pX][pY], pX + 1, pY)) { //right
            count++;
        }

        return count;
    }

    public boolean sideMatch(int color, int pX, int pY) {
        //System.out.println("side match: "+ (color == board[pY][pX])+" color: "+color);
        //System.out.println("sidematch called: "+pX+" "+pY);
        //System.out.println("color: "+color+" board: "+board[pX][pY]);
        return color == board[pX][pY];
    }

}
