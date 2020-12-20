package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;

public class MyPanel extends JPanel {
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private static long TimeToEnd;
    private static double Curr_grad;

    MyPanel(Arena _ar, gameClient.util.Range2Range _w2f) {
        super();
        this._ar = _ar;
        this._w2f = _w2f;

    }

    public static void SetTimeToEnd(long timeToEnd) {
        TimeToEnd = timeToEnd;
    }

    public static void setCurrGrade(double curr_grad) {
       Curr_grad = curr_grad;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.drawPokemons(g);
        this.drawGraph(g);
        this.drawAgants(g);
        this.drawInfo(g);

    }


    void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    void drawPokemons(Graphics g) {
        java.util.List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    g.setColor(Color.black);
                    g.drawString("" + f.getValue(), (int) fp.x(), (int) fp.y() - 4 * r);


                }
            }
        }

    }

    void drawInfo(Graphics g) {


        g.setFont(new Font("name" , Font.PLAIN,(int)((getWidth()+getHeight())/80)));
        g.setColor(Color.black);
        g.drawString("Time To end: "+ TimeToEnd/1000 , (int)(10+getWidth()*0.03),(int)(50+(getHeight()*0.05)));
        g.drawString("current grade: "+ Curr_grad , (int)((getWidth()*0.80)-60),(int)(50+(getHeight()*0.05)));

    }

    void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {

                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            }
        }
    }

    void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());

    }


    public void set_ar(Arena _ar) {
        this._ar = _ar;
    }

    public void set_w2f(Range2Range w2f) {
        this._w2f = w2f;
    }
}


