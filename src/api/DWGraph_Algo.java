package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph gr;
    private HashMap<node_data, Double> nodeDis;
    private HashMap<node_data, node_data> nodePar;
    private Set<node_data> used;
    private PriorityQueue<node_data> unused;
    private List<node_data> path;
    private HashMap<Integer,HashMap<Integer,edge_data>> ReversedGr;

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

        Iterator<node_data> itradd = gr.getV().iterator();
        while (itradd.hasNext()) {
            g.addNode(new NodeData(itradd.next()));
        }

        Iterator<node_data> itr = gr.getV().iterator();
        while (itr.hasNext()) {
            node_data TmpNode = itr.next();
            Iterator<edge_data> itr1 = gr.getE(TmpNode.getKey()).iterator();
            while (itr1.hasNext()) {
                edge_data e = itr1.next();
                g.connect(e.getSrc(), e.getDest(), e.getWeight());
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
        if(flag==false)return flag;

      directed_weighted_graph tmp = ReversGr();
        Iterator<node_data> it4 = gr.getV().iterator();
        Q.add(it4.next());
        Q.peek().setTag(0);

        while (!Q.isEmpty()) {
            node_data t3 = Q.poll();
            Iterator<edge_data> it5 = tmp.getE(t3.getKey()).iterator();
            while (it5.hasNext()) {
                edge_data e = it5.next();
                if (tmp.getNode(e.getDest()).getTag() < 0) {
                    tmp.getNode(e.getDest()).setTag(0);
                    Q.add(tmp.getNode(e.getDest()));

                }

            }
        }
        Iterator<node_data>ite=tmp.getV().iterator();
        while (ite.hasNext()) {
            node_data t8 = ite.next();
            if (t8.getTag() == -1) {
                flag = false;
            }
            t8.setTag(-1);
        }
        return flag;
    }

    public directed_weighted_graph ReversGr(){
        directed_weighted_graph g = new DWGraph_DS();
        Iterator<node_data> itr = gr.getV().iterator();
        while(itr.hasNext()){
            g.addNode(itr.next());
        }
        Iterator<node_data> itr1 = gr.getV().iterator();
        while(itr1.hasNext()){
            node_data n = itr1.next();
            Iterator<edge_data> itr2 = gr.getE(n.getKey()).iterator();
            while(itr2.hasNext()){
                edge_data e = itr2.next();
                g.connect(e.getDest(),e.getSrc(),e.getWeight());
            }
        }
        return g;
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        if (gr.getNode(src) == null || gr.getNode(dest) == null) return -1;
        used = new HashSet<>();
        unused = new PriorityQueue<>();
        nodeDis = new HashMap<>();

        Dijkstra(src);
        double ShortestPath = gr.getNode(dest).getWeight();
        reset_nodes();
        return  ShortestPath;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (gr.getNode(src) == null || gr.getNode(dest) == null) return null;
        used = new HashSet<>();
        unused = new PriorityQueue<>();
        nodeDis = new HashMap<>();
        nodePar = new HashMap<>();
        path = new ArrayList<>();
        Dijkstra(src);
        if (gr.getNode(dest).getWeight() == -1) return path;
        node_data pointerNode = gr.getNode(dest);
        path.add(pointerNode);
        while (pointerNode != gr.getNode(src)) {
            pointerNode = nodePar.get(pointerNode);
            path.add(pointerNode);
        }
        Collections.reverse(path);
        reset_nodes();
        return path;
    }

    @Override
    public boolean save(String file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json =gson.toJson(gr);

        try{
            PrintWriter pw = new PrintWriter( new File(file));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
try
{
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(gr.getClass(),new GrapgJsonDeserializer());
}
        return false;
    }


    public void Dijkstra(int src) {
        gr.getNode(src).setWeight(0.0);
        nodeDis.put(gr.getNode(src), 0.0);
        unused.add(gr.getNode(src));

        while (!unused.isEmpty()) {

            node_data t = unused.poll();
            used.add(t);
            Iterator<edge_data> Nei = gr.getE(t.getKey()).iterator();
            while (Nei.hasNext()) {
                edge_data tempEd = Nei.next();
                if (!nodeDis.containsKey(gr.getNode(tempEd.getDest()))) {
                    nodeDis.put(gr.getNode(tempEd.getDest()), -1.0);
                }
                double tempNeiDis = nodeDis.get(gr.getNode(tempEd.getDest())); // dis from src to this dest in edge.
                double tDis = nodeDis.get(t); // parent dis.
                double EdgeDis = tempEd.getWeight();
                if ((tempNeiDis == -1.0 || tempNeiDis > (tDis + EdgeDis))&&(!used.contains(gr.getNode(tempEd.getDest())))) {//Test
                    nodeDis.put(gr.getNode(tempEd.getDest()), tDis + EdgeDis);
                    gr.getNode(tempEd.getDest()).setWeight(tDis + EdgeDis);//
                    unused.add(gr.getNode(tempEd.getDest()));
                    if (nodePar != null) {
                        nodePar.put(gr.getNode(tempEd.getDest()), t);

                    }
                }
            }
        }
    }

    private void reset_nodes() {
        Iterator<node_data> it = gr.getV().iterator();
        while (it.hasNext()) {
            node_data temp = it.next();
            temp.setWeight(-1);
        }
    }

}
