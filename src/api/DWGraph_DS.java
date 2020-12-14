package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class implements directed_weighted_graph interface
 * and saves a graph by hash maps one for the graph nodes
 * the second one is for nodes and their neighbors
 * and the third one is helping us to know which nodes are pointing on every node.
 *
 */
public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer,node_data> Graph;
    private HashMap<Integer,HashMap<Integer,edge_data>> Edges;
    private HashMap<Integer,HashMap<Integer,edge_data>> Parents;
    private  int MC;
    private int EdgeSize;

    /**
     * constructor for a graph
     */

    public DWGraph_DS() {
       Graph = new HashMap<>();
       Edges = new HashMap<>();
       Parents = new HashMap<>();
       MC=0;
       EdgeSize=0;

    }

    @Override
    public node_data getNode(int key) {
        if(!Graph.containsKey(key)){return null;}
        return Graph.get(key);
    }

    /**
     * returns the edge between node with key src and node with key dest
     * all the edges are saved at the Edges HashMap so after we check if there is such an edge
     * we just return it from the HashMap.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(!Graph.containsKey(src)||!Graph.containsKey(dest)){return null;}
        if(!Edges.get(src).containsKey(dest)){return null;}
        if(!Parents.get(dest).containsKey(src)){throw new IllegalStateException("Edges and Parents should be synchronized");}
        return Edges.get(src).get(dest);
    }

    /**
     * Adds a node to the graph by adding him to the Graph HashMap and
     * creating to empty HashMaps for every node that we add there
     * we can save who this node points and who points on this node.
     * (which edges are going out from this node and which edges are going to this node).
     * @param n
     */
    @Override
    public void addNode(node_data n) {
    if(Graph.containsKey(n.getKey())){return;}
    Graph.put(n.getKey(),n);
    Edges.put(n.getKey(),new HashMap<Integer, edge_data>());
    Parents.put(n.getKey(),new HashMap<Integer, edge_data>());
    MC++;
    }

    /**
     * Connects two nodes by creating a new edge with the src and dest we got
     * and w represents the weight and then adds this edge to the src key at
     * Edges HashMap and to the Parents HashMap by the dest key .
     * all this happens after were checking if we have those nodes at the graph
     * and if the weight isnt negative.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
    if(Graph.containsKey(src)&&Graph.containsKey(dest)){
        if(w<0){throw new IllegalArgumentException("weight of an edge should be positive");}
        if(src==dest){return;}
        if(!Edges.get(src).containsKey(dest)){EdgeSize++;}
        Edge edge = new Edge(src,dest,w);
        Edges.get(src).put(dest,edge);
        Parents.get(dest).put(src,edge);
        MC++;
    }
    }

    /**
     * Returns a collection of all the nodes at the graph
     * by going to Graph HashMap and return his values which is a collection like we need.
     * @return
     */
    @Override
    public Collection<node_data> getV() {
        return Graph.values();
    }

    /**
     * Returns a required node neighbors (only the ones that he points at them)
     * creating a new collection the going then going to Edges HashMap and going
     * to node_id key and putting all the edges in the new collection we created and then return it.
     * @param node_id
     * @return
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        Collection<edge_data> Neigh = new HashSet();
        Iterator<Integer> itr = Edges.get(node_id).keySet().iterator();
        while(itr.hasNext()){
            Neigh.add(getEdge(node_id, itr.next()));
        }
        return Neigh;
    }

    /**
     * Deletes a node from the graph by going to all the nodes that he points at(we get it from Edges HashMap)
     * and delete him from parents HashMap so it wont show he points on them while every edge we delete we count EdgeSize--.
     * Then remove him completely from Edges HashMap then deleting him from all the nodes that points on him.
     * and for the finale we remove him from the Graph HashMap.
     * @param key
     * @return
     */
    @Override
    public node_data removeNode(int key) {
        if(!Graph.containsKey(key)){return null;}
        Iterator<Integer>itr= Edges.get(key).keySet().iterator();
        while(itr.hasNext()){
            Parents.get(itr.next()).remove(key);
            EdgeSize--;
            MC++;
        }
        Edges.remove(key);
        Iterator<Integer>itr1= Parents.get(key).keySet().iterator();
        while(itr1.hasNext()){
            Edges.get(itr1.next()).remove(key);
            EdgeSize--;
            MC++;
        }
        node_data n = getNode(key);
        Edges.remove(key);
        Parents.remove(key);
        Graph.remove(key);
        MC++;
        return n;
    }

    /**
     * Deletes an edge between two nodes by Removing dest from Edge HashMap of key.
     * and removing src from dest HashMap of parents.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(!Graph.containsKey(src)){return null;}
        if(!Edges.get(src).containsKey(dest)){return null;}
        edge_data e = getEdge(src,dest);
        Edges.get(src).remove(dest);
        Parents.get(dest).remove(src);
        EdgeSize--;
        MC++;
        return e;
    }

    /**
     * The number of nodes that are int he Graph HashMap is the Nodes number .
     * @return
     */
    @Override
    public int nodeSize() {
        return Graph.size();
    }

    @Override
    public int edgeSize() {
        return EdgeSize;
    }

    @Override
    public int getMC() {
        return MC;
    }

    @Override
    public String toString() {
        return "DWGraph_DS{" +
                "Graph=" + Graph +
                ", Edges=" + Edges +
                ", Parents=" + Parents +
                ", MC=" + MC +
                ", EdgeSize=" + EdgeSize +
                '}';
    }

}
