package inference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by chaoli on 10/23/16.
 */
public class varTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getIndex() throws Exception {
        Var v7 = new Var(7, 2);

        assertNotEquals(6, v7.getIndex());
        assertEquals(7, v7.getIndex());
        assertNotEquals(8, v7.getIndex());
    }

    @Test
    public void card() throws Exception {
        Var v7 = new Var(7, 2);

        assertNotEquals(1, v7.card());
        assertEquals(2, v7.card());
        assertNotEquals(3, v7.card());
    }

}