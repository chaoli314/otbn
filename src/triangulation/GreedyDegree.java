package triangulation;

import graph.Graph;

import java.util.BitSet;

/**
 * Created by chaoli on 10/27/16.
 */
public class GreedyDegree extends GreedyHeuristic {

    @Override
    protected int compute_cost(Graph graph, BitSet remaining, int v, int... weights) {
        BitSet nb = util.BitSetUtil.set_intersection(graph.getNeighbors(v), remaining);
        return nb.cardinality();
    }
}