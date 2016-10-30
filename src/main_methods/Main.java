package main_methods;

import bayesian_network.Bayesian_network;
import graph.Graph;
import io.HuginNetFile;
import triangulation.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Main starts.");

        //  String filename = "bnr/bnlearn/alarm.net";
        String filename = args[0];

        Bayesian_network bn = HuginNetFile.read(filename);

        Graph g = bn.getMoralGraph();
        int[] weights = bn.getWeights();

       // Graph GreedyFillin = new GreedyFillin().run(g, weights);
        System.out.println(g.V()+", "+ g.E()+", "+ g.density());

        System.out.println("DCM");
        Graph DFS_DCM_OandV = new TriangulationByDFS_DCM_OandV().run(g, weights);
        Graph DFS_DCM_PGM2012 = new TriangulationByDFS_DCM_PGM2012().run(g, weights);
        Graph DFS_DCM_AMBN2015 = new TriangulationByDFS_DCM_AMBN2015().run(g, weights);

        System.out.println("PIVOT");
        DFS_DCM_OandV = new TriangulationByDFS_DCM_OandV().run(g, weights);
        Graph DFS_DCM_OandV_PIVOTCLIQUE = new TriangulationByDFS_DCM_OandV_PIVOTCLIQUE().run(g, weights);

        System.out.println("BOTH_DCM_PIVOT");
        DFS_DCM_OandV = new TriangulationByDFS_DCM_OandV().run(g, weights);
        Graph DFS_DCM_AMBN2015_PIVOTCLIQUE = new TriangulationByDFS_DCM_AMBN2015_PIVOTCLIQUE().run(g, weights);

        System.out.println("Main ends.");
    }
}