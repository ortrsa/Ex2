package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private directed_weighted_graph TmpGr;

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
    void getNode() {
        TmpGr = GraphCreator(10);
        assertNotNull(TmpGr.getNode(0));
        TmpGr.removeNode(0);
        assertNull(TmpGr.getNode(0));
    }

    @Test
    void getEdge() {
        TmpGr = GraphCreator(10);
        assertEquals(TmpGr.getEdge(2, 3).getSrc(), 2, "should be same keys");
        assertNull(TmpGr.getEdge(3, 2), "the graph is a directed graph");
        TmpGr.removeEdge(2, 3);
        assertNull(TmpGr.getEdge(2, 3), "shouldn't be an edge between those nodes");
    }

    @Test
    void addNode() {
        TmpGr = GraphCreator(10);
        assertEquals(TmpGr.nodeSize(), 10, "should be 10 nodes in that graph");
        assertNull((TmpGr.getNode(10)), "shouldn't have node with key 10(the 11's node)");
        TmpGr.addNode(new NodeData());
        assertEquals(TmpGr.nodeSize(), 11, "after adding a node this graph should have 11 nodes");
        assertNotNull((TmpGr.getNode(10)), "should have node with key 10(the 11's node)");
    }

    @Test
    void connect() {
        TmpGr = GraphCreator(10);
        assertEquals(TmpGr.edgeSize(), 13, "should have 13 edges");
        TmpGr.connect(2, 3, 2);
        TmpGr.connect(30, 36, 4);
        assertThrows(IllegalArgumentException.class, () -> {
            TmpGr.connect(0, 4, -11);
        }, "should throw exeption if weight is negative");
        assertEquals(TmpGr.edgeSize(), 13, "should still have 13 edges because this edge already exist");
        assertNull(TmpGr.getEdge(3, 2), "there isnt an edge from node with key 3 to node with key 2");
        TmpGr.connect(3, 2, 2);
        assertNotNull(TmpGr.getEdge(3, 2), "now should be an edge between 3 to 2");
        TmpGr.connect(2, 3, 0);
        assertEquals(TmpGr.getEdge(2, 3).getWeight(), 0, "should change the node weight");
        assertEquals(TmpGr.edgeSize(), 14, "shouldn't add to the sum of edges");
    }

    @Test
    void getV() {
        TmpGr = GraphCreator(10);
        assertNotNull(TmpGr.getV(), "");
        Iterator<node_data> Itr = TmpGr.getV().iterator();
        for (int tm = 0; tm < 10; tm++) {
            assertEquals(Itr.next().getKey(), tm, "those keys should be the same");
        }

    }

    @Test
    void getE() {
        TmpGr = GraphCreator(10);
        assertEquals(TmpGr.getE(3).size(), 3, "node with key 3 has 3 edges from him");
        assertFalse(TmpGr.getE(3).contains(TmpGr.getEdge(4, 3)), "shouldn't be the same edge because its a directed graph");
        assertTrue(TmpGr.getE(3).contains(TmpGr.getEdge(3, 6)), "should be the same edge");
        assertTrue(TmpGr.getE(3).contains(TmpGr.getEdge(3, 1)), "should be the same edge");
    }

    @Test
    void removeNode() {
        TmpGr = GraphCreator(10);
        assertEquals(10, TmpGr.nodeSize(), "this graph should have 10 nodes");
        assertEquals(13, TmpGr.edgeSize(), "This graph should have 13 edges");
        TmpGr.removeNode(4);
        TmpGr.removeNode(9);
        assertNull(TmpGr.removeNode(4));
        assertNull(TmpGr.removeNode(30));
        TmpGr.connect(4, 1, 1);
        assertEquals(8, TmpGr.nodeSize(), "2 nodes were deleted (node with key 4 deleted once even we tried twice)");
        assertEquals(9, TmpGr.edgeSize(), "all the edges connected to the deleted nodes were removed as well");
        assertNull(TmpGr.getEdge(4, 1));
    }

    @Test
    void removeEdge() {
        TmpGr = GraphCreator(10);
        assertNotNull(TmpGr.removeEdge(2, 7), "remove Edge 2-7");
        assertNull(TmpGr.removeEdge(2, 7), "remove deleted edge ");
        assertNull(TmpGr.removeEdge(30, 34), "remove edges between nodes that are not in the graph");
        assertFalse(TmpGr.getE(8).isEmpty());
        TmpGr.removeEdge(8, 9);
        TmpGr.removeEdge(8, 6);
        assertTrue(TmpGr.getE(8).isEmpty());


    }

    @Test
    void nodeSize() {
        TmpGr = GraphCreator(1000);
        assertEquals(1000, TmpGr.nodeSize());
        for (int i = 0; i < 100; i++) {
            TmpGr.removeNode(i + 300);
        }
        assertEquals(900, TmpGr.nodeSize());
    }

    @Test
    void edgeSize() {
        TmpGr = GraphCreator(1000);
        for (int i = 0; i < 200; i++) {
            TmpGr.connect(i + 300, i + 303, i * 2 / 3);
        }
        assertEquals(213, TmpGr.edgeSize());
        TmpGr.removeNode(2);
        assertEquals(210, TmpGr.edgeSize());
        TmpGr.removeEdge(0,1);
        assertEquals(209, TmpGr.edgeSize());
        TmpGr.addNode(new NodeData());
        assertEquals(209, TmpGr.edgeSize());

    }

}