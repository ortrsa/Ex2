package gameClient;

import api.directed_weighted_graph;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 */
public class MyFrame extends JFrame {
    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private MyPanel panel;
    private int h;
    private int w;


    MyFrame(String a) {
        super(a);
        int _ind = 0;
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.panel = new MyPanel(_ar, _w2f);
        this.add(panel);
    }

    public void update(Arena ar) {
        w = getWidth();
        h = getHeight();
        this._ar = ar;
        updateFrame();
        panel.set_ar(ar);
    }

    private void updateFrame() {

        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 150, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
        panel.set_w2f(_w2f);
    }

    public void paint(Graphics g) {
        if (w!=getWidth()||h!=getHeight()) {
            updateFrame();
        }
        int w = this.getWidth();
        int h = this.getHeight();
        //g.clearRect(0, 0, w, h);
        panel.paint(g);
    }

}
