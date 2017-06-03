package bayesian_network;

import inference.CPT;
import inference.Var;
import inference.VarSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaoli on 10/20/16.
 */
public class Node extends Var {

    private BayesianNetwork network_;

    // super.getIndex();
    private String node_name_;

    private List<String> stateLabels_;
    private Map<String, Integer> stateLabel_to_stateIndex_;

    private List<Node> parents_;

    // ~ CPT ~
    private CPT cpt_;

    public Node(BayesianNetwork network,
                String node_name,
                int node_index) {
        super(node_index, 0);
        this.network_ = network;
        this.node_name_ = node_name;

        this.stateLabels_ = new ArrayList<>();
        this.stateLabel_to_stateIndex_ = new HashMap<>();

        parents_ = new ArrayList<>();
        cpt_ = null;
    }

    // ~ Methods ~
    public String getName() {
        return node_name_;
    }

    public int getStateIndex(String stateLabel) {
        return stateLabel_to_stateIndex_.get(stateLabel);
    }

    public String getStateLabel(int stateIndex) {
        return stateLabels_.get(stateIndex);
    }

    public void addState(String stateLabel) {
        stateLabel_to_stateIndex_.put(stateLabel, super.getCard());
        stateLabels_.add(stateLabel);
        super.setCard(1 + super.getCard());
    }

    // ~ Parents ~
    public void addParent(Node parent) {
        parents_.add(parent);
    }

    public List<Node> getParents() {
        return parents_;
    }

    // ~ Create CPT ~
    public CPT generateTable(){

        VarSet vars = new VarSet();

        int childIndex = super.getIndex();
        int childCard = super.getCard();
        vars.add(new Var(childIndex, childCard));

        for (Node parent:parents_) {
            int parentIndex = parent.getIndex();
            int parentCard = parent.getCard();
            vars.add(new Var(parentIndex, parentCard));
        }

        int tableSize = vars.nrStates().intValueExact();
        double[] data = new double[tableSize];
        cpt_ = new CPT(vars,data);
        return cpt_;
    }

    public CPT getTable(){
        return cpt_;
    }
}