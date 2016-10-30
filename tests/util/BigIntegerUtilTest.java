package util;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 10/23/16.
 */
public class BigIntegerUtilTest {
    @Test(expected = ArithmeticException.class)
    public void toIntExact() throws Exception {

        // create 3 BigInteger objects
        BigInteger bi1, bi2, bi3;

        // create 3 Integer objects
        Integer i1, i2, i3;

        // assign values to bi1, bi2, bi3
        bi1 = BigInteger.valueOf(Integer.MAX_VALUE);
        bi2 = BigInteger.valueOf(Integer.MAX_VALUE);
        bi3 = BigInteger.valueOf(Integer.MAX_VALUE);

        bi1 = bi1.subtract(BigInteger.ONE);
        bi3 = bi3.add(BigInteger.ONE);

        // assign the integer values of bi1, bi2 to i1, i2
        i1 = BigIntegerUtil.toIntExact(bi1);
        assertEquals((long)Integer.MAX_VALUE-1, (long)i1);
        i2 = BigIntegerUtil.toIntExact(bi2);    //  safely convert Integer.MAX_VALUE to int.
        assertEquals((long)Integer.MAX_VALUE, (long)i2);

        i3 = BigIntegerUtil.toIntExact(bi3);    // expected Exception
        assertEquals((long)Integer.MAX_VALUE, (long)i1);

    }
}