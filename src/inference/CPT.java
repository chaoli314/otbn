package inference;

/**
 * Created by chaoli on 10/26/16.
 */
public class CPT extends Factor {
    public CPT(VarSet vars, double[] data) {
        super(vars, data);
    }

    /**
     * Returns this CPT's Var(node).
     *
     * @return child Var
     */
    public Var get_var() {
        return this.getVars().get(0);
    }
}
