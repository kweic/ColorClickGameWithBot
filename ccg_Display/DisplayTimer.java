package ccg_Display;

import ccg_Logic.BoardMovement;
import ccg_Logic.ccg_aiMoves;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author kw
 */
public class DisplayTimer {

    boolean printedUpdate;
    BoardMovement boardMovement;
    BoardGenerator boardGen;
    boolean aiOn;
    boolean runLoop = true;
    int aiCount;
    int refreshDelay;
    int updateCount;
    ccg_aiMoves aiMoves;

    public DisplayTimer(BoardGenerator boardGen, BoardMovement boardMove, int delay, ccg_aiMoves aiMoves) {
        printedUpdate = false;
        this.refreshDelay = delay;
        this.boardMovement = boardMove;
        this.boardGen = boardGen;
        this.aiMoves = aiMoves;
        updateCount = 0;
        aiCount = 0;
        aiOn = true;
    }

    public void setUpdateFalse() {
        printedUpdate = false;
    }

    public boolean getUpdated() {
        System.out.println("timer updated returning: " + printedUpdate);
        return printedUpdate;
    }

    public void update() {

    }

    public int AIcall(int aiCount, boolean aiOn) {
        if (aiOn) {
            if (aiCount >= 2 && boardMovement.boardUpdated()) { //1 second delay
                System.out.println("do ai move");
                aiMoves.doAIMoves();
                aiCount = 0;
            } else {
                aiCount++;
            }
        }
        return aiCount;
    }

    public void runUpdates() {
        Timer timer = new Timer(refreshDelay, new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent arg0) {

                // Code to be executed
                //if cols !updated and update count is 0, do an update on the click by itself, increment update count
                //if !cols updated and update count is >0 update cols and set update to true
                //else if cols updated rows !updated update rows and set rows to true
                //mouse position relative to screen
                //System.out.println(MouseInfo.getPointerInfo().getLocation().x + " "+ MouseInfo.getPointerInfo().getLocation().y);
                //System.out.println("timer running, count: "+updateCount);
                if (runLoop) {

                    /*
                    if (aiOn) {
                        if (aiCount == 10) {
                            System.out.println("do ai move");
                            aiMoves.doAIMoves();
                            aiCount = 0;
                        } else {
                            aiCount++;
                        }
                    }
                     */
                    aiCount = AIcall(aiCount, aiOn);

                    if (!boardMovement.colsUpdated() && updateCount == 0) {
                        updateCount++;
                        boardGen.clickRefresh(boardMovement.getBoard());

                    } else if (updateCount == 1) {
                        boardMovement.updateCols();

                        boardGen.clickRefresh(boardMovement.getBoard());
                        updateCount++;
                    } else if (updateCount == 2) {
                        boardMovement.updateRows();
                        boardGen.clickRefresh(boardMovement.getBoard());
                        updateCount++;
                    } else if (updateCount > 2) {
                        updateCount = 0;
                    }
                    
                    
                    //To Do: get this working
                    //if (boardMovement.checkClear()) {
                    //    runLoop = false;
                        //aiOn = false;
                    //}

                    //if(!printedUpdate){
                    //   System.out.println("doing timer");
                    //   boardGen.clickRefresh();
                    //   printedUpdate = true;
                    //}
                }
            }
        });

        timer.setRepeats(true);
        timer.start();
    }

}
