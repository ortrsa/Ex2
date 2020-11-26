package api;

import java.util.Iterator;
import java.util.List;

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
        return false;
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
