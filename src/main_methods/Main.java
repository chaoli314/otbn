package main_methods;


import bayesian_network.BayesianNetwork;
import graph.Graph;
import io.HuginNetFormat;

public class Main {

    public static void main(String[] args) {

        System.out.println("Main starts.");
        String filename = "bnr/softbank/hc.net";
        BayesianNetwork bn = HuginNetFormat.read(filename);
        Graph g = bn.getMoralGraph();
        int[] weights = bn.getWeights();
        //System.out.print(g);
        //Graph h = new triangulation.GreedyFillin().run(g, weights);
        //System.out.print(h);




   /*
        String filename = args[0];

        BayesianNetwork bn = HuginNetFormat.read(filename);

        Graph g = bn.getMoralGraph();
        int[] weights = bn.getWeights();

        Graph GreedyFillin = new GreedyFillin().run(g, weights);
        System.out.println(g.V()+", "+ g.E()+", "+ g.density());

        System.out.println("Main ends.");
*/
    }
}