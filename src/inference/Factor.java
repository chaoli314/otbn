package inference;

import java.util.Arrays;
import java.util.Random;

import static inference.Index.indexFor;
import static inference.VarSet.*;
import static util.BigIntegerUtil.toIntExact;

/*** Created by chao li on 10/23/16.*/
public class Factor {

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

    //  Getters and Setters ##################################
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

    // ~ Information Theory ~

    /**
     * Returns logarithm of x, or 0 if x == 0.
     */
    private static double log0(double x) {
        return (0 != x) ? java.lang.Math.log(x) : 0.0;
    }

    /**
     * Returns the Shannon entropy of this factor
     */
    public double entropy() {
        double entropy = 0;
        for (double p : _p)
            entropy = p * log0(p);
        return -entropy;
    }




    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review


    /**
     * using chain rule: H(XY) = H(X)+H(Y|X)
     */
    public double conditional_entropy(Var x) {

        VarSet X = new VarSet();
        X.add(x);

        double H_XY = this.entropy();
        double H_X = this.marginal(X).entropy();
        return H_XY - H_X;
    }

    /**
     * using chain rule: H(XY) = H(X)+H(Y|X) => (result = H_XY - H_X)
     */
    public double conditional_entropy(VarSet X) {
        double H_XY = this.entropy();
        double H_X = this.marginal(X).entropy();
        return H_XY - H_X;
    }

    /**
     * I(X;Y) = H(Y) + H(X) - H(XY)
     */
    public double MutualInfo(Var x, Var y) {

        VarSet X = new VarSet();
        X.add(x);

        VarSet Y = new VarSet();
        Y.add(y);

        VarSet XY = new VarSet();
        XY.add(x);
        XY.add(y);

        double H_X = this.marginal(X).entropy();
        double H_Y = this.marginal(Y).entropy();
        double H_XY = this.marginal(XY).entropy();

        return H_X + H_Y - H_XY;
    }

    /**
     * I(X;Y) = H(Y) + H(X) - H(XY)
     */
    public double MutualInfo(VarSet X, VarSet Y) {

        VarSet XY = new VarSet();
        XY.addAll(X);
        XY.addAll(Y);

        double H_X = this.marginal(X).entropy();
        double H_Y = this.marginal(Y).entropy();
        double H_XY = this.marginal(XY).entropy();

        return H_X + H_Y - H_XY;
    }

    public Factor reorderVars(VarSet res_vars) {
        int[] convertLinearIndex = indexFor(this.scope_, res_vars);
        double[] res_p = new double[_p.length];
        for (int i = 0; i < res_p.length; ++i) {
            res_p[i] = this._p[convertLinearIndex[i]];
        }
        return new Factor(res_vars, res_p);
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
        for (double p: this._p){
            if (p < 0){
                hasNegatives = true;
                break;
            }
        }
        return hasNegatives;
    }





    // ~ 指数运算 *********************************************************************

    /**
     * Applies logarithm pointwise;uses log(0)==0;
     */
    public Factor takeLog() {
        for (int i = 0; i < _p.length; ++i)
            _p[i] = log0(_p[i]);
        return this;
    }

    /**
     * Returns pointwise logarithm
     */
    public Factor log() {
        Factor x = new Factor(this);
        x.takeLog();
        return x;
    }

    /**
     * Returns pointwise exponent
     */
    public Factor exp() {
        Factor x = new Factor(this);
        x.takeExp();
        return x;
    }

    /**
     * Applies exponent pointwise
     */
    public Factor takeExp() {
        for (int i = 0; i < _p.length; ++i)
            _p[i] = Math.exp(_p[i]);
        return this;
    }

    // + - * / ; 加减乘除运算 *********************************************************************

    /**
     * Returns point-wise inverse ( uses 1/0==0; not 1/0==Infinity.)
     */
    public Factor inverse() {
        Factor x = new Factor(this);
        for (int i = 0; i < x._p.length; ++i)
            x._p[i] = (x._p[i] != 0 ? (1 / (x._p[i])) : 0);
        return x;
    }

    /**
     * Returns marginal on vars, obtained by summing out all variables except those in vars; 周辺化
     */
    public Factor marginal(VarSet vars) {
        VarSet res_vars = set_intersection(this.scope_, vars); // もし、vars有多余变量，无视他
        double[] res_p = new double[toIntExact(res_vars.nrStates())];
        int[] i_res = indexFor(res_vars, this.scope_);
        for (int i = 0; i < this._p.length; ++i)
            res_p[i_res[i]] += this._p[i];
        return new Factor(res_vars, res_p);
    }

    /**
     * Returns max-marginal on \a vars, obtained by maximizing all variables except those in \a vars, and normalizing the result if \a normed == \c
     * true
     */
    public Factor maxMarginal(VarSet vars) {
        VarSet res_vs = set_intersection(this.scope_, vars); // もし、vars有多余变量，无视他
        double[] res_p = new double[toIntExact(res_vs.nrStates())];
        int[] i_res = indexFor(res_vs, this.scope_);
        for (int i = 0; i < this._p.length; ++i)
            if (_p[i] > res_p[i_res[i]]) res_p[i_res[i]] = _p[i];
        return new Factor(res_vs, res_p);
    }

    /**
     * reuse marginal
     */
    public Factor summing_out(VarSet vars) {
        VarSet res_vars = set_difference(this.scope_, vars);
        return this.marginal(res_vars);
    }

    /**
     * Caution : mutate this factor
     */
    public Factor product(Factor that) {
        Factor other = product(this, that);
        this.scope_ = other.scope_;
        this._p = other._p;
        return this;
    }

    public static Factor product(Factor A, Factor B) {
        VarSet C_vs = set_union(A.scope_, B.scope_);
        int[] i_A_for_C = indexFor(A.scope_, C_vs);
        int[] i_B_for_C = indexFor(B.scope_, C_vs);
        int C_tableSize = toIntExact(C_vs.nrStates());
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            C_p[i_C] = A._p[i_A_for_C[i_C]] * B._p[i_B_for_C[i_C]];
        }
        return new Factor(C_vs, C_p);
    }

    public static Factor sum(Factor A, Factor B) {
        VarSet C_vs = set_union(A.scope_, B.scope_);
        int[] i_A_for_C = indexFor(A.scope_, C_vs);
        int[] i_B_for_C = indexFor(B.scope_, C_vs);
        int C_tableSize = toIntExact(C_vs.nrStates());
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            C_p[i_C] = A._p[i_A_for_C[i_C]] + B._p[i_B_for_C[i_C]];
        }
        return new Factor(C_vs, C_p);
    }

    public static Factor difference(Factor A, Factor B) {
        VarSet C_vs = set_union(A.scope_, B.scope_);
        int[] i_A_for_C = indexFor(A.scope_, C_vs);
        int[] i_B_for_C = indexFor(B.scope_, C_vs);
        int C_tableSize = toIntExact(C_vs.nrStates());
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            C_p[i_C] = A._p[i_A_for_C[i_C]] - B._p[i_B_for_C[i_C]];
        }
        return new Factor(C_vs, C_p);
    }

    /**
     * Specilized the divide by zero
     */
    public static Factor quotient(Factor A, Factor B) {
        VarSet C_vs = set_union(A.scope_, B.scope_);
        int[] i_A_for_C = indexFor(A.scope_, C_vs);
        int[] i_B_for_C = indexFor(B.scope_, C_vs);
        int C_tableSize = toIntExact(C_vs.nrStates());
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            if (B._p[i_B_for_C[i_C]] != 0) C_p[i_C] = A._p[i_A_for_C[i_C]] / B._p[i_B_for_C[i_C]];
        }
        return new Factor(C_vs, C_p);
    }



    // ~ methods *********************************************************************

    /**
     * Normalizes this factor
     */
    public double normalize() {
        double Z = sum(); // apache math lib
        for (int i = 0; i < _p.length; ++i) {
            _p[i] /= Z;
        }
        return Z;
    }

    /**
     * Returns normalized copy of this
     */
    public Factor normalized() {
        Factor x = new Factor(this);
        x.normalize();
        return x;
    }

    /**
     * Draws all values i.i.d. from a uniform distribution on [0,1)
     */
    public Factor randomize() {
        Random rand = new Random();
        for (int i = 0; i < _p.length; ++i)
            _p[i] = rand.nextDouble();
        return this;
    }

    /**
     * Sets all values to 1/n, n is the table size
     */
    public Factor setUniform() {
        Arrays.fill(_p, 1 / _p.length);
        return this;
    }

    /**
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(_p);
        result = prime * result + ((scope_ == null) ? 0 : scope_.hashCode());
        return result;
    }

    /**
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Factor other = (Factor) obj;
        if (!Arrays.equals(_p, other._p)) return false;
        if (scope_ == null) {
            if (other.scope_ != null) return false;
        } else if (!scope_.equals(other.scope_)) return false;
        return true;
    }

    /**
     * Writes a factor to an output stream
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("variables: " + scope_ + "\n");
        for (int linearState = 0; linearState < toIntExact(scope_.nrStates()); linearState++) {
            s.append(("" + String.format("%2d", linearState) + "|getCard" + Arrays.toString(Index.calcState(scope_, linearState))) + "|prob: "
                    + String.format("%.5f", _p[linearState]) + "\n");
        }
        return s.toString();
        // return "Factor [scope_=" + scope_ + ", _p=" + Arrays.toString(_p) + "]";
    }


    // ~ 実装していないメソッド *********************************************************************

    /**
     * not yet
     */
    void slice() {
    }


    /**
     * Copy constructor
     */
    public Factor(Factor other) {
        scope_ = new VarSet(other.scope_);
        _p = new double[other._p.length];
        System.arraycopy(other._p, 0, this._p, 0, other._p.length);
    }
}