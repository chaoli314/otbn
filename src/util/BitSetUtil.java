package util;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * Created by chaoli on 10/21/16.
 */
public class BitSetUtil {


    public static BigInteger tableSize(final BitSet clique, int[] weights) {
        if (clique.isEmpty()) return BigInteger.ZERO;
        BigInteger tableSize = BigInteger.ONE;
        for (int v = clique.nextSetBit(0); v >= 0; v = clique.nextSetBit(v + 1)) {
            tableSize = tableSize.multiply(BigInteger.valueOf(weights[v]));
        }
        return tableSize;
    }

    public static BigInteger totalTableSize(final Iterable<BitSet> cliques, int[] weights) {
        BigInteger totalTableSize = BigInteger.ZERO;
        for (BitSet clique : cliques) {
            totalTableSize = totalTableSize.add(tableSize(clique, weights));
        }
        return totalTableSize;
    }

    //
    public static boolean includes(final BitSet superSet, final BitSet subSet) {
        BitSet result = (BitSet) subSet.clone();
        result.andNot(superSet);
        return result.isEmpty(); // Is Subset\Superset empty
    }

    //  V \ W
    public static BitSet set_difference(final BitSet V, final BitSet W) {
        BitSet result = (BitSet) V.clone();
        result.andNot(W);
        return result;
    }

    //  V & W
    public static BitSet set_intersection(final BitSet V, final BitSet W) {
        BitSet result = (BitSet) V.clone();
        result.and(W);
        return result;
    }

    //
    public static BitSet set_union(final BitSet V, final BitSet W) {
        BitSet result = (BitSet) V.clone();
        result.or(W);
        return result;
    }

    public static BitSet set_union(final BitSet V, final int w) {
        BitSet result = (BitSet) V.clone();
        result.set(w);
        return result;
    }

    //
    public static BitSet set_symmetric_difference(final BitSet A, final BitSet B) {
        BitSet result = (BitSet) A.clone();
        result.xor(B);
        return result;
    }
}
