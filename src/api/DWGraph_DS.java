package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    private static int Ck=0;
    private Edge edge;
    private HashMap<Integer,node_data> Graph;
    private HashMap<Integer,HashMap<Integer,Edge>> Edges;

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
    }

    @Override
    public void connect(int src, int dest, double w) {
    if(Graph.containsKey(src)&&Graph.containsKey(dest)&&!Edges.get(src).containsKey(dest)){
        if(w<0){throw new IllegalArgumentException("weight of an edge should be positive");}
        if(src==dest){return;}
        edge = new Edge(src,dest,w);
        Edges.get(src).put(dest,edge);
    }
    }

    @Override
    public Collection<node_data> getV() {
        return null;
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return null;
    }

    @Override
    public node_data removeNode(int key) {
        return null;
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


    private class Edge implements edge_data {

       private int Src;
       private int Dest;
       double Weight;
       String Info ;
       int Tag;

      public Edge(int src, int dest , double weight){
           this.Src = src;
           this.Dest = dest;
           this.Weight = weight;
           this.Tag = -1;
        }

        @Override
        public int getSrc() {

            return Src;
        }

        @Override
        public int getDest() {
            return Dest;
        }

        @Override
        public double getWeight() {
            return Weight;
        }

        @Override
        public String getInfo() {
            return Info;
        }

        @Override
        public void setInfo(String s) {
            this.Info = s;

        }

        @Override
        public int getTag() {
            return Tag;
        }

        @Override
        public void setTag(int t) {
            this.Tag = t;

        }
    }
    ///////////////////////////////////////



    private class DWNode implements node_data{
        private HashMap<Integer,Double> Neighbors;
        private int key;
        private geo_location geo;
        private String Info;
        private int tag;
        private double Weight;

        public DWNode(double X, double Y, double Z){
           geo = new Geo_Location(X,Y,Z);
            this.key=Ck;
            Ck++;
            this.Neighbors=new HashMap<Integer, Double>();
            this.Info="";
            this.tag=-1;
            this.Weight=0;
        }

public DWNode(){
    this.key=Ck;
    Ck++;
    this.Neighbors=new HashMap<Integer, Double>();
    this.Info="";
    this.tag=-1;
    this.Weight=0;
}
        @Override
        public int getKey() {
            return key;
        }

        @Override
        public geo_location getLocation() {

    return geo;
        }

        @Override
        public void setLocation(geo_location p) {
        this.geo=new Geo_Location(p.x(),p.y(),p.z());
        }

        @Override
        public double getWeight() {
            return Weight;
        }

        @Override
        public void setWeight(double w) {
        this.Weight=w;
        }

        @Override
        public String getInfo() {
            return Info;
        }

        @Override
        public void setInfo(String s) {
        this.Info=s;
        }

        @Override
        public int getTag() {
            return this.tag;
        }

        @Override
        public void setTag(int t) {
        this.tag=t;
        }
    }
    public class Geo_Location implements geo_location {
        private double X;
        private double Y;
        private double Z;

        public Geo_Location(double X, double Y , double Z){
            this.X=X;
            this.Y=Y;
            this.Z=Z;
        }

        @Override
        public double x() {
            return X;
        }

        @Override
        public double y() {
            return Y;
        }

        @Override
        public double z() {
            return Z;
        }

        @Override
        public double distance(geo_location g) {
            return Math.sqrt(Math.pow(this.X-g.x(),2)+Math.pow(this.Y-g.y(),2)+Math.pow(this.Z-g.z(),2));
        }
    }
}
