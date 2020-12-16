package gameClient;

import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class comparator implements Comparator<CL_Pokemon> {
    private ArrayList<CL_Agent> ag = new ArrayList<>();
    private directed_weighted_graph g = new DWGraph_DS();
    private dw_graph_algorithms ga = new DWGraph_Algo();

    public comparator() {
        ga.init(g);
    }

    public void update(List<CL_Agent> ag) {
        this.ag.addAll(ag);
    }

    public double isFaster(CL_Pokemon p) {
        double dis = -1;
        double min = -1;

        Iterator<CL_Agent> it = this.ag.iterator();
        while (it.hasNext()) {
            CL_Agent agent = it.next();

            if (agent.getNextNode() == -1) {
                dis = (ga.shortestPathDist(agent.getSrcNode(), p.get_edge().getSrc())) / agent.getSpeed();

            } else {
                double dis1 = g.getNode(agent.get_curr_edge().getDest()).getLocation().distance(agent.getLocation());
                double dis2 = g.getNode(agent.get_curr_edge().getDest()).getLocation().distance(g.getNode(p.get_edge().getSrc()).getLocation());
                dis = (dis1 + dis2) / agent.getSpeed();
            }
            if (min == -1 || dis < min) {
                min = dis;
            }
        }
        return min;
    }

    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {

        if(ag.isEmpty()){
            double o1val = o1.getValue();/// o1min ;
            double o2val = o2.getValue();// / o2min ;
            if (o1val < o2val) {
                return 1;
            }
            if (o1val > o2val) {
                return -1;
            }
            return 0;
        }else {
            double o1min = isFaster(o1);
            double o2min = isFaster(o2);
            double o1val = o1.getValue() / o1min ;
            double o2val = o2.getValue() / o2min ;
            if (o1val < o2val) {
                return 1;
            }
            if (o1val > o2val) {
                return -1;
            }
            return 0;
        }
    }

    public void updategg(directed_weighted_graph g) {
        this.g = g;
    }
}
