package util;

import graph.Graph;

import java.util.BitSet;

/**
 * Created by chaoli on 10/27/16.
 */
public class GraphUtil {

    public static void eliminateVertex(Graph graph, BitSet remaining, int v) {
        BitSet nb = util.BitSetUtil.set_intersection(graph.getNeighbors(v), remaining);

        for (int w = nb.nextSetBit(0); w >= 0; w = nb.nextSetBit(w + 1)) {
            //  make w simplicial except self loop w-w
            graph.getNeighbors(w).or(nb);
            graph.getNeighbors(w).clear(w);
        }
        remaining.clear(v);
    }

    public static void eliminateSimplicial(Graph graph, BitSet remaining) {
        boolean finish = false;
        while (!finish) {
            finish = true;
            for (int v = remaining.nextSetBit(0); v >= 0; v = remaining.nextSetBit(v + 1)) {
                if (isSimplicial(graph, remaining, v)) {
                    remaining.clear(v);
                    finish = false;
                    break;
                }
            }
        }
    }

    public static boolean isSimplicial(Graph graph, BitSet missing, int v) {
        BitSet nb = util.BitSetUtil.set_intersection(graph.getNeighbors(v), missing);
        for (int w = nb.nextSetBit(0); w >= 0; w = nb.nextSetBit(w + 1)) {
            nb.clear(w);
            if (!(util.BitSetUtil.set_difference(nb, graph.getNeighbors(w)).isEmpty())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isClique(BitSet clique, Graph G) {

        if (clique.cardinality() < 2) return false;

        BitSet nb = new BitSet(G.V());
        //nb.set(0,G.V());
        for (int w = clique.nextSetBit(0); w >= 0; w = clique.nextSetBit(w + 1))
            nb.or(G.getNeighbors(w));
        nb.andNot(clique);

        for (int w = nb.nextSetBit(0); w >= 0; w = nb.nextSetBit(w + 1)) {
            if (BitSetUtil.includes(G.getNeighbors(w), clique)) return false;
        }
        return true;
    }


}
