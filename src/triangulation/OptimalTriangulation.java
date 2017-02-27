package triangulation;


import graph.Graph;
import util.Counter;

/**
 * Created by chaoli on 10/27/16.
 */
public abstract class OptimalTriangulation {

    protected util.Counter nodeCounter_;
    protected util.Counter cliqueCounter_;
    protected long time_for_DCM_;
    protected long time_for_total_;

    public OptimalTriangulation() {
        this.nodeCounter_ = new util.Counter("nodes");
        this.cliqueCounter_ = new util.Counter("cliques");
        this.time_for_DCM_ = 0L;
        this.time_for_total_ = 0L;
    }

    public abstract Graph run(final Graph originalGraph, int... weights);

    public abstract void printStatistics();
}