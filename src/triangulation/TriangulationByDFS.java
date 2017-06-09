package triangulation;

import graph.BronKerboschAlgorithm;
import graph.Graph;
import util.BitSetUtil;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.GraphUtil.eliminateSimplicial;

/**
 * Created by chaoli on 10/28/16.
 */
public abstract class TriangulationByDFS extends OptimalTriangulation {

    protected Graph best_H_;
    protected BigInteger best_tts_;    //  upper bound;
    protected Map<BitSet, BigInteger> map;

    protected long elapsed_time_;
    protected long start_time_;

    @Override
    public Graph run(final Graph originalGraph, int... weights) {

        Graph s_H = new Graph(originalGraph);
        BitSet s_remaining = new BitSet(originalGraph.V());
        s_remaining.set(0, originalGraph.V());

        List<BitSet> s_cliques = new BronKerboschAlgorithm(s_H).getAllMaximalCliques();
        BigInteger s_tts = BitSetUtil.totalTableSize(s_cliques, weights);

        eliminateSimplicial(s_H, s_remaining);

        if (s_remaining.isEmpty()) return s_H;

        best_H_ = new GreedyFillin().run(s_H);
        List<BitSet> best_cliques = new BronKerboschAlgorithm(best_H_).getAllMaximalCliques();
        best_tts_ = BitSetUtil.totalTableSize(best_cliques, weights);    //  upper bound;
        map = new HashMap<>();

        long start_time = System.nanoTime();

        start_time_ = System.nanoTime();
        elapsed_time_ = 0L;

        expandNode(s_H, s_remaining, s_cliques, s_tts, weights);

        long end_time = System.nanoTime();
        time_for_total_ += (end_time - start_time);
        this.printStatistics();
        return best_H_;
    }

    @Override
    public void printStatistics() {
        System.out.println("optimal tts, dummy, runtime(second), search nodes, dummy");
        System.out.println(best_tts_ + ", " + time_for_DCM_ * 10000 / 1000000000L / 10000.0 + ", " + time_for_total_ * 10000 / 1000000000L / 10000.0 + ", " +
                nodeCounter_.tally() + ", " + cliqueCounter_.tally());
    }

    protected abstract void expandNode(Graph s_H, BitSet s_remaining, List<BitSet> s_cliques, BigInteger s_tts, int[] weights);
}