package inference;

import java.util.Arrays;
import java.util.Random;

import static inference.Index.indexFor;
import static inference.VarSet.*;
import static util.BigIntegerUtil.toIntExact;

/*** Created by chao li on 10/23/16.*/
public class Factor {

    private VarSet scope_;
    private double[] data_;

    //  Constructors    ################################
    public Factor(VarSet vars, double[] data) {
        scope_ = vars;
        data_ = data;
    }

    public Factor(VarSet vars) {
        scope_ = vars;
        data_ = new double[toIntExact(scope_.nrStates())];
    }

    public void fill(double x) {
        Arrays.fill(data_, x);
    }

    /**
     * Copy constructor
     */
    public Factor(Factor other) {
        scope_ = new VarSet(other.scope_);
        data_ = new double[other.data_.length];
        System.arraycopy(other.data_, 0, this.data_, 0, other.data_.length);
    }

    //  Getters and Setters ##################################
    public VarSet getVars() {
        return scope_;
    }

    public double getDataItem(int index) {
        return data_[index];
    }

    public void setDataItem(int index, double value) {
        data_[index] = value;
    }

    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review
    ////////////// under review////////////// under review////////////// under review

    public Factor reorderVars(VarSet res_vars) {
        int[] convertLinearIndex = indexFor(this.scope_, res_vars);
        double[] res_p = new double[data_.length];
        for (int i = 0; i < res_p.length; ++i) {
            res_p[i] = this.data_[convertLinearIndex[i]];
        }
        return new Factor(res_vars, res_p);
    }


    // ~ Getters and Setters *********************************************************************

    /**
     * Returns reference to value vector
     */
    public double[] p() {
        return data_;
    }


    // ~ Some simple statistics *********************************************************************

    /**
     * Returns maximum of all values
     */
    public double max() {
        return org.apache.commons.math3.stat.StatUtils.max(this.data_);
    }

    /**
     * Returns minimum of all values
     */
    public double min() {
        return org.apache.commons.math3.stat.StatUtils.min(this.data_);
    }

    /**
     * Returns sum of all values
     */
    public double sum() {
        return org.apache.commons.math3.stat.StatUtils.sum(this.data_);
    }

    /**
     * Returns true if one or more entries are NaN
     */
    public boolean hasNaNs() {
        boolean foundnan = false;
        for (double p : data_) {
            if (Double.isNaN(p)) {
                foundnan = true;
                break;
            }
        }
        return foundnan;
    }

    /**
     * Returns true if one or more values are negative
     */
    public boolean hasNegatives() {
        boolean hasNegatives = false;
        for (double p : this.data_) {
            if (0 > p) {
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
        for (int i = 0; i < data_.length; ++i)
            data_[i] = log0(data_[i]);
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
        for (int i = 0; i < data_.length; ++i)
            data_[i] = Math.exp(data_[i]);
        return this;
    }

    // + - * / ; 加减乘除运算 *********************************************************************

    /**
     * Returns point-wise inverse ( uses 1/0==0; not 1/0==Infinity.)
     */
    public Factor inverse() {
        Factor x = new Factor(this);
        for (int i = 0; i < x.data_.length; ++i)
            x.data_[i] = (x.data_[i] != 0 ? (1 / (x.data_[i])) : 0);
        return x;
    }

    /**
     * Returns marginal on vars, obtained by summing out all variables except those in vars; 周辺化
     */
    public Factor marginal(VarSet vars) {
        VarSet res_vars = set_intersection(this.scope_, vars); // もし、vars有多余变量，无视他
        double[] res_p = new double[toIntExact(res_vars.nrStates())];
        int[] i_res = indexFor(res_vars, this.scope_);
        for (int i = 0; i < this.data_.length; ++i)
            res_p[i_res[i]] += this.data_[i];
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
        for (int i = 0; i < this.data_.length; ++i)
            if (data_[i] > res_p[i_res[i]]) res_p[i_res[i]] = data_[i];
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
        this.data_ = other.data_;
        return this;
    }

    public static Factor product(Factor A, Factor B) {
        VarSet C_vs = set_union(A.scope_, B.scope_);
        int[] i_A_for_C = indexFor(A.scope_, C_vs);
        int[] i_B_for_C = indexFor(B.scope_, C_vs);
        int C_tableSize = toIntExact(C_vs.nrStates());
        double[] C_p = new double[C_tableSize];
        for (int i_C = 0; i_C < C_tableSize; i_C++) {
            C_p[i_C] = A.data_[i_A_for_C[i_C]] * B.data_[i_B_for_C[i_C]];
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
            C_p[i_C] = A.data_[i_A_for_C[i_C]] + B.data_[i_B_for_C[i_C]];
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
            C_p[i_C] = A.data_[i_A_for_C[i_C]] - B.data_[i_B_for_C[i_C]];
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
            if (B.data_[i_B_for_C[i_C]] != 0) C_p[i_C] = A.data_[i_A_for_C[i_C]] / B.data_[i_B_for_C[i_C]];
        }
        return new Factor(C_vs, C_p);
    }

    // ~ Entropy 情報理論など *********************************************************************

    /**
     * Returns logarithm of x, or 0 if x == 0;log(0) or log(x),x<0数学上无定义; make sure x is not negative
     */
    private static double log0(double x) {
        return (x != 0) ? Math.log(x) : 0;
    }

    /**
     * 条件：正規化してから、ENTROPYを計算すること；Returns the Shannon entropy of this factor
     */
    public double entropy() {
        double result = 0;
        for (double p : data_)
            result -= p * log0(p);
        return result;
    }

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

    // ~ methods *********************************************************************

    /**
     * Normalizes this factor
     */
    public double normalize() {
        double Z = sum(); // apache math lib
        for (int i = 0; i < data_.length; ++i) {
            data_[i] /= Z;
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
        for (int i = 0; i < data_.length; ++i)
            data_[i] = rand.nextDouble();
        return this;
    }

    /**
     * Sets all values to 1/n, n is the table size
     */
    public Factor setUniform() {
        Arrays.fill(data_, 1 / data_.length);
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
        result = prime * result + Arrays.hashCode(data_);
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
        if (!Arrays.equals(data_, other.data_)) return false;
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
                    + String.format("%.5f", data_[linearState]) + "\n");
        }
        return s.toString();
        // return "Factor [scope_=" + scope_ + ", data_=" + Arrays.toString(data_) + "]";
    }


    // ~ 実装していないメソッド *********************************************************************

    /**
     * not yet
     */
    void slice() {
    }

    public int getSize() {
        return data_.length;
    }
}