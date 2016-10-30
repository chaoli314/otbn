package graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Created by chaoli on 10/21/16.
 */
public class GraphTest {

    private Graph graph_;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        graph_ = null;
    }

    @Test
    public void V() throws Exception {
        graph_ = new Graph(5);
        assertNotEquals(4, graph_.V());
        assertEquals(5, graph_.V());
        assertNotEquals(6, graph_.V());
    }

    @Test
    public void E() throws Exception {
        graph_ = new Graph(3);
        graph_.addEdge(0, 2);
        assertEquals(1, graph_.E());
        graph_.addEdge();
        assertEquals(2, graph_.E());
        graph_.addEdge();
        assertEquals(3, graph_.E());
    }

    @Test
    public void density() throws Exception {

    }

    @Test
    public void removeEdge() throws Exception {

    }

    @Test
    public void containsEdge() throws Exception {
        graph_ = new Graph(3);
        graph_.addEdge(0, 2);
        assertTrue(graph_.containsEdge(0, 2));
        assertFalse(graph_.containsEdge(0, 1));
        graph_.addEdge(0, 1);
        assertTrue(graph_.containsEdge(0, 1));
    }

    @Test
    public void getNeighbors() throws Exception {
        graph_ = new Graph(3);
        graph_.addEdge(1, 0);
        graph_.addEdge(1, 2);    // neighbors of v1 = {0, 2}
        BitSet bs = new BitSet();
        bs.set(0);
        bs.set(2);
        assertEquals(bs, graph_.getNeighbors(1));
    }

    @Test
    public void degree() throws Exception {
        graph_ = new Graph(3);
        assertEquals(0, graph_.degree(0));

        graph_.addEdge(0, 1);
        assertEquals(1, graph_.degree(0));

        graph_.addEdge(0, 2);
        assertEquals(2, graph_.degree(0));
    }

    @Test
    public void equals() throws Exception {
        Graph g1 = new Graph(3);
        Graph g2 = new Graph(3);

        assertEquals(g1, g2);

        g1.addEdge(0, 1);
        assertNotEquals(g1, g2);

        g2.addEdge(0, 1);
        assertEquals(g1, g2);
    }


    @Test
    public void addEdge() throws Exception {
        graph_ = new Graph(3);
        graph_.addEdge(0, 2);
        assertEquals(1, graph_.E());
        graph_.addEdge();
        assertEquals(2, graph_.E());
        graph_.addEdge();
        assertEquals(3, graph_.E());
        assertTrue(graph_.containsEdge(0, 2));
    }


}