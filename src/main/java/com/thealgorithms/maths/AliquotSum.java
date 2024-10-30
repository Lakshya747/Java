package com.thealgorithms.maths;

import java.util.stream.IntStream;

/**
 * In number theory, the aliquot sum s(n) of a positive integer n is the sum of
 * all proper divisors of n, that is, all divisors of n other than n itself.
 * For example, the proper divisors of 15 (that is, the positive divisors of 15 that
 * are not equal to 15) are 1, 3 and 5, so the aliquot sum of 15 is 9 i.e. (1 + 3 + 5).
 * Wikipedia: https://en.wikipedia.org/wiki/Aliquot_sum
 */
public final class AliquotSum {

    private AliquotSum() {}

    /**
     * Finds the aliquot sum of an integer number.
     *
     * @param number a positive integer
     * @return aliquot sum of given {@code number}
     */
    public static int getAliquotValue(int number) {
        var sumWrapper = new Object() {
            int value = 0;
        };
        IntStream.iterate(1, i -> ++i)
            .limit(number / 2)
            .filter(i -> number % i == 0)
            .forEach(i -> sumWrapper.value += i);
        return sumWrapper.value;
    }

    /**
     * Function to calculate the aliquot sum of an integer number
     *
     * @param n a positive integer
     * @return aliquot sum of given {@code number}
     */
    public static int getAliquotSum(int n) {
        if (n <= 0) {
            return -1;
        }
        int sum = 1;
        double root = Math.sqrt(n);

        // Get factors before and after the square root
        for (int i = 2; i <= root; i++) {
            if (n % i == 0) {
                sum += i;
                if (i != n / i) {
                    sum += n / i;
                }
            }
        }

        // If n is a perfect square, adjust the sum
        if (root == (int) root) {
            sum -= root;
        }

        return sum;
    }
}
