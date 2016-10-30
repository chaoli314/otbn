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
public class Node {

    private Bayesian_network network_;


    private String node_name_;
    private int node_index_;

    private List<String> stateLabels_;
    private Map<String, Integer> statelabel_to_stateIndex_;

    private List<Node> parents_;
    private CPT cpt_;

    public Node(Bayesian_network network_,
                String node_name_,
                int node_index_) {
        this.network_ = network_;
        this.node_name_ = node_name_;
        this.node_index_ = node_index_;

        this.stateLabels_ = new ArrayList<>();
        this.statelabel_to_stateIndex_ = new HashMap<>();
        parents_ = new ArrayList<>();
    }

    public void addParent(Node newParent) {
        parents_.add(newParent);
    }
    public List<Node> getParents(){
        return parents_;
    }

    public CPT generateTable(){

        VarSet vars = new VarSet();

        int childIndex = this.getIndex();
        int childCard = this.getNumberOfStates();
        vars.add(new Var(childIndex, childCard));

        for (Node parent:parents_) {
            int parentIndex = parent.getIndex();
            int parentCard = parent.getNumberOfStates();
            vars.add(new Var(parentIndex, parentCard));
        }

        int tableSize = util.BigIntegerUtil.toIntExact(vars.tableSize());
        double[] data = new double[tableSize];

        cpt_ = new CPT(vars,data);
        return cpt_;
    }

    public CPT getTable(){
        return cpt_;
    }


    public String getName() {
        return node_name_;
    }

    public int getIndex() {
        return node_index_;
    }

    public int getNumberOfStates() {
        return stateLabels_.size();
    }


    public int getStateIndex(String stateLabel) {
        return statelabel_to_stateIndex_.get(stateLabel);
    }

    public String getStateLabel(int stateIndex) {
        return stateLabels_.get(stateIndex);
    }

    public void addState(String label) {
        if (statelabel_to_stateIndex_.containsKey(label)) {
            throw new RuntimeException("duplicate state label!");
        }
        statelabel_to_stateIndex_.put(label, stateLabels_.size());
        stateLabels_.add(label);
    }

}
