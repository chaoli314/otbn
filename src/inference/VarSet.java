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
    public VarSet(Collection<? extends Var> c) {
        super(c);
    }

    public  VarSet (Var t) {
        this.add(t);
    }

    public  VarSet (Var t1, Var t2){
        this.add(t1);
        this.add(t2);
    }

    // ~ Methods ~
    public BigInteger nrStates() {
        BigInteger states = BigInteger.ONE;
        for (Var var : this) states = states.multiply(BigInteger.valueOf(var.getCard()));
        return states;
    }

    // ~ set_difference ~ set_intersection ~ set_symmetric_difference ~ set_union
    public static VarSet set_difference(final Collection<Var> V, final Collection<Var> W) {
        Set<Var> res = new TreeSet<>(V);
        res.removeAll(W);
        return new VarSet(res);
    }

    public static VarSet set_intersection(final Collection<Var> V, final Collection<Var> W) {
        Set<Var> res = new TreeSet<>(V);
        res.retainAll(W);
        return new VarSet(res);
    }

    public static VarSet set_union(final Collection<Var> V, final Collection<Var> W) {
        Set<Var> res = new TreeSet<>(V);
        res.addAll(W);
        return new VarSet(res);
    }

    public static VarSet set_symmetric_difference(final Collection<Var> V, final Collection<Var> W) {
        throw new java.lang.UnsupportedOperationException("Invalid operation!");
    }
}