package inference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 3/7/17.
 */
public class IndexTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void indexFor() throws Exception {

        final int nrVars = 2;
        List<Var> vars = new ArrayList<>();
        for (int i = 0; i < nrVars; ++i)
            vars.add(new Var(i, i + 2));

        Random rnd = new Random();


        for (int repeat = 0; repeat < 10; ++repeat) {
            VarSet indexVars = new VarSet();
            VarSet forVars = new VarSet();

            for (int i = 0; i < nrVars; ++i) {
                if (rnd.nextInt(2) == 0) {
                    indexVars.add(vars.get(i));
                    forVars.add(vars.get(i));
                } else if (rnd.nextInt(2) == 0) {
                    forVars = VarSet.set_union(forVars, new VarSet(vars.get(i)));
                }
            }
            //
            int[] ind = Index.IndexFor(indexVars, forVars);
            for (int iter = 0; iter < forVars.nrStates().intValueExact(); ++iter) {
                assertEquals(Index.calcLinearState(indexVars, Index.calcState(forVars, iter)), ind[iter]);
            }
        }
    }

    @Test
    public void calcState() throws Exception {

    }

    @Test
    public void calcLinearState() throws Exception {

    }

}