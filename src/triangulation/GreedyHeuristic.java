package triangulation;

import graph.Graph;

import java.util.BitSet;

/**
 * Created by chaoli on 10/27/16.
 */
public abstract class GreedyHeuristic {

    public Graph run(final Graph originalGraph, int... weights) {

        Graph graph = new Graph(originalGraph);

        BitSet remaining = new BitSet(graph.V());
        remaining.set(0, graph.V());

        while (!remaining.isEmpty()) {

            int lowest_cost = Integer.MAX_VALUE;

            int next_vertex = 0;

            for (int v = remaining.nextSetBit(0); v >= 0; v = remaining.nextSetBit(v + 1)) {
                int cost_of_v = compute_cost(graph, remaining, v, weights);
                if (cost_of_v < lowest_cost) {
                    lowest_cost = cost_of_v;
                    next_vertex = v;
                }
            }
            util.GraphUtil.eliminateVertex(graph, remaining, next_vertex);
        }
        return graph;
    }



    /**
     *
     * @param graph
     * @param remaining
     * @param v
     * @param weights   weights of vertex set.
     * @return
     */
    protected abstract int compute_cost(Graph graph, BitSet remaining, int v, int... weights);
}