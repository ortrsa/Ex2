package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame{
	private int _ind;
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	MyPanel panel;
	MyFrame(String a) {
		super(a);
		int _ind = 0;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panel= new MyPanel(_ar , _w2f);
		this.add(panel);
	}
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
		panel.set_ar(ar);
	}

	private void updateFrame() {

		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
		panel.set_w2f(_w2f);
	}
	public void paint(Graphics g) {

		int w = this.getWidth();
		int h = this.getHeight();
		g.clearRect(0, 0, w, h);
		panel.paint(g);

	}

}
