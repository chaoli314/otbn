package inference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by chaoli on 10/23/16.
 */
public class VarSetTest {


    VarSet vsL_;
    VarSet vsR_;
    VarSet result_;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        vsL_ = null;
        vsR_ = null;
        result_ = null;
    }

    @Test
    public void nrStates() throws Exception {
        Var v1 = new Var(1, 3);
        Var v2 = new Var(2, 5);
        Var v3 = new Var(3, 7);
        Var v4 = new Var(4, 3);
        Var v5 = new Var(5, 2);

        result_ = new VarSet(Arrays.asList(v1, v2, v3, v4, v5));

        assertEquals(BigInteger.valueOf(630), result_.nrStates());
    }

    @Test
    public void set_difference() throws Exception {

        Var v5 = new Var(5, 2);
        Var v6 = new Var(6, 7);
        Var v7 = new Var(7, 2);
        Var v8 = new Var(8, 3);
        Var v9 = new Var(9, 5);
        Var v10 = new Var(10, 7);

        // ~ http://en.cppreference.com/w/cpp/algorithm/set_intersection
        // std::vector<int> v1{5,6,7,8};
        // std::vector<int> v2{5,  7,  9,10};
        vsL_ = new VarSet(Arrays.asList(v5, v6, v7, v8));
        vsR_ = new VarSet(Arrays.asList(v5, v7, v9, v10));
        // 6 8
        result_ = VarSet.set_difference(vsL_, vsR_);
        assertEquals(v6, result_.get(0));
        assertEquals(v8, result_.get(1));
        assertEquals(2, result_.size());
    }

    @Test
    public void set_intersection() throws Exception {

        Var v5 = new Var(5, 2);
        Var v6 = new Var(6, 7);
        Var v7 = new Var(7, 2);
        Var v8 = new Var(8, 3);
        Var v9 = new Var(9, 5);
        Var v10 = new Var(10, 7);

        // ~ http://en.cppreference.com/w/cpp/algorithm/set_intersection
        // std::vector<int> v1{5,6,7,8};
        // std::vector<int> v2{5,  7,  9,10};
        vsL_ = new VarSet(Arrays.asList(v5, v6, v7, v8));
        vsR_ = new VarSet(Arrays.asList(v5, v7, v9, v10));
        // 5 7
        result_ = VarSet.set_intersection(vsL_, vsR_);
        assertEquals(v5, result_.get(0));
        assertEquals(v7, result_.get(1));
        assertEquals(2, result_.size());
    }

    @Test
    public void set_union() throws Exception {

        Var v1 = new Var(1, 3);
        Var v2 = new Var(2, 5);
        Var v3 = new Var(3, 7);
        Var v4 = new Var(4, 3);
        Var v5 = new Var(5, 2);
        Var v6 = new Var(6, 7);
        Var v7 = new Var(7, 2);

        // ~ http://en.cppreference.com/w/cpp/algorithm/set_union
        // std::vector<int> v1 = {1, 2, 3, 4, 5};
        // std::vector<int> v2 = {      3, 4, 5, 6, 7};
         vsL_ = new VarSet(Arrays.asList(v1, v2, v3, v4, v5));
         vsR_ = new VarSet(Arrays.asList(v3, v4, v5, v6, v7));
        // 1 2 3 4 5 6 7
         result_ = VarSet.set_union(vsL_, vsR_);

        assertEquals(v1, result_.get(0));
        assertEquals(v2, result_.get(1));
        assertEquals(v3, result_.get(2));
        assertEquals(v4, result_.get(3));
        assertEquals(v5, result_.get(4));
        assertEquals(v6, result_.get(5));
        assertEquals(v7, result_.get(6));

        assertEquals(v1, result_.get(0));
        assertNotEquals(v1, result_.get(1));
        assertNotEquals(v1, result_.get(2));
        assertNotEquals(v1, result_.get(3));
        assertNotEquals(v1, result_.get(4));
        assertNotEquals(v1, result_.get(5));
        assertNotEquals(v1, result_.get(6));
        assertEquals(7, result_.size());
    }

    @Test
    public void set_symmetric_difference() throws Exception {

    }
}