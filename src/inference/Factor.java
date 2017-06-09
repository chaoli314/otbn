package inference;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static inference.Index.IndexFor;
import static inference.VarSet.*;

/*** Created by chao li on 10/23/16.*/
public class Factor {

    /**
     * Returns logarithm of x, or 0 if x == 0.
     */
    private static final double log0(double x) {
        return (0.0 == x) ? 0.0 : java.lang.Math.log(x);
    }

    private static final double divides0(double x, double y) {
        return (0.0 == y ? 0.0 : (x / y));
    }

    private static final double inverse(double x) {
        return (0.0 == x) ? 0.0 : 1.0 / x;
    }

    private VarSet scope_;
    private double[] _p;

    // ~ Constructors ~
    public Factor(Var v) {
        VarSet vars = new VarSet();
        vars.add(v);
        this.scope_ = vars;
        _p = new double[scope_.nrStates().intValueExact()];
    }

    public Factor(VarSet vars) {
        this.scope_ = vars;
        _p = new double[scope_.nrStates().intValueExact()];
    }

    public Factor(VarSet vars, double[] p) {
        scope_ = vars;
        _p = p;
    }

    /**
     * Copy constructor
     */
    public Factor(Factor other) {
        scope_ = new VarSet(other.scope_);
        _p = new double[other._p.length];
        System.arraycopy(other._p, 0, this._p, 0, other._p.length);
    }

    // ~ Getters and Setters ~
    public VarSet vars() {
        return scope_;
    }

    public double[] p() {
        return _p;
    }

    public void set(int index, double value) {
        _p[index] = value;
    }

    public double get(int index) {
        return _p[index];
    }

    public void fill(double x) {
        Arrays.fill(_p, x);
    }

    public int nrStates() {
        return _p.length;
    }

    // ~ Statistics ~

    /**
     * Returns maximum of all values.
     */
    public double max() {
        return org.apache.commons.math3.stat.StatUtils.max(this._p);
    }

    /**
     * Returns minimum of all values.
     */
    public double min() {
        return org.apache.commons.math3.stat.StatUtils.min(this._p);
    }

    /**
     * Returns sum of all values.
     */
    public double sum() {
        return org.apache.commons.math3.stat.StatUtils.sum(this._p);
    }

    /**
     * Returns true if one or more values are NaN.
     */
    public boolean hasNaNs() {
        boolean hasNaNs = false;
        for (double p : _p) {
            if (Double.isNaN(p)) {
                hasNaNs = true;
                break;
            }
        }
        return hasNaNs;
    }

    /**
     * Returns true if one or more values are negative.
     */
    public boolean hasNegatives() {
        boolean hasNegatives = false;
        for (double p : this._p) {
            if (p < 0) {
                hasNegatives = true;
                break;
            }
        }
        return hasNegatives;
    }

    // ~ Marginalization ~

    /**
     * Returns marginal on vars, obtained by summing out all variables except those in vars; 周辺化
     */
    public Factor marginal(VarSet vars) {
        VarSet res_vars = set_intersection(this.scope_, vars);
        double[] res_p = new double[res_vars.nrStates().intValueExact()];
        int[] i_res = IndexFor(res_vars, this.scope_);
        for (int i = 0; i < this._p.length; ++i) res_p[i_res[i]] += this._p[i];
        return new Factor(res_vars, res_p);
    }

    /**
     * reuse marginal
     */
    public Factor summing_out(VarSet vars) {
        VarSet res_vars = set_difference(this.scope_, vars);
        return this.marginal(res_vars);
    }

    /**
     * Returns max-marginal on \a vars, obtained by maximizing all variables except those in \a vars, and normalizing the result if \a normed == \c
     * true
     */
    public Factor maxMarginal(VarSet vars) {
        VarSet res_vars = set_intersection(this.scope_, vars);
        double[] res_p = new double[res_vars.nrStates().intValueExact()];
        int[] i_res = IndexFor(res_vars, this.scope_);
        for (int i = 0; i < this._p.length; ++i) if (_p[i] > res_p[i_res[i]]) res_p[i_res[i]] = _p[i];
        return new Factor(res_vars, res_p);
    }

    // ~ Unary transformations ~

    /**
     * Returns pointwise exponent.
     */
    public Factor exp() {
        Factor x = new Factor(this);
        x.takeExp();
        return x;
    }

    /**
     * Returns pointwise logarithm.
     */
    public Factor log0() {
        Factor x = new Factor(this);
        x.takeLog0();
        return x;
    }

    /**
     * Returns pointwise inverse.
     */
    public Factor inverse() {
        Factor x = new Factor(this);
        for (int i = 0; i < x._p.length; ++i)
            x._p[i] = inverse(x._p[i]);
        return x;
    }

    /**
     * Returns normalized copy of this factor.
     */
    public Factor normalized() {
        Factor x = new Factor(this); // Non-modifying operations
        x.normalize(); // Modifying operations
        return x;
    }

    // ~ Unary operations ~

    /**
     * Draws all values i.i.d. from a uniform distribution on [0,1)
     */
    public Factor randomize() {
        Random rnd = new Random();
        for (int i = 0; i < _p.length; ++i)
            _p[i] = rnd.nextDouble();
        return this;
    }

    /**
     * Sets all values to 1/n, where n is the number of states.
     */
    public Factor setUniform() {
        Arrays.fill(_p, 1.0 / (double) _p.length);
        return this;
    }

    /**
     * Applies exponent pointwise.
     */
    public Factor takeExp() {
        for (int i = 0; i < _p.length; ++i)
            _p[i] = Math.exp(_p[i]);
        return this;
    }

    /**
     * Applies logarithm pointwise.
     */
    public Factor takeLog0() {
        for (int i = 0; i < _p.length; ++i)
            _p[i] = log0(_p[i]);
        return this;
    }

    /**
     * Normalizes this factor.
     */
    public double normalize() {
        final double Z = sum();
        for (int i = 0; i < _p.length; ++i) _p[i] /= Z;
        return Z;
    }

    // ~ Information Theory ~

    /**
     * Returns the Shannon entropy of this factor
     */
    public double entropy() {
        double entropy = 0;
        for (double p : _p)
            entropy = p * log0(p);
        return -entropy;
    }

    /**
     * chain rule: H(Y|X) = H(X,Y) - H(X)
     */
    public double conditional_entropy(VarSet X) {
        double H_XY = this.entropy();
        double H_X = this.marginal(X).entropy();
        return H_XY - H_X;
    }

    /**
     * chain rule: H(Y|X) = H(X,Y) - H(X)
     */
    public double conditional_entropy(Var x) {
        return conditional_entropy(new VarSet(x));
    }

    /**
     * I(X;Y) = H(Y) + H(X) - H(XY)
     */
    public double MutualInfo(Var x, Var y) {
        return MutualInfo(new VarSet(x), new VarSet(y));
    }

    /**
     * I(X;Y) = H(X) + H(Y) - H(XY)
     */
    public double MutualInfo(VarSet X, VarSet Y) {
        VarSet XY = set_union(X, Y);
        double H_X = this.marginal(X).entropy();
        double H_Y = this.marginal(Y).entropy();
        double H_XY = this.marginal(XY).entropy();
        return H_X + H_Y - H_XY;
    }


    //TODO review following code.

    // + - * / ##########################################


    public static Factor multiply(Factor A, Factor B) {
        VarSet result_vs = set_union(A.scope_, B.scope_);
        int[] i_A = IndexFor(A.scope_, result_vs);
        int[] i_B = IndexFor(B.scope_, result_vs);
        final int C_tableSize = result_vs.nrStates().intValueExact();
        double[] result_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            result_p[i_C] = A._p[i_A[i_C]] * B._p[i_B[i_C]];
        }
        return new Factor(result_vs, result_p);
    }

    public static Factor sum(Factor A, Factor B) {
        VarSet result_vs = set_union(A.scope_, B.scope_);
        int[] i_A = IndexFor(A.scope_, result_vs);
        int[] i_B = IndexFor(B.scope_, result_vs);
        final int C_tableSize = result_vs.nrStates().intValueExact();
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            C_p[i_C] = A._p[i_A[i_C]] + B._p[i_B[i_C]];
        }
        return new Factor(result_vs, C_p);
    }

    public static Factor difference(Factor A, Factor B) {
        VarSet result_vs = set_union(A.scope_, B.scope_);
        int[] i_A = IndexFor(A.scope_, result_vs);
        int[] i_B = IndexFor(B.scope_, result_vs);
        final int C_tableSize = result_vs.nrStates().intValueExact();
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            C_p[i_C] = A._p[i_A[i_C]] - B._p[i_B[i_C]];
        }
        return new Factor(result_vs, C_p);
    }

    /**
     * Specilized the divide by zero
     */
    public static Factor quotient(Factor A, Factor B) {
        VarSet result_vs = set_union(A.scope_, B.scope_);
        int[] i_A = IndexFor(A.scope_, result_vs);
        int[] i_B = IndexFor(B.scope_, result_vs);
        final int C_tableSize = result_vs.nrStates().intValueExact();
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            if (B._p[i_B[i_C]] != 0) C_p[i_C] = A._p[i_A[i_C]] / B._p[i_B[i_C]];
        }
        return new Factor(result_vs, C_p);
    }

    // ~ methods *********************************************************************
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factor factor = (Factor) o;
        return Objects.equals(scope_, factor.scope_) &&
                Arrays.equals(_p, factor._p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope_, _p);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("variables: " + scope_ + "\n");
        for (int linearState = 0; linearState < scope_.nrStates().intValueExact(); ++linearState) {
            s.append(String.format("%3d", linearState) + "|getCard" + Index.calcState(scope_, linearState) + "|prob: "
                    + String.format("%.3f", _p[linearState]) + "\n");
        }
        return s.toString();
        // return "Factor [scope_=" + scope_ + ", _p=" + Arrays.toString(_p) + "]";
    }

    // ~ 実装していないメソッド *********************************************************************


    public Factor reorderVars(VarSet res_vars) {
        double[] res_p = new double[_p.length];
        //
        int[] convert_index = IndexFor(scope_, res_vars);
        for (int i = 0; i < res_p.length; ++i) {
            res_p[i] = _p[convert_index[i]];
        }
        return new Factor(res_vars, res_p);
    }

    /**
     * Caution : mutate this factor
     *//*
    public Factor multiply(Factor that) {
        Factor other = multiply(this, that);
        this.scope_ = other.scope_;
        this._p = other._p;
        return this;
    }*/
}