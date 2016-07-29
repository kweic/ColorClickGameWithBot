package ccg_Display;

import ccg_ClickListener.ClickListener;
import ccg_Logic.BoardMoveHandler;
import ccg_Logic.BoardMovement;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;

/**
 *
 * @author kw
 */
public class BoardGenerator {

    int colorAmount;
    int height;
    int width;
    int padding;
    Random random;
    ArrayList<Color> colors;
    int[][] board;
    int fixedBoxSize;
    BoardMoveHandler moveHandler;
    BoardPrint boardPrinter;
    BoardMovement boardUpdater;
    DisplayTimer timer;
    JPanel mainBoard;
    //to do
    //add pre drawn randoms that I can check have adequate amount of each to be passable

    public BoardGenerator(int colorAmount, int height, int width, int padding, int boxSize) {
        this.padding = padding;
        this.colorAmount = colorAmount;
        this.height = height;
        this.width = width;

        fixedBoxSize = boxSize;
        random = new Random();

        colors = new ArrayList(Arrays.asList(Color.lightGray, Color.yellow, Color.blue, Color.red,
                Color.green, Color.magenta, Color.cyan, Color.pink, Color.gray, Color.orange, Color.black, Color.white));
    }

    public int[][] getBoard() {
        return board;
    }

    public void setPrinter(BoardPrint boardPrinter) {
        this.boardPrinter = boardPrinter;
    }

    public JPanel generateInitialBoard(BoardMoveHandler moveHandler, BoardMovement boardUpdater) {
        mainBoard = new JPanel();

        mainBoard.setBounds(0, 0, width * (fixedBoxSize + padding), height * (fixedBoxSize + padding));
        mainBoard.setLayout(null);
        board = makeBoard(generatedColors());

        //moveHandler = new BoardMoveHandler(board, this, height, width);
        //boardUpdater = new BoardMovement(this, board, height, width);
        this.moveHandler = moveHandler;
        moveHandler.setBoard(board);
        this.boardUpdater = boardUpdater;
        System.out.println("preview from initial");
        previewFromGen();
        return generateBoard(board);
    }

    public void previewFromGen() { //this is correct board x,y orient
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(board[x][y] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public JPanel generateBoard(int[][] board) {
        mainBoard.removeAll();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //add each color box
                //colorArray[x][y] = Integer.parseInt(reader.next());
                JPanel nextBox = getColorBox(board[x][y], x, y);
                nextBox.setBounds(x * (fixedBoxSize + padding), y * (fixedBoxSize + padding), fixedBoxSize, fixedBoxSize);
                //nextBox.setLocation(x*fixedBoxSize,y*fixedBoxSize);
                mainBoard.add(nextBox);
            }
        }

        return mainBoard;
    }

    public void attachTimer(DisplayTimer timer) {
        this.timer = timer;
    }

    public void updateBoard() {
        boardUpdater.updateBoard();
    }

    public void clickRefresh(int[][] board) {
        //System.out.println("click refresh called");

        generateBoard(board);

        boardPrinter.refreshPlayerPanel();
        //System.out.println("click refresh FINISHED");
    }

    public int[][] makeBoard(String genColors) {
        int[][] colorArray = new int[width][height];
        Scanner reader = new Scanner(genColors);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //add each color box
                colorArray[x][y] = Integer.parseInt(reader.next());
            }
        }
        return colorArray;
    }

    public String generatedColors() {
        //make this return a string representing all the colors to be on the board
        StringBuilder genColors = new StringBuilder();
        for (int i = 0; i < (height * width); i++) {

            genColors.append(random.nextInt(colorAmount) + 1); //1+ to skip the "blank" 0
            genColors.append(" ");
        }
        return genColors.toString();
    }

    public JPanel getColorBox(int color, int x, int y) {
        JPanel colorBox = new JPanel();
        if (board[x][y] != 0) { //only add listeners to color boxes
            ClickListener listener = new ClickListener(moveHandler, board, x, y);
            listener.addListener(colorBox);
        }
        colorBox.setBackground(colors.get(color));

        return colorBox;
    }
}
