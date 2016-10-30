package util;

import graph.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertTrue;

/**
 * Created by chaoli on 10/27/16.
 */
public class GraphUtilTest {

    Graph graph_;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        graph_ = null;
    }

    @Test
    public void isSimplicial() throws Exception {
        graph_ = new Graph(5);
        graph_.addEdge(0, 1);
        graph_.addEdge(0, 2);
        graph_.addEdge(1, 2);
        graph_.addEdge(1, 3);
        graph_.addEdge(2, 3);
        graph_.addEdge(2, 4);
        graph_.addEdge(3, 4);   // v0 & v4 are simplicial.

        BitSet remaining = new BitSet();
        remaining.set(0,5);

        assertTrue(GraphUtil.isSimplicial(graph_,remaining,0));
        assertTrue(!GraphUtil.isSimplicial(graph_,remaining,1));
        assertTrue(!GraphUtil.isSimplicial(graph_,remaining,2));
        assertTrue(!GraphUtil.isSimplicial(graph_,remaining,3));
        assertTrue(GraphUtil.isSimplicial(graph_,remaining,4));
    }

}