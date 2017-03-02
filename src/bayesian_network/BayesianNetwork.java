package bayesian_network;

import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaoli on 10/20/16.
 */
public class BayesianNetwork {

    private List<Node> nodes_;
    private Map<String, Integer> name_to_index_;

    public BayesianNetwork() {
        this.nodes_ = new ArrayList<>();
        this.name_to_index_ = new HashMap<>();
    }

    // ~ Methods ~

    public Node getNodeByName(String node_name) {
        int node_index = name_to_index_.get(node_name);
        return nodes_.get(node_index);
    }

    public Node getNodeByIndex(int node_index) {
        return nodes_.get(node_index);
    }

    public int getNodeIndex(String node_name) {
        return name_to_index_.get(node_name);
    }

    public String getNodeName(int node_index) {
        return nodes_.get(node_index).getName();
    }

    public Node addNode(String node_name) {
        int node_index = name_to_index_.size();
        name_to_index_.put(node_name, node_index);

        Node node = new Node(this, node_name, node_index);
        nodes_.add(node);
        return node;
    }

    public int getNumberOfNodes() {
        return nodes_.size();
    }

    // TODO adding unit test script.

    public Graph getMoralGraph() {

        int V = nodes_.size();
        Graph graph = new Graph(V);

        for (int childNodeIndex = 0; childNodeIndex < this.getNumberOfNodes(); ++childNodeIndex) {

            Node childNode = getNodeByIndex(childNodeIndex);
            List<Node> parentNodes = childNode.getParents();

            for (Node parentNode : parentNodes) {
                graph.addEdge(/*child -> each parent*/childNode.getIndex(), parentNode.getIndex());
            }

            for (int j = 0; j < parentNodes.size() - 1; ++j) {
                for (int k = j + 1; k < parentNodes.size(); ++k) {
                    int v1 = parentNodes.get(j).getIndex();
                    int v2 = parentNodes.get(k).getIndex();
                    graph.addEdge(/*each pair of parents */v1, v2);
                }
            }
        }
        return graph;
    }

    public int[] getWeights() {
        int N = nodes_.size();
        int[] weight = new int[N];
        for (int i = 0; i < N; ++i) weight[i] = nodes_.get(i).getCard();
        return weight;
    }
    // TODO adding unit test script.


}
