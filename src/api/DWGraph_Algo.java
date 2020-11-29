package api;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph gr;
    private HashMap<Integer, Double> nodeDis;
    private HashMap<Integer, node_data> nodePar;
    private Set<Integer> used;
    private PriorityQueue<node_data> unused;
    private List<node_data> path;

    @Override
    public void init(directed_weighted_graph g) {
        this.gr = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return gr;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph g = new DWGraph_DS();
        Iterator<node_data> itr = gr.getV().iterator();
        while (itr.hasNext()) {
            node_data TmpNode = new NodeData();
            TmpNode = itr.next();
            g.addNode(TmpNode);
            Iterator<edge_data> itr1 = gr.getE(TmpNode.getKey()).iterator();
            while (itr1.hasNext()) {
                edge_data e = itr1.next();
                g.connect(TmpNode.getKey(), e.getSrc(), e.getWeight());
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
        if (gr.getNode(src) == null || gr.getNode(dest) == null) return -1;
        used = new HashSet<>();
        unused = new PriorityQueue<>();
        nodeDis = new HashMap<>();

        Dijkstra(src);

        return gr.getNode(dest).getTag();
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


    public void Dijkstra(int src) {
        gr.getNode(src).setWeight(0.0);
        nodeDis.put(src, 0.0);
        unused.add(gr.getNode(src));

        while (!unused.isEmpty()) {

            node_data t = unused.poll();
            used.add(t.getKey());
            Iterator<edge_data> Nei = gr.getE(t.getKey()).iterator();
            while (Nei.hasNext()) {
                edge_data tempEd = Nei.next();
                if (!nodeDis.containsKey(tempEd.getDest())) {//check if needed
                    nodeDis.put(tempEd.getDest(), -1.0);
                }
                double tempNeiDis = nodeDis.get(tempEd.getDest()); // dis from src to this dest in edge.
                double tDis = nodeDis.get(t.getKey()); // parent dis.
                double EdgeDis = tempEd.getWeight();
                if ((tempNeiDis == -1.0 || tempNeiDis > (tDis + EdgeDis))) {
                    nodeDis.put(tempEd.getDest(), tDis + EdgeDis);
                    gr.getNode(tempEd.getDest()).setWeight(tDis + EdgeDis);//
                    unused.add(gr.getNode(tempEd.getDest()));
                    if (nodePar != null) {
                        nodePar.put(tempEd.getDest(), t);

                    }

                }

            }


        }


    }


}
