package api;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeDataTest {

    @Test
    void compareTo() {
        node_data n = new NodeData(0,0,0,1);
        node_data n1 = new NodeData(0,0,2,2);
        node_data n2 = new NodeData(0,0,2,3);
        n2.setWeight(5);
        n.setWeight(10);
        n1.setWeight(0);
        PriorityQueue<node_data> Prior = new PriorityQueue<>();
        Prior.add(n1);
        Prior.add(n);
        Prior.add(n2);
       assertEquals(2,Prior.poll().getKey());
        assertEquals(3,Prior.poll().getKey());
        assertEquals(1,Prior.poll().getKey());
        n2.setWeight(1);
        n.setWeight(2);
        n1.setWeight(3);
        Prior.add(n1);
        Prior.add(n);
        Prior.add(n2);
        assertEquals(3,Prior.poll().getKey());
        assertEquals(1,Prior.poll().getKey());
        assertEquals(2,Prior.poll().getKey());

    }

    @Test
    void Distance(){
        node_data n = new NodeData(0,0,0,1);
        node_data n1 = new NodeData(0,0,2,2);
        node_data n2 = new NodeData(0,0,4,3);
        assertEquals(2,n.getLocation().distance(n1.getLocation()));
        assertEquals(2,n1.getLocation().distance(n2.getLocation()));
        assertEquals(4,n.getLocation().distance(n2.getLocation()));
        assertEquals(n.getLocation().distance(n2.getLocation()),n.getLocation().distance(n1.getLocation())+n1.getLocation().distance(n2.getLocation()));


    }
}