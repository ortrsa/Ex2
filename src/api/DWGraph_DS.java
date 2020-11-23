package api;

import java.util.Collection;

public class DWGraph_DS implements directed_weighted_graph {
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
    private class DWNode implements node_data{

        @Override
        public int getKey() {
            return 0;
        }

        @Override
        public geo_location getLocation() {
            return null;
        }

        @Override
        public void setLocation(geo_location p) {

        }

        @Override
        public double getWeight() {
            return 0;
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
