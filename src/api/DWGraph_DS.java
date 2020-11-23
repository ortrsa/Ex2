package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    private static int Ck=0;
    @Override
    public node_data getNode(int key) {
        return null;
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return null;
    }

    @Override
    public void addNode(node_data n) {

    }

    @Override
    public void connect(int src, int dest, double w) {

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
    private class Edge implements edge_data {

        @Override
        public int getSrc() {
            return 0;
        }

        @Override
        public int getDest() {
            return 0;
        }

        @Override
        public double getWeight() {
            return 0;
        }

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public void setInfo(String s) {

        }

        @Override
        public int getTag() {
            return 0;
        }

        @Override
        public void setTag(int t) {

        }
    }
    private class DWNode implements node_data{
        private HashMap<Integer,Double> Neighbors;
        private int key;
        private geo_location geo;
        private String Info;
        private int tag;

public DWNode(){
    this.key=Ck;
    Ck++;
    this.Neighbors=new HashMap<Integer, Double>();
    this.Info="";
    this.tag=-1;
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

        }

        @Override
        public double getWeight() {
            return Neighbors.;
        }

        @Override
        public void setWeight(double w) {

        }

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public void setInfo(String s) {

        }

        @Override
        public int getTag() {
            return 0;
        }

        @Override
        public void setTag(int t) {

        }
    }
}
