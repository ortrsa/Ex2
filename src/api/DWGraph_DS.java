package api;

import javax.swing.text.EditorKit;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer,node_data> Graph;
    private HashMap<Integer,HashMap<Integer,edge_data>> Edges;
    private HashMap<Integer,HashMap<Integer,edge_data>> Parents;
    private  int MC;
    private int EdgeSize;


    public DWGraph_DS() {
       Graph = new HashMap<>();
       Edges = new HashMap<>();
       MC=0;
       EdgeSize=0;

    }

    @Override
    public node_data getNode(int key) {
        if(!Graph.containsKey(key)){return null;}
        return Graph.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(!Graph.containsKey(src)&&!Graph.containsKey(dest)){return null;}
        if(!Edges.get(src).containsKey(dest)){return null;}
        return Edges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
    if(Graph.containsKey(n.getKey())){return;}
    Graph.put(n.getKey(),n);
    Edges.put(n.getKey(),new HashMap<Integer, edge_data>());
    Parents.put(n.getKey(),new HashMap<Integer, edge_data>());
    MC++;
    }

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

    @Override
    public Collection<node_data> getV() {
        return Graph.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        Collection<edge_data> Neigh = new HashSet();
        Iterator<Integer> itr = Edges.get(node_id).keySet().iterator();
        while(itr.hasNext()){
            Neigh.add(getEdge(node_id, itr.next()));
        }
        return Neigh;
    }

    @Override
    public node_data removeNode(int key) {
        if(!Graph.containsKey(key)){return null;}
        Iterator<Integer>itr= Edges.get(key).keySet().iterator();
        while(itr.hasNext()){
            Edges.get(key).remove(itr.next());
            EdgeSize--;
            MC++;
        }
        Iterator<Integer>itr1= Parents.get(key).keySet().iterator();
        while(itr.hasNext()){
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

    @Override
    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }
    ///////////////////////////////////////////////





//    public class Geo_Location implements geo_location {
//        private double X;
//        private double Y;
//        private double Z;
//
//        public Geo_Location(double X, double Y , double Z){
//            this.X=X;
//            this.Y=Y;
//            this.Z=Z;
//        }
//
//        @Override
//        public double x() {
//            return X;
//        }
//
//        @Override
//        public double y() {
//            return Y;
//        }
//
//        @Override
//        public double z() {
//            return Z;
//        }
//
//        @Override
//        public double distance(geo_location g) {
//            return Math.sqrt(Math.pow(this.X-g.x(),2)+Math.pow(this.Y-g.y(),2)+Math.pow(this.Z-g.z(),2));
//        }
//    }
}
