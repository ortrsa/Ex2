package api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    public directed_weighted_graph GrapgCreator(int x) {

        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < x; i++) {
            g.addNode(new NodeData());
        }
//        for (int i = 0; i < x-1; i++) {
//            g.connect(i,i+1,(i*4)%3);
//        }
        g.connect(0, 1, 0);
        g.connect(1, 2, 2);
        g.connect(2, 7, 0.5);
        g.connect(7, 1, 1);
        g.connect(2, 3, 2);
        g.connect(3, 1, 4);
        g.connect(3, 6, 10);
        g.connect(3, 4, 1);
        g.connect(4, 8, 1.5);
        g.connect(8, 6, 0);
        g.connect(8, 9, 13);
        g.connect(4, 5, 2);
        g.connect(5, 0, 3);


        return g;
    }

    @Test
    void init() {

    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
        dw_graph_algorithms ga = new DWGraph_Algo();
        directed_weighted_graph t = GrapgCreator(10);

        directed_weighted_graph gb = new DWGraph_DS();
        ga.init(t);
        gb = ga.copy();

        assertEquals(t.edgeSize(), gb.edgeSize(), "the graphs should have the same edge");
        assertEquals(t.nodeSize(), gb.nodeSize(), "the graphs should have the same nodes");
        assertEquals(t.getEdge(1, 2), gb.getEdge(1, 2), "the graphs should have the same edge");
        System.out.println(t.nodeSize());
        t.removeNode(5);
        System.out.println(t.nodeSize());
        assertNotEquals(t.nodeSize(), gb.nodeSize(), "the copy shouldn't change if the source graph changed");


    }

    @Test
    void isConnected() {
    }

    @Test
    void shortestPathDist() {
    }

    @Test
    void shortestPath() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }

    @Test
    void dijkstra() {
    }
}