package bayesian_network;

import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaoli on 10/20/16.
 */
public class Bayesian_network {

    private List<Node> nodes_;
    private Map<String, Integer> name_to_index_;

    public Bayesian_network() {
        this.nodes_ = new ArrayList<>();
        this.name_to_index_ = new HashMap<>();
    }

    public Node getNodeByName(String nodeName) {
        int nodeIndex = name_to_index_.get(nodeName);
        return nodes_.get(nodeIndex);
    }

    public Node getNodeByIndex(int nodeIndex) {
        return nodes_.get(nodeIndex);
    }

    public int getNodeIndex(String nodeName) {
        return name_to_index_.get(nodeName);
    }

    public String getNodeName(int nodeIndex) {
        return nodes_.get(nodeIndex).getName();
    }

    public Node addNode(String nodeName) {
        if (name_to_index_.containsKey(nodeName)) {
            throw new RuntimeException("duplicate node names!");
        }
        int nodeIndex = name_to_index_.size();
        name_to_index_.put(nodeName, nodeIndex);
        Node node = new Node(this, nodeName, nodeIndex);
        nodes_.add(node);
        return node;
    }

    public int getNumberOfNodes() {
        return nodes_.size();
    }

    public int size() {
        return nodes_.size();
    }

    public Graph getMoralGraph() {

        int V = nodes_.size();
        Graph graph = new Graph(V);

        for (int childNodeIndex = 0; childNodeIndex < this.size(); ++childNodeIndex) {

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
        for (int i = 0; i < N; ++i) weight[i] = nodes_.get(i).getNumberOfStates();
        return weight;
    }
}
