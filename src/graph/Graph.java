package graph;

import java.util.*;

/**
 * Created by chaoli on 10/20/16.
 */
public class Graph {

    private BitSet[] matrix_;

    /**
     * Initializes an empty graph with {@code V} vertices and 0 edges.
     *
     * @param V number of vertices
     */
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        matrix_ = new BitSet[V];
        for (int v = 0; v < V; v++)
            matrix_[v] = new BitSet();
    }

    /**
     * Uniformly random connected graph
     *
     * @param V
     * @param E
     */
    public Graph(int V, int E) {
        this(V);
        if (E > (long) V * (V - 1) / 2) throw new IllegalArgumentException("Too many edges");
        if (E < (V - 1) /* Bayesian network is a connected Graph */)
            throw new IllegalArgumentException("Too few edges");
        this.generatesRandomSpanningTree();
        this.addEdge_num_edges(E);
    }

    public Graph(int V, double density) {
        this(V);
        if (density > 1.0) throw new IllegalArgumentException("Too many edges");
        if (density < (2.0 / (double) V) /* Bayesian network is a connected Graph */)
            throw new IllegalArgumentException("Too few edges");
        this.generatesRandomSpanningTree();
        this.addEdge_density(density);
    }

    /**
     * copy constructor
     */
    public Graph(Graph G) {
        matrix_ = new BitSet[G.V()];
        for (int v = 0; v < G.V(); v++) {
            this.matrix_[v] = (BitSet) G.matrix_[v].clone();
        }
    }

    public void generatesRandomSpanningTree() {
        BitSet S = new BitSet(V());    // Initially store all nodes in S
        S.set(0, V());
        BitSet T = new BitSet(V());    // marked
        Random rand = new Random();

        int current_node = rand.nextInt(V());
        S.clear(current_node);
        T.set(current_node);

        while (!S.isEmpty()) {
            int neighbor_node = rand.nextInt(V());
            if (!T.get(neighbor_node)) {
                this.addEdge(current_node, neighbor_node);
                S.clear(neighbor_node);
                T.set(neighbor_node);
            }
            current_node = neighbor_node;
        }
    }

    public int V() {
        return matrix_.length;
    }

    public int E() {
        int count = 0;
        for (BitSet bs : matrix_) count += bs.cardinality();
        return count / 2;
    }

    public double density() {
        return (2 * E()) * 100 / (V() * (V() - 1)) / 100.0;
    }

    public void addEdge(int v, int w) {
        matrix_[v].set(w);
        matrix_[w].set(v);
    }

    /**
     * Add a random edge
     */
    public void addEdge() {
        int v = 0, w = 0;
        Random rand = new Random();

        do {
            v = rand.nextInt(V());
            w = rand.nextInt(V());
        } while ((v == w) ||
                this.containsEdge(v, w));
        addEdge(v, w);
    }

    public void addEdge_density(double density) {
        while (this.density() < density) this.addEdge();
    }

    public void addEdge_num_edges(int E) {
        while (this.E() < E) this.addEdge();
    }

    public void removeEdge(int v, int w) {
        matrix_[v].clear(w);
        matrix_[w].clear(v);
    }

    public boolean containsEdge(int v, int w) {
        return matrix_[v].get(w);
    }

    public BitSet getNeighbors(int v) {
        return matrix_[v];
    }


    /**
     * @param v
     * @return
     * @see <a href="http://algs4.cs.princeton.edu/41graph/AdjMatrixGraph.java.html">
     * http://algs4.cs.princeton.edu/41graph/AdjMatrixGraph.java.html</a>
     */
    public Iterable<Integer> adj(int v) {
        return new AdjIterator(v);
    }

    /**
     * support iteration over graph vertices
     *
     * @see <a href="http://algs4.cs.princeton.edu/41graph/AdjMatrixGraph.java.html">
     * http://algs4.cs.princeton.edu/41graph/AdjMatrixGraph.java.html</a>
     */
    private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {
        private int v;
        private int w = 0;

        AdjIterator(int v) {
            this.v = v;
        }

        public Iterator<Integer> iterator() {
            return this;
        }

        public boolean hasNext() {
            while (w < V()) {
                if (containsEdge(v, w)) return true;
                w++;
            }
            return false;
        }

        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return w++;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public int degree(int v) {
        return getNeighbors(v).cardinality();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return Arrays.equals(this.matrix_, graph.matrix_);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(matrix_);
    }

    /**
     * string representation of Graph - takes quadratic time
     *
     * @return
     * @see <a href="http://algs4.cs.princeton.edu/41graph/AdjMatrixGraph.java.html">
     * http://algs4.cs.princeton.edu/41graph/AdjMatrixGraph.java.html</a>
     */
    @Override
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append("V = " + V() + ", E = " + E() + NEWLINE);
        for (int v = 0; v < V(); v++) {
            s.append(v + ": ");
            for (int w : adj(v)) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}