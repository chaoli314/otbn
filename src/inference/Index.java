package inference;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by chaoli on 10/23/16.
 */
public class Index {
    /**
     * IndexFor
     */
    public final static int[] IndexFor(VarSet indexVars, VarSet forVars) {
        final int kNumberOfVars = forVars.size();
        final int kTableSize = forVars.nrStates().intValueExact();
        // card[]
        int[] card = new int[kNumberOfVars];
        for (int l = 0; l < kNumberOfVars; ++l)
            card[l] = forVars.get(l).getCard();
        // stride[]
        int[] stride = new int[kNumberOfVars];
        for (int i = 0, sum = 1; i < indexVars.size(); ++i) {
            int j = forVars.indexOf(indexVars.get(i));
            stride[j] = sum;
            sum *= card[j];
        }
        // ~ initiate ~
        int[] index = new int[kTableSize];
        int _index = 0;
        int[] assignment = new int[kNumberOfVars];

        // ~ iteration ~
        for (int i = 0; i < kTableSize; ++i) {
            index[i] = _index;
            for (int l = 0; l < kNumberOfVars; ++l) {
                ++assignment[l];
                if (assignment[l] == card[l]) {
                    _index -= (card[l] - 1) * stride[l];
                    assignment[l] = 0;
                } else {
                    _index += stride[l];
                    break;
                }
            }
        }
        return index;
    }

    /**
     * calcLinearState
     */
    public final static int calcLinearState(final List<Var> vs, Map<Var, Integer> state) {
        int prod = 1;
        int linearState = 0;
        // ~ linearState ~
        for (Var v : vs) {
            int m = state.get(v);
            linearState += prod * m;
            prod *= v.getCard();
        }
        return linearState;
    }

    /**
     * calcState
     */
    public final static Map<Var, Integer> calcState(List<Var> vs, int linearState) {
        Map<Var, Integer> state = new TreeMap<>();
        for (Var v : vs) {
            state.put(v, linearState % v.getCard());
            linearState /= v.getCard();
        }
        return state;
    }
}