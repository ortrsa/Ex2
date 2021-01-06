package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * This class implements dw_graph_algorithms
 */
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

    /**
     * Deep coping of a graph by creating a new empty graph Then going on the original graph
     * HashMap of nodes by Iterator and adding to the copied graph all the nodes.
     * Then we need to copy the edges so were going on the Graph nodes and then on every node
     * edges then this node is the src of them and we connect the nodes as well at the copied graph.
     * @return
     */
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

    /**
     * This function returns true if  we can get from every node to every other node.
     * Because we are talking about a Directed graph we used a known algorithm that first take randomly node and see
     * if we can get from this node to every other node by going from this node to his neighbors changing their tag to 0
     * then to their neighbors and so on if there isnt a  node with tag -1 at the graph we can get from this node to every other node.
     * now we need to check that all the nodes can get to this node so we call a function we created named ReversGr this function
     * works similar to copy function but it swap every time it connect the src with the dest and then we have reverse graph exactly
     * then we check at the reverse graph if the node we check at the regular graph can get to all the other nodes if so plus the fact
     * that in the regular graph he can get to all the graph is connected!
     *
     * @return
     */
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

    /**
     * returns a reversed graph works like copy but change the src and dest every time we use connect.
     * @return
     */
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

    /**
     * Returns the shortest path distance between nNode src to Node dest by applying Dijkstra algorithm that change the nodes
     * weights to the shortest path dist from src node. then the shortest path dist from src to dest is the dest Node Weight.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
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

    /**
     * return list that contains the shortest path (by weight) from src to dest.
     * reset all nodes tag,
     * apply 'Dijkstra' algorithm with 'src', if the 'dest' doesn't connect to 'src' return -1.
     * the 'Dijkstra' fill the nodePer HashMap with nodes and it's parents.
     * create pointer that point to the dest node and add it to the 'path' list,
     * while the pointer not equal to 'src' node, pointer = 'nodePar'.
     * reverse path list and return path.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
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
    /**
     * Saves this graph to the given file name,
     * this method use the serializable interface to serialize the graph
     * and save it to file.
     * if the file save successfully return true.
     *
     * @param file - the file name (may include a relative path).
     * @return
     */
    @Override
    public boolean save(String file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json =gson.toJson(gr);

        try{
            PrintWriter pw = new PrintWriter( new File(file));
            pw.write(json);
            pw.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Load can load two type of Jsons one of them is the one based on the graph we saved
     * the second on
     * Load this graph from the given file name,
     * this method deserialize the graph from the given file
     * and init the graph to this set of algorithm.
     * if the file Load successfully return true.
     * Load can load two type of Jsons one of them is the one based on the graph we saved
     * the second one is the Graph we will use for the game that graph has unique Strings at the json
     * that we used to build a special json for it so we can reat it and build a graph.
     * @param file - file name
     * @return
     */
    @Override
    public boolean load(String file) {
try
{
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());
    Gson gson = builder.create();
    FileReader reader = new FileReader(file);
    this.init(gson.fromJson(reader,directed_weighted_graph.class));

} catch (FileNotFoundException e) {
    e.printStackTrace();
    return false;
}
return true;
    }

    /**
     * Dijkstra algorithm is an algorithm to fined the shortest path between nodes(by weight).
     * this method find the shortest distance by weight from src node to all the node in the graph.
     * this method based on 4 data structure nodeDis(HashMap), unused(PriorityQueue), used(HashSet)
     * and nodePar(HashMap).
     * first add the src node to unused PriorityQueue and set is weight to 0,
     * continue while unused is not empty,
     * iterate throw all it's neighbors and every node that not in the nodeDis HaseMap add with -1 weight,
     * (nodeDis contains node(Key) and his distance from src(Value)),
     * if the node weight is bigger then his father weight + the edge weight or node weight = -1
     * replace the node weight with his father + the edge between the node and his father.
     * (skip this step for the father node).
     * add to unused PriorityQueue and if needed add the father to nodePar HaseMap , same for all neighbors.
     * after that pull the next node from unused, because of the PriorityQueue every next node on the list
     * will be with the lightest weight.
     * after iterate throw all the graph all the nodes will be in the nodeDis Hashmap and contain in the value
     * their min weight from src node.
     * also the nodePar will contain every node and his father when called from shortestPath method.
     *
     * @param src
     */
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

    /**
     * resets Graph nodes Weight to -1 after using Dijkstra.
     */
    private void reset_nodes() {
        Iterator<node_data> it = gr.getV().iterator();
        while (it.hasNext()) {
            node_data temp = it.next();
            temp.setWeight(-1);
        }
    }
    @Override
    public List<node_data> connected_component(int key){
        Queue<node_data> Q = new LinkedList<>();
        List<node_data> connected_to = new ArrayList<>();
        List<node_data> connected_from = new ArrayList<>();
        List<node_data> res = new ArrayList<>();
        Q.add(gr.getNode(key));
        while(!Q.isEmpty()){
            for (edge_data e : gr.getE(Q.poll().getKey())) {
                if(gr.getNode(e.getDest()).getWeight()==-1){
                    gr.getNode(e.getDest()).setWeight(0);
                    connected_to.add(gr.getNode(e.getDest()));
                    Q.add(gr.getNode(e.getDest()));
                }
            }
        }
        reset_nodes();
        directed_weighted_graph tmp = ReversGr();
        Q.add(tmp.getNode(key));
        while(!Q.isEmpty()){
            for (edge_data e: tmp.getE(Q.poll().getKey())) {
                if(tmp.getNode(e.getDest()).getWeight()==-1){
                    tmp.getNode(e.getDest()).setWeight(0);
                    connected_from.add(tmp.getNode(e.getDest()));
                    Q.add(tmp.getNode(e.getDest()));
                }

            }
        }
        for (node_data n:connected_to) {
            if(connected_from.contains(n)){
                res.add(n);
            }
        }
        return res;
    }
    @Override
    public List<List<node_data>> connected_components(){

List<List<node_data>> res = new ArrayList<>();
List<node_data> tmp = new ArrayList<>();
        List<node_data> tmp1 = new ArrayList<>();
        for (node_data n :gr.getV()) {
            tmp.add(n);
        }
        while(!tmp.isEmpty()){
            tmp1 = connected_component(tmp.get(0).getKey());
            res.add(tmp1);
            for (node_data n :tmp1) {
                tmp.remove(n);
            }
        }
        return res;
    }
}
