package inference;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by chaoli on 10/23/16.
 */
public class VarSet extends ArrayList<Var> {

    public VarSet(Collection<? extends Var> c) {
        super(c);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public VarSet() {
    }

    public BigInteger tableSize() {
        BigInteger tableSize = BigInteger.ONE;
        for (Var v : this) tableSize = tableSize.multiply(BigInteger.valueOf(v.card()));
        return tableSize;
    }

    public static VarSet set_difference(final Collection<Var> A, final Collection<Var> B) {
        Set<Var> result = new TreeSet<>();
        result.addAll(A);
        result.removeAll(B);
        return new VarSet(result);
    }

    public static VarSet set_intersection(final Collection<Var> A, final Collection<Var> B) {
        Set<Var> result = new TreeSet<>();
        result.addAll(A);
        result.retainAll(B);
        return new VarSet(result);
    }

    public static VarSet set_union(final Collection<Var> A, final Collection<Var> B) {
        Set<Var> result = new TreeSet<>();
        result.addAll(A);
        result.addAll(B);
        return new VarSet(result);
    }

    public static VarSet set_symmetric_difference(final Collection<Var> A, final Collection<Var> B) {
        throw new java.lang.UnsupportedOperationException();
    }
}