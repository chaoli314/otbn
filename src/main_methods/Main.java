package main_methods;


import bayesian_network.BayesianNetwork;
import graph.Graph;
import io.HuginNetFormat;

public class Main {

    public static void main(String[] args) {

        System.out.println("Main starts.");
        String filename = "bnr/bnlearn/mildew.net";
        BayesianNetwork bn = HuginNetFormat.read(filename);
        Graph g = bn.getMoralGraph();
        int[] weights = bn.getWeights();
        //System.out.print(g);
        Graph h1 = new triangulation.TriangulationByDFS_DCM_OandV().run(g, weights);
        //System.out.print(h1);
        Graph h2 = new triangulation.TriangulationByDFS_DCM_AMBN2015_PIVOTCLIQUE().run(g, weights);
        //System.out.print(h2);

    }
}