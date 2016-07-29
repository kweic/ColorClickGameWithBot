package ccg_ClickListener;

import ccg_Logic.BoardMoveHandler;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author kw
 */
public class ClickListener {

    int x;
    int y;
    int[][] board;
    BoardMoveHandler moveHandler;

    public ClickListener(BoardMoveHandler moveHandler, int[][] board, int x, int y) {
        this.moveHandler = moveHandler;

        this.x = x;
        this.y = y;
        this.board = board;
    }

    public void addListener(JPanel colorBox) {

            colorBox.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {

                    //System.out.println("Clicked: " + x + " " + y);
                    moveHandler.click(x, y);
                    //moveHandler.printPreviewBoard();
                    //System.out.println("click finished");
                }

            });
       

    }

}
