package graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 10/21/16.
 */
public class BronKerboschAlgorithmTest {

    private Graph graph_;

    @Before
    public void setUp() throws Exception {
        graph_ = new Graph(6);
        graph_.addEdge(0, 1);
        graph_.addEdge(0, 4);
        graph_.addEdge(1, 2);
        graph_.addEdge(1, 4);
        graph_.addEdge(2, 3);
        graph_.addEdge(3, 4);
        graph_.addEdge(3, 5);
    }

    @After
    public void tearDown() throws Exception {
        graph_ = null;
    }

    @Test
    public void getMaximumClique() throws Exception {
        BronKerboschAlgorithm bk = new BronKerboschAlgorithm(graph_);
        BitSet maximumClique = bk.getMaximumClique();
        BitSet ExpectedMaxClique = new BitSet();
        ExpectedMaxClique.set(0);
        ExpectedMaxClique.set(1);
        ExpectedMaxClique.set(4);   // {0,1,4}
        assertEquals(ExpectedMaxClique,maximumClique);
        assertEquals(3, maximumClique.cardinality());
    }


    @Test
    public void getAllMaximalCliques() throws Exception {

        BronKerboschAlgorithm bk = new BronKerboschAlgorithm(graph_);

        Set<BitSet>   maximalCliques  = new HashSet<>(bk.getAllMaximalCliques());
        Set<BitSet> ExpectedMaxCliques = new HashSet<>();


        BitSet clique1 = new BitSet ();
        clique1.set(1);
        clique1.set(2);   // {1,2}

        BitSet clique2 = new BitSet ();
        clique2.set(0);
        clique2.set(1);
        clique2.set(4);   // {0,1,4}

        BitSet clique3 = new BitSet ();
        clique3.set(2);
        clique3.set(3);   // {2,3}

        BitSet clique4 = new BitSet ();
        clique4.set(3);
        clique4.set(4);   // {3,4}

        BitSet clique5 = new BitSet ();
        clique5.set(3);
        clique5.set(5);   // {3,5}

        ExpectedMaxCliques.add(clique1);
        ExpectedMaxCliques.add(clique2);
        ExpectedMaxCliques.add(clique3);
        ExpectedMaxCliques.add(clique4);
        ExpectedMaxCliques.add(clique5);
        assertEquals(ExpectedMaxCliques, maximalCliques);
    }
}