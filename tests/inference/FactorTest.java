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

    @Test
    public void multiply() throws Exception {

        final int N = 9;
        Var v1 = new Var(1, 3);
        Var v2 = new Var(2, 3);
        Factor x = new Factor(v1);
        x.set(0, 2.0);
        x.set(1, 0.0);
        x.set(2, -1.0);
        Factor y = new Factor(v2);
        y.set(0, 0.5);
        y.set(1, -1.0);
        y.set(2, 0.0);
        Factor r;

        Factor z = new Factor(new VarSet(v1, v2));
        z.set(0, 1.0);
        z.set(1, 0.0);
        z.set(2, -0.5);
        z.set(3, -2.0);
        z.set(4, 0.0);
        z.set(5, 1.0);
        z.set(6, 0.0);
        z.set(7, 0.0);
        z.set(8, 0.0);
        r = Factor.multiply(x, y);
        assertArrayEquals(z.p(), r.p(), tol);
    }
}