package api;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private directed_weighted_graph TmpGr;
    public directed_weighted_graph GraphCreator(int x) {

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

    }

    @Test
    void getEdge() {
        TmpGr = GraphCreator( 10);
       assertEquals(TmpGr.getEdge(2,3).getSrc(),2,"should be same keys");
       assertNull(TmpGr.getEdge(3,2),"the graph is a directed graph");
       TmpGr.removeEdge(2,3);
        assertNull(TmpGr.getEdge(2,3),"shouldn't be an edge between those nodes");
    }

    @Test
    void addNode() {
        TmpGr = GraphCreator( 10);
        assertEquals(TmpGr.nodeSize(),10,"should be 10 nodes in that graph");
        assertNull((TmpGr.getNode(10)),"shouldn't have node with key 10(the 11's node)");
        TmpGr.addNode(new NodeData());
        assertEquals(TmpGr.nodeSize(),11,"after adding a node this graph should have 11 nodes");
        assertNotNull((TmpGr.getNode(10)),"should have node with key 10(the 11's node)");
    }

    @Test
    void connect() {
        TmpGr = GraphCreator( 10);
        assertEquals(TmpGr.edgeSize(),13,"should have 13 edges");
        TmpGr.connect(2,3,2);
        TmpGr.connect(30,36,4);
        assertThrows(IllegalArgumentException.class,() ->{TmpGr.connect(0,4,-11);},"should throw exeption if weight is negative");
        assertEquals(TmpGr.edgeSize(),13,"should still have 13 edges because this edge already exist");
        assertNull(TmpGr.getEdge(3,2),"there isnt an edge from node with key 3 to node with key 2");
        TmpGr.connect(3,2,2);
        assertNotNull(TmpGr.getEdge(3,2),"now should be an edge between 3 to 2");
        TmpGr.connect(2,3,0);
        assertEquals(TmpGr.getEdge(2,3).getWeight(),0,"should change the node weight");
        assertEquals(TmpGr.edgeSize(),14,"shouldn't add to the sum of edges");
    }

    @Test
    void getV() {
        TmpGr = GraphCreator( 10);
        assertNotNull(TmpGr.getV(),"");
        Iterator<node_data>Itr = TmpGr.getV().iterator();
        for (int tm =0;tm<10;tm++){
            assertEquals(Itr.next().getKey(),tm,"those keys should be the same");
        }

    }

    @Test
    void getE() {
        TmpGr = GraphCreator( 10);
        Iterator<edge_data> Itr = TmpGr.getE(3).iterator();
        assertEquals(TmpGr.getE(3).size(),3,"node with key 3 has 3 edges from him");
        assertFalse(Itr.next().equals(TmpGr.getEdge(4,3)),"shouldn't be the same edge because its a directed graph");
        assertTrue(Itr.next().equals(TmpGr.getEdge(3,6)),"should be the same edge");
        assertTrue(Itr.next().equals(TmpGr.getEdge(3,1)),"should be the same edge");
    }

    @Test
    void removeNode() {
        TmpGr = GraphCreator( 10);
        assertEquals(TmpGr.nodeSize(),10,"this graph should have 10 nodes");
        assertEquals(TmpGr.edgeSize(),13,"This graph should have 13 edges");
        TmpGr.removeNode(4);
        TmpGr.removeNode(9);
        assertNull(TmpGr.removeNode(4));
        assertNull(TmpGr.removeNode(30));
        TmpGr.connect(4,1,1);
        assertEquals(TmpGr.nodeSize(),8,"2 nodes were deleted (node with key 4 deleted once even we tried twice)");
        assertEquals(TmpGr.edgeSize(),9,"all the edges connected to the deleted nodes were removed as well");
        assertNull(TmpGr.getEdge(4,1));
    }

    @Test
    void removeEdge() {

    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }

    @Test
    void getMC() {
    }
}