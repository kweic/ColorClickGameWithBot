/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccg_Display;

import ccg_ClickListener.MouseListener;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author kw
 */
public class BoardPrint implements Runnable {

    private JFrame frame;
    private JPanel panel;

    public BoardPrint(JPanel panel) {
        this.panel = panel;

    }

    @Override
    public void run() {
        System.out.println("started running UI");
        frame = new JFrame();

        //below not working yet
        MouseListener listener = new MouseListener(frame);
        listener.addCoordListener();
        //above not working yet

        frame.setPreferredSize(new Dimension(panel.getWidth() + 15, panel.getHeight() + 35)); //15 and 35 get rid of overlap from frame edges
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //fullscreen
        frame.setLocation(200, 100);
        //frame.setUndecorated(true); //no icons
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);

        System.out.println("frame size: " + frame.getWidth() + " " + frame.getHeight());
        System.out.println("finished running UI");

    }

    public void refreshPlayerPanel() {
        //System.out.println("refresh in BoardPrint called");
        frame.repaint();
    }

    private void createComponents(Container container) {
        //JPanel panel = new JPanel();
        //panel.setBounds(0, 0, width, height);
        panel.setBackground(Color.lightGray);

        container.add(panel);
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public JFrame getFrame() {
        return frame;
    }
}
