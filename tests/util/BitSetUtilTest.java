package util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 10/21/16.
 */
public class BitSetUtilTest {



    private  BitSet bits1;
   private  BitSet bits2;

    @Before
    public void setUp() throws Exception {
        bits1 = new BitSet();
        bits2 = new BitSet();
    }

    @After
    public void tearDown() throws Exception {
        bits1 = null;
        bits2 = null;
    }
    @Test
    public void tableSize() throws Exception {

        BitSet clique = new BitSet();

        clique.set(1);
        clique.set(2);
        clique.set(3);
        clique.set(4);
        clique.set(5);  //  {1, 2, 3, 4, 5};
        int[] weights = new int[]{999, 3,5,7,3,2};
        assertEquals(BigInteger.valueOf(630), util.BitSetUtil.tableSize(clique, weights));
    }
    @Test
    public void set_difference() throws Exception {
        bits1.set(1);
        bits1.set(2);
        bits1.set(5);
        bits1.set(9);//{1, 2,5,9 }

        bits2.set(2);
        bits2.set(5);
        bits2.set(7);//{2,5,7}

        BitSet result = util.BitSetUtil.set_difference(bits1, bits2);//{1,9}

        assertEquals(false, result.get(0));
        assertEquals(true, result.get(1));
        assertEquals(false, result.get(2));
        assertEquals(false, result.get(3));
        assertEquals(false, result.get(4));
        assertEquals(false, result.get(5));
        assertEquals(false, result.get(6));
        assertEquals(false, result.get(7));
        assertEquals(false, result.get(8));
        assertEquals(true, result.get(9));

        }

    @Test
    public void set_intersection() throws Exception {
        bits1.set(1);
        bits1.set(2);
        bits1.set(3);
        bits1.set(4);
        bits1.set(5);
        bits1.set(6);
        bits1.set(7);
        bits1.set(8);//{1, 2,3,4, 5,6,7,8 }

        bits2.set(5);
        bits2.set(7);
        bits2.set(9);
        bits2.set(10);//{5,7,9,10}

        BitSet result = util.BitSetUtil.set_intersection(bits1, bits2);//{5 7}
        assertEquals(false, result.get(0));
        assertEquals(false, result.get(1));
        assertEquals(false, result.get(2));
        assertEquals(false, result.get(3));
        assertEquals(false, result.get(4));
        assertEquals(true, result.get(5));
        assertEquals(false, result.get(6));
        assertEquals(true, result.get(7));
        assertEquals(false, result.get(8));
        assertEquals(false, result.get(9));
        assertEquals(false, result.get(10));
    }

    @Test
    public void set_symmetric_difference() throws Exception {
        bits1.set(1);
        bits1.set(2);
        bits1.set(3);
        bits1.set(4);
        bits1.set(5);
        bits1.set(6);
        bits1.set(7);
        bits1.set(8);//{1, 2,3,4, 5,6,7,8 }

        bits2.set(5);
        bits2.set(7);
        bits2.set(9);
        bits2.set(10);//{5,7,9,10}

        BitSet result = util.BitSetUtil.set_symmetric_difference(bits1, bits2);//{1 2 3 4 6 8 9 10}
        assertEquals(false, result.get(0));
        assertEquals(true, result.get(1));
        assertEquals(true, result.get(2));
        assertEquals(true, result.get(3));
        assertEquals(true, result.get(4));
        assertEquals(false, result.get(5));
        assertEquals(true, result.get(6));
        assertEquals(false, result.get(7));
        assertEquals(true, result.get(8));
        assertEquals(true, result.get(9));
        assertEquals(true, result.get(10));
    }

    @Test
    public void set_union() throws Exception {
        bits1.set(1);
        bits1.set(2);
        bits1.set(3);
        bits1.set(4);
        bits1.set(5);//{1, 2,3,4, 5 }

        bits2.set(3);
        bits2.set(4);
        bits2.set(5);
        bits2.set(6);
        bits2.set(7);//{3,4,5,6,7}

        BitSet result = util.BitSetUtil.set_union(bits1, bits2);//{1,2,3,4,5,6,7}
        assertEquals(false, result.get(0));
        assertEquals(true, result.get(1));
        assertEquals(true, result.get(2));
        assertEquals(true, result.get(3));
        assertEquals(true, result.get(4));
        assertEquals(true, result.get(5));
        assertEquals(true, result.get(6));
        assertEquals(true, result.get(7));
        assertEquals(false, result.get(8));
    }
    @Test
    public void set_union_element() throws Exception {
        bits1.set(1);
        bits1.set(2);
        bits1.set(3);   //{1,2,3}

        BitSet result = util.BitSetUtil.set_union(bits1, 5);//{1,2,3}
        assertEquals(false, result.get(0));
        assertEquals(true, result.get(1));
        assertEquals(true, result.get(2));
        assertEquals(true, result.get(3));
        assertEquals(false, result.get(4));
        assertEquals(true, result.get(5)/* result[5] is set to be true. */);

        assertEquals(false, bits1.get(0));
        assertEquals(true, bits1.get(1));
        assertEquals(true, bits1.get(2));
        assertEquals(true, bits1.get(3));
        assertEquals(false, bits1.get(4));
        assertEquals(false, bits1.get(5)/* bits1 is unchanged */);

    }
}