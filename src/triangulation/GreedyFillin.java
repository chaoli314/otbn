package triangulation;

import graph.Graph;

import java.util.BitSet;

/**
 * Created by chaoli on 10/27/16.
 */
public class GreedyFillin extends GreedyHeuristic {
    @Override
    protected int compute_cost(Graph graph, BitSet remaining, int v, int... weights) {
        int cost = 0;
        BitSet nb = util.BitSetUtil.set_intersection(graph.getNeighbors(v), remaining);
        for (int v1 = nb.nextSetBit(0); v1 >= 0; v1 = nb.nextSetBit(v1 + 1)) {
            for (int v2 = nb.nextSetBit(v1 + 1); v2 >= 0; v2 = nb.nextSetBit(v2 + 1)) {
                if (!graph.containsEdge(v1, v2)) {
                    ++cost;
                }
            }
        }
        return cost;
    }
}
