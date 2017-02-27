package util;

import java.math.BigInteger;

/**
 * Created by chaoli on 10/23/16.
 */
public class BigIntegerUtil {

    public static int toIntExact(BigInteger BigInteger_value) {
        BigInteger INT_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
        if (BigInteger_value.compareTo(INT_MAX) > 0)
            throw new ArithmeticException("integer overflow -> " +
                    "The clique table size is too large.");
        return BigInteger_value.intValue();
    }

    public static long toLongExact(BigInteger BigInteger_value) {
        BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
        if (BigInteger_value.compareTo(LONG_MAX) > 0)
            throw new ArithmeticException("long overflow -> " +
                    "The clique table size is too large.");
        return BigInteger_value.longValue();
    }

}
