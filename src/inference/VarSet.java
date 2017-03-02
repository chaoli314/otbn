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
    // ~ Constructors ~
    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public VarSet() {
    }
    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public VarSet(Collection<Var> c) {
        super(c);
    }

    // ~ Methods ~
    public BigInteger nrStates() {
        BigInteger states = BigInteger.ONE;
        for (Var var : this) states = states.multiply(BigInteger.valueOf(var.getCard()));
        return states;
    }

    // ~ set_difference ~ set_intersection ~ set_symmetric_difference ~ set_union
    public static VarSet set_difference(Collection<Var> A, Collection<Var> B) {
        Set<Var> res = new TreeSet<>(A);
        res.removeAll(B);
        return new VarSet(res);
    }

    public static VarSet set_intersection(Collection<Var> A, Collection<Var> B) {
        Set<Var> res = new TreeSet<>(A);
        res.retainAll(B);
        return new VarSet(res);
    }

    public static VarSet set_union(Collection<Var> A, Collection<Var> B) {
        Set<Var> res = new TreeSet<>(A);
        res.addAll(B);
        return new VarSet(res);
    }

    public static VarSet set_symmetric_difference(Collection<Var> A, Collection<Var> B) {
        throw new java.lang.UnsupportedOperationException("Invalid operation!");
    }
}