package api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    public directed_weighted_graph GraphCreator(int x) {
        NodeData n = new NodeData();
        n.resetCk();
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < x; i++) {
            g.addNode(new NodeData());
        }
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
        directed_weighted_graph t = GraphCreator(10);

        directed_weighted_graph gb = new DWGraph_DS();
        ga.init(t);
        gb = ga.copy();

        assertEquals(t.edgeSize(), gb.edgeSize(), "the graphs should have the same edge");
        assertEquals(t.nodeSize(), gb.nodeSize(), "the graphs should have the same nodes");
        assertEquals(t.getEdge(1, 2), gb.getEdge(1, 2), "the graphs should have the same edge");
        t.removeNode(5);
        assertNotEquals(t.nodeSize(), gb.nodeSize(), "the copy shouldn't change if the source graph changed");


    }

    @Test
    void isConnected() {
        dw_graph_algorithms ga = new DWGraph_Algo();
        directed_weighted_graph EmptyG = new DWGraph_DS();
        directed_weighted_graph t = GraphCreator(10);
        t.connect(6, 9, 3);
        t.connect(9, 0, 3);
        ga.init(EmptyG);
        assertTrue(ga.isConnected(), "Empty graph is connected");
        ga.init(t);
        assertTrue(ga.isConnected(), "this graph is connected");
        t.removeEdge(9, 0);
        assertFalse(ga.isConnected(), "this graph isn't connected");
        t.removeNode(6);
        t.removeNode(8);
        t.removeNode(9);
        assertTrue(ga.isConnected(), "this graph is connected");

    }

    @Test
    void shortestPathDist() {
        dw_graph_algorithms ga = new DWGraph_Algo();
        directed_weighted_graph t = GraphCreator(10);
        ga.init(t);
        assertEquals(0, ga.shortestPathDist(0, 0), "the distance between node to itself is 0");
        assertEquals(6.5, ga.shortestPathDist(0, 6), "the distance should be 6.5");
        t.removeNode(4);
        assertEquals(14, ga.shortestPathDist(0, 6), "the distance should be 14");
        assertEquals(-1, ga.shortestPathDist(0, 4), "the distance to removed node is -1");
        assertEquals(-1, ga.shortestPathDist(0, 5), "the path between 0 to 5 ware deleted");
    }

    @Test
    void shortestPath() {
        dw_graph_algorithms ga = new DWGraph_Algo();
        directed_weighted_graph t = GraphCreator(10);
        ga.init(t);

        int[] ExptKey0={0,1,2,3,4,8,6};
        int j=0;
        for (node_data n : ga.shortestPath(0,6)) {
            assertEquals(n.getKey(),ExptKey0[j],"should return the shortest path ");
            j++;
        }

        t.connect(1,0,0);
        t.connect(0,5,3);
        t.connect(2,1,2);
        t.connect(7,2,0.5);
        t.connect(3,2,2);
        t.connect(4,3,1);
        t.connect(6,3,10);
        t.connect(6,8,0);
        t.connect(8,4,1.5);
        t.connect(5,4,2);
        t.connect(5,4,2);
        t.connect(1,7,1);
        t.connect(1,3,4);

        int[] ExptKey1={0,1,7,2,3,4,8,6};
        int i=0;
        for (node_data n : ga.shortestPath(0,6)) {
            assertEquals(n.getKey(),ExptKey1[i],"making this graph undirected should change the shortest path");
            i++;
        }




    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }


}