package inference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by chaoli on 3/4/17.
 */
public class FactorTest {

    private final double tol = 1e-8;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void marginal() throws Exception {

        Var v1 = new Var(1, 2);
        Var v2 = new Var(2, 3);
        VarSet vars = new VarSet();
        vars.add(v1);
        vars.add(v2);
        Factor x = new Factor(vars);
        x.randomize();

        // test marginal
        Factor y = x.marginal(new VarSet(v1));
        assertEquals(y.vars(), new VarSet(v1));
        assertNotEquals(y.vars(), new VarSet(v2));
        assertEquals(y.get(0), x.get(0) + x.get(2) + x.get(4), tol);
        assertEquals(y.get(1), x.get(1) + x.get(3) + x.get(5), tol);

        y = x.marginal(new VarSet(v2));
        assertNotEquals(y.vars(), new VarSet(v1));
        assertEquals(y.vars(), new VarSet(v2));
        assertEquals(y.get(0), x.get(0) + x.get(1), tol);
        assertEquals(y.get(1), x.get(2) + x.get(3), tol);
        assertEquals(y.get(2), x.get(4) + x.get(5), tol);

        y = x.marginal(new VarSet());
        assertEquals(y.vars(), new VarSet());
        assertEquals(y.get(0), x.get(0) + x.get(1) + x.get(2) + x.get(3) + x.get(4) + x.get(5), tol);

        y = x.marginal(new VarSet(v1, v2));
        assertEquals(y.vars(), x.vars());
        assertArrayEquals(y.p(), x.p(), tol);

        // test summing_out
        y = x.summing_out(new VarSet(v1));
        assertNotEquals(y.vars(), new VarSet(v1));
        assertEquals(y.vars(), new VarSet(v2));
        assertEquals(y.get(0), x.get(0) + x.get(1), tol);
        assertEquals(y.get(1), x.get(2) + x.get(3), tol);
        assertEquals(y.get(2), x.get(4) + x.get(5), tol);
    }
}