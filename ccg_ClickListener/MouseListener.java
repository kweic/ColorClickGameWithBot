package ccg_ClickListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;

/**
 *
 * @author kw
 */
public class MouseListener {

    JFrame panel;

    public MouseListener(JFrame panel) {
        this.panel = panel;
    }

    public void addCoordListener() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("click sensed ######");
                System.out.println(e.getPoint());
            }
        });
    }
}
