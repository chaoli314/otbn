package triangulation;

import graph.Graph;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static util.BitSetUtil.set_intersection;
import static util.GraphUtil.eliminateSimplicial;

/**
 * Created by chaoli on 10/27/16.
 */
public class TriangulationByDFS_DCM_AMBN2015_PIVOTCLIQUE extends TriangulationByDFS {

    protected void expandNode(Graph n_H, BitSet n_remaining, List<BitSet> n_cliques, BigInteger n_tts, int[] weights) {

        elapsed_time_ =         System.nanoTime() - start_time_;
        if(elapsed_time_ / 1000000000L > 3600L){
            System.out.println("time out");
            return;
        }

        //  The number of nodes + 1
        nodeCounter_.increment();

        //  pivot   clique  selection strategy
        BitSet pivotClique = new BitSet(n_H.V());
        int max = 0;

        for (BitSet clique : n_cliques) {
            int size = set_intersection(clique, n_remaining).cardinality(); //int size = clique.cardinality();
            if (size > max) {
                max = size;
                pivotClique = clique;
            }
        }

        /*branch & bound*/
        for (int v = n_remaining.nextSetBit(0); v >= 0; v = n_remaining.nextSetBit(v + 1)) {

            // pivot clique pruning.
            if (pivotClique.get(v)) continue;

            /*Let m = Copy(n)*/
            Graph m_H = new Graph(n_H);
            BitSet m_remaining = (BitSet) n_remaining.clone();
            List<BitSet> m_cliques = new ArrayList<>(n_cliques);
            BigInteger m_tts = n_tts;

            /* EliminateVertex(m, v)*/
            long start_time = System.nanoTime();
            m_tts = TriangulationByDFS_DCM_AMBN2015.eliminateVertex_DCM_AMBN2015(m_H, m_remaining, v, m_cliques, m_tts, weights, cliqueCounter_);
            long end_time = System.nanoTime();
            time_for_DCM_   += (end_time - start_time);
            eliminateSimplicial(m_H, m_remaining);  //  GraphUtil.eliminateSimplicial

            if (m_remaining.isEmpty()) {
                if (m_tts.compareTo(best_tts_) < 0) {   //  Found a new goal node
                    best_H_ = m_H;
                    best_tts_ = m_tts;
                }
            } else {
                //  greater than upper bound
                if (m_tts.compareTo(best_tts_) >= 0) {
                    continue;
                }
                //  map pruning
                if (map.get(m_remaining) != null && map.get(m_remaining).compareTo(m_tts) <= 0) {
                    continue;
                }

                map.put(m_remaining, m_tts);
                expandNode(m_H, m_remaining, m_cliques, m_tts, weights);
            }
        }
    }
}