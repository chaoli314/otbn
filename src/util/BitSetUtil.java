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


    public static boolean includes(final BitSet superSet, final BitSet subSet) {
        BitSet copiedSubSet= (BitSet) subSet.clone();
        copiedSubSet.andNot(superSet);
        return copiedSubSet.isEmpty();
    }

    public static BitSet set_difference(final BitSet A, final BitSet B) {
        BitSet result = (BitSet) A.clone();
        result.andNot(B);
        return result;
    }

    public static BitSet set_intersection(final BitSet A, final BitSet B) {
        BitSet result = (BitSet) A.clone();
        result.and(B);
        return result;
    }

    public static BitSet set_symmetric_difference(final BitSet A, final BitSet B) {
        BitSet result = (BitSet) A.clone();
        result.xor(B);
        return result;
    }

    public static BitSet set_union(final BitSet A, final BitSet B) {
        BitSet result = (BitSet) A.clone();
        result.or(B);
        return result;
    }

    public static BitSet set_union(final BitSet R, final int v) {
        BitSet R_or_v = (BitSet) R.clone();
        R_or_v.set(v);
        return R_or_v;
    }
}
