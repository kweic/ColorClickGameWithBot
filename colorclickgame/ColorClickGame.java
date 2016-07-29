/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colorclickgame;

import ccg_Display.BoardGenerator;
import ccg_Display.BoardPrint;
import ccg_Display.DisplayTimer;
import ccg_Logic.BoardMoveHandler;
import ccg_Logic.BoardMovement;
import ccg_Logic.ccg_aiMoves;
import javax.swing.JPanel;

/**
 *
 * @author kw
 */
public class ColorClickGame {

    public static void main(String[] args) {
        //                                        colors, height, wid, padding, box size

        int colorAmount = 3;
        int boxSize = 20;
        int width = 20;
        int height = 30;

        int gapSize = 1;

        BoardGenerator generator = new BoardGenerator(colorAmount, height, width, gapSize, boxSize);

        BoardMoveHandler moveHandler = new BoardMoveHandler(generator, height, width);
        BoardMovement boardUpdater = new BoardMovement(generator, height, width);
        JPanel board = generator.generateInitialBoard(moveHandler, boardUpdater);
        BoardPrint boardPrinter = new BoardPrint(board);
        generator.setPrinter(boardPrinter);
        boardUpdater.setBoard(generator.getBoard());

        ccg_aiMoves aiMoves = new ccg_aiMoves(generator.getBoard(), height, width, boardUpdater, moveHandler); //create the things in main and then send them out to each class instead of creating in the class
        //aiMoves.setUpdatedBoard(generator.getBoard());
        DisplayTimer displayTimer = new DisplayTimer(generator, boardUpdater, 150, aiMoves);
        boardUpdater.attachTimer(displayTimer);
        displayTimer.runUpdates();

        //DisplayTimer displayTimer = new DisplayTimer(generator, 1000);
        //generator.attachTimer(displayTimer);
        //displayTimer.runUpdates();
        boardPrinter.run();

    }

}
