package triangulation;


import edu.princeton.cs.algs4.Counter;
import graph.Graph;

/**
 * Created by chaoli on 10/27/16.
 */
public abstract class OptimalTriangulation {

    protected Counter nodeCounter_;
    protected Counter cliqueCounter_;
    protected long time_for_DCM_;
    protected long time_for_total_;

    public OptimalTriangulation() {
        this.nodeCounter_ = new Counter("nodes");
        this.cliqueCounter_ = new Counter("cliques");
        this.time_for_DCM_ = 0L;
        this.time_for_total_ = 0L;
    }

    public abstract Graph run(final Graph originalGraph, int... weights);

    public abstract void printStatistics();
}