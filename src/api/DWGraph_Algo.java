package api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DWGraph_Algo implements dw_graph_algorithms {
    private  directed_weighted_graph gr;

    @Override
    public void init(directed_weighted_graph g) {
    this.gr=g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return gr;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph g = new DWGraph_DS();
        Iterator<node_data> itr = gr.getV().iterator();
        while(itr.hasNext()){
            node_data TmpNode = new NodeData();
            TmpNode=itr.next();
            g.addNode(TmpNode);
            Iterator<edge_data>itr1=gr.getE(TmpNode.getKey()).iterator();
            while(itr1.hasNext()){
                edge_data e=itr1.next();
                g.connect(TmpNode.getKey(),e.getSrc(),e.getWeight());
            }
        }
        return g;
    }

    @Override
    public boolean isConnected() {

    Iterator<node_data> it = gr.getV().iterator();
        if (!it.hasNext()) return true;
    boolean flag = true;


    Queue<node_data> Q = new LinkedList<>();
    Iterator<node_data> it1 = gr.getV().iterator();
        Q.add(it1.next());
        Q.peek().setTag(0);

        while (!Q.isEmpty()) {
        node_data t3 = Q.poll();
        Iterator<edge_data> it2 = gr.getE(t3.getKey()).iterator();
        while (it2.hasNext()) {
            edge_data e = it2.next();
            if (gr.getNode(e.getDest()).getTag() < 0) {
                gr.getNode(e.getDest()).setTag(0);
                Q.add(gr.getNode(e.getDest()));

            }

        }
    }


        while (it.hasNext()) {
        node_data t8 = it.next();
        if (t8.getTag() == -1) {
            flag = false;
        }
        t8.setTag(-1);
    }

        return flag;
}
    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
