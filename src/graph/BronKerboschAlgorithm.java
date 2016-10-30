package graph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static util.BitSetUtil.*;

/**
 * Created by chaoli on 10/21/16.
 */
public class BronKerboschAlgorithm {

    private final Graph graph_;
    private final BitSet subGraph_;
    private List<BitSet> cliques_;

    public BronKerboschAlgorithm(Graph graph) {
        this.graph_ = graph;
        BitSet subGraph = new BitSet(graph.V());
        subGraph.set(0, graph.V()); //  whole graph
        this.subGraph_ = subGraph;
    }

    public BronKerboschAlgorithm(Graph graph, BitSet subGraph) {
        this.graph_ = graph;
        this.subGraph_ = subGraph;
    }

    public BitSet getMaximumClique(){
        getAllMaximalCliques();

        BitSet maximumClique = cliques_.get(0);
        int maximumSize = maximumClique.cardinality();

        for (BitSet clique : cliques_) {
            if (maximumSize < clique.cardinality()) {
                maximumSize = clique.cardinality();
                maximumClique = clique;
            }
        }
        return maximumClique;
    }

    public List<BitSet> getAllMaximalCliques() {
        cliques_ = new ArrayList<>();
        BitSet R = new BitSet();    //  empty
        BitSet P = subGraph_;
        BitSet X = new BitSet();//  empty
        BronKerbosch2(R, P, X);
        return cliques_;
    }

    private void BronKerbosch2(BitSet R, BitSet P, BitSet X) {
        if (P.isEmpty() && X.isEmpty()) {
            cliques_.add(R);
        } else {
            int u = choosePivotVertex(set_union(P,X));
            BitSet P_minus_Nb_u = set_difference(P, graph_.getNeighbors(u));

            for (int v = P_minus_Nb_u.nextSetBit(0); v >= 0 ; v = P_minus_Nb_u.nextSetBit(v+1)){

                BronKerbosch2 ( set_union(R,v),/* union: R|{v} */
                        set_intersection(P, graph_.getNeighbors(v)),
                        set_intersection(X, graph_.getNeighbors(v)) );

                P.clear(v);
                X.set(v);
            }
        }
    }

    private int choosePivotVertex(BitSet bs) {
        int u = bs.nextSetBit(0);
        for (int i = bs.nextSetBit(u+1) ; i >= 0; i = bs.nextSetBit(i+1)) {
            if(graph_.degree(i)  >  graph_.degree(u)){
                u = i;
            }
        }
        return u;
    }
}