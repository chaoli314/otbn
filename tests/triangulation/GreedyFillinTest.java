package triangulation;

import graph.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 10/27/16.
 */
public class GreedyFillinTest {

    Graph graph_;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        graph_ = null;
    }

    @Test
    public void compute_cost() throws Exception {

        graph_ = new Graph(5);
        graph_.addEdge(0, 1);
        graph_.addEdge(0, 2);
        graph_.addEdge(1, 2);
        graph_.addEdge(1, 3);
        graph_.addEdge(2, 4);
        graph_.addEdge(3, 4);

        Graph H = new Graph(graph_);

        BitSet remaining = new BitSet();
        remaining.set(0, 5);

        assertEquals(0, new GreedyFillin().compute_cost(H, remaining, 0));
        util.GraphUtil.eliminateVertex(H, remaining, 0);

        assertEquals(1, new GreedyFillin().compute_cost(H, remaining, 1));
        util.GraphUtil.eliminateVertex(H, remaining, 1);

        assertEquals(0, new GreedyFillin().compute_cost(H, remaining, 2));
        util.GraphUtil.eliminateVertex(H, remaining, 2);

        assertEquals(0, new GreedyFillin().compute_cost(H, remaining, 3));
        util.GraphUtil.eliminateVertex(H, remaining, 3);

        assertEquals(0, new GreedyFillin().compute_cost(H, remaining, 4));
        util.GraphUtil.eliminateVertex(H, remaining, 4);

        assertEquals(1, H.E() - graph_.E());
    }

}