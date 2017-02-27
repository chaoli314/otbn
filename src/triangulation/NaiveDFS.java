package triangulation;

import graph.BronKerboschAlgorithm;
import graph.Graph;
import util.BitSetUtil;

import java.math.BigInteger;
import java.util.*;

import static util.GraphUtil.eliminateSimplicial;

/**
 * Created by chaoli on 10/28/16.
 */
public class NaiveDFS extends OptimalTriangulation {

    protected Graph best_H_;
    protected BigInteger best_tts_;    //  upper bound;
    protected Map<BitSet, BigInteger> map;

    @Override
    public Graph run(final Graph originalGraph, int... weights) {

        Graph s_H = new Graph(originalGraph);
        BitSet s_remaining = new BitSet(originalGraph.V());
        s_remaining.set(0, originalGraph.V());

        List<BitSet> s_cliques = new BronKerboschAlgorithm(s_H).getAllMaximalCliques();
        BigInteger s_tts = BitSetUtil.totalTableSize(s_cliques, weights);

        //eliminateSimplicial(s_H, s_remaining);

        if (s_remaining.isEmpty()) return s_H;

        best_H_ = new GreedyFillin().run(s_H);
        List<BitSet> best_cliques = new BronKerboschAlgorithm(best_H_).getAllMaximalCliques();
        best_tts_ = BitSetUtil.totalTableSize(best_cliques, weights);    //  upper bound;
        map = new HashMap<>();

        long start_time = System.nanoTime();
        expandNode(s_H, s_remaining, s_cliques, s_tts, weights);
        long end_time = System.nanoTime();
        time_for_total_ += (end_time - start_time);
        this.printStatistics();
        return best_H_;
    }

    @Override
    public void printStatistics() {

        System.out.println(best_tts_ + ", " + time_for_DCM_ * 10000 / 1000000000L / 10000.0 + ", " + time_for_total_ * 10000 / 1000000000L / 10000.0 + ", " +
                nodeCounter_.tally() + ", " + cliqueCounter_.tally());
    }

    protected void expandNode(Graph n_H, BitSet n_remaining, List<BitSet> n_cliques, BigInteger n_tts, int[] weights) {
        //  The number of nodes + 1

            nodeCounter_.increment();

        /*branch & bound*/
        for (int v = n_remaining.nextSetBit(0); v >= 0; v = n_remaining.nextSetBit(v + 1)) {

            /*Let m = Copy(n)*/
            Graph m_H = new Graph(n_H);
            BitSet m_remaining = (BitSet) n_remaining.clone();
            List<BitSet> m_cliques = new ArrayList<>(n_cliques);
            BigInteger m_tts = n_tts;

            /* EliminateVertex(m, v)*/
            long start_time = System.nanoTime();

            m_tts = TriangulationByDFS_DCM_OandV.eliminateVertex_DCM_OandV(m_H, m_remaining, v, m_cliques, m_tts, weights, cliqueCounter_);

            //  long start_time = System.nanoTime();
            long end_time = System.nanoTime();
            time_for_DCM_   += (end_time - start_time);

            //eliminateSimplicial(m_H, m_remaining);  //  GraphUtil.eliminateSimplicial

                map.put(m_remaining, m_tts);
                expandNode(m_H, m_remaining, m_cliques, m_tts, weights);

        }
    }


}
