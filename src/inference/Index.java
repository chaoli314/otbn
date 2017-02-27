package inference;

import java.util.List;

/**
 * Created by chaoli on 10/23/16.
 */
public class Index {
    /** indexFor */
    public final static int[] indexFor(VarSet indexVars, VarSet forVars) {
        int numberOfVars = forVars.size();

        // The current linear index corresponding to the state of indexVars
        int _index = 0;
        int tableSize = 1;//tableSize(forVars);
        int[] index = new int[tableSize];

        // For each variable in forVars, the amount of change in _index
        int[] card_ = new int[numberOfVars];

        // For each variable in forVars, the amount of change in _index
        int[] stride_ = new int[numberOfVars];

        // For each variable in forVars, the current state
        int[] assignment_ = new int[numberOfVars]; // 初始状态全部为 零。



        // //////////////////////////////////////////////////////////////////////////////////////////////
        for (int l = 0; l < numberOfVars; ++l)
            card_[l] = forVars.get(l).card();

        int sum = 1;
        for (int i = 0; i < indexVars.size(); ++i) {
            int j = forVars.indexOf(indexVars.get(i));
            stride_[j] = sum;
            sum *= card_[j];
        }
        // Increments the current state of forVars.
        /** i_forVars is from 1, since the index[0] = 0, */
        for (int i_forVars = 1; i_forVars < tableSize; ++i_forVars) {
            for (int i = 0; i < numberOfVars; ++i) {
                _index += stride_[i];
                if (++assignment_[i] < card_[i]) break;
                _index -= stride_[i] * card_[i];
                assignment_[i] = 0;
            }
            index[i_forVars] = _index;
        }
        return index;
    }

    /** * calcState */
    public final static int[] calcState(List<Var> vs, int linearState) {
        int[] state = new int[vs.size()];
        for (int i = 0; i < state.length; ++i) {
            state[i] = linearState % vs.get(i).card();
            linearState /= vs.get(i).card();
        }
        return state;
    }

    /** * calcLinearState */
    public final static int calcLinearState(List<Var> vs, int[] state) {
        int prod = 1;
        int linearState = 0;
        for (int i = 0; i < state.length; ++i) {
            linearState += prod * state[i];
            prod *= vs.get(i).card();
        }
        return linearState;
    }
}