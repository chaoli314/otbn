package inference;

import java.util.List;

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
    public Var getVar() {
        return super.vars().get(0);
    }

    public List<Var> getParents(){return super.vars().subList(1,super.vars().size());}

    // TODO implement CPT
    // TODO implement CPT
}