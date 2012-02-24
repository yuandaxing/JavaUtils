
package cgcl.data.structure;
/**
 * The following calculations are taken from:
 * http://www.cs.wisc.edu/~cao/papers/summary-cache/node8.html
 * "Bloom Filters - the math"
 *
 * This class's static methods are meant to facilitate the use of the Bloom
 * Filter class by helping to choose correct values of 'bits per element' and
 * 'number of hash functions, k'.
 */
/*
 * probs
 * 这里的行表示每个字符串应该使用多少bit位
 * 列表示使用多少hash函数，值表示出错的概率
 * 注意probs中的值表示出错的概率
 * 这个值和字符串的长度没有关系，字符串的多少没有关系。
 * 
 * 最后要说明的是bit位和hash函数不需要相等。
 * computeBloomSpec 这个方法首先考虑的是尽可能少的使用bit，然后是尽可能少的使用hash函数
 */

class BloomCalculations {

    private static final int minBuckets = 2;
    private static final int minK = 1;
    private static final int[] optKPerBuckets =
            new int[]{1, // dummy K for 0 buckets per element
                      1, // dummy K for 1 buckets per element
                      1, 2, 3, 3, 4, 5, 5, 6, 7, 8, 8, 8, 8, 8};

    /**
     * In the following table, the row 'i' shows false positive rates if i buckets
     * per element are used.  Column 'j' shows false positive rates if j hash
     * functions are used.  The first row is 'i=0', the first column is 'j=0'.
     * Each cell (i,j) the false positive rate determined by using i buckets per
     * element and j hash functions.
     */
    static final double[][] probs = new double[][]{
        {1.0}, // dummy row representing 0 buckets per element
        {1.0, 1.0}, // dummy row representing 1 buckets per element
        {1.0, 0.393,  0.400},
        {1.0, 0.283,  0.237,  0.253},
        {1.0, 0.221,  0.155,  0.147,   0.160},
        {1.0, 0.181,  0.109,  0.092,   0.092,   0.101}, // 5
        {1.0, 0.154,  0.0804, 0.0609,  0.0561,  0.0578,  0.0638},
        {1.0, 0.133,  0.0618, 0.0423,  0.0359,  0.0347,  0.0364},
        {1.0, 0.118,  0.0489, 0.0306,  0.024,   0.0217,  0.0216,  0.0229},
        {1.0, 0.105,  0.0397, 0.0228,  0.0166,  0.0141,  0.0133,  0.0135,  0.0145}, // 9
        {1.0, 0.0952, 0.0329, 0.0174,  0.0118,  0.00943, 0.00844, 0.00819, 0.00846},
        {1.0, 0.0869, 0.0276, 0.0136,  0.00864, 0.0065,  0.00552, 0.00513, 0.00509},
        {1.0, 0.08,   0.0236, 0.0108,  0.00646, 0.00459, 0.00371, 0.00329, 0.00314},
        {1.0, 0.074,  0.0203, 0.00875, 0.00492, 0.00332, 0.00255, 0.00217, 0.00199},
        {1.0, 0.0689, 0.0177, 0.00718, 0.00381, 0.00244, 0.00179, 0.00146, 0.00129},
        {1.0, 0.0645, 0.0156, 0.00596, 0.003,   0.00183, 0.00128, 0.001,   0.000852} // 15
    };  // the first column is a dummy column representing K=0.

    /**
     * Given the number of buckets that can be used per element, return a
     * specification that minimizes the false positive rate.
     *
     * @param bucketsPerElement The number of buckets per element for the filter.
     * 这里的意思是多少个字符
     * @return A spec that minimizes the false positive rate.
     */
    public static BloomSpecification computeBloomSpec(int bucketsPerElement)
    {
        assert bucketsPerElement >= 1;
        assert bucketsPerElement <= probs.length - 1;
        
        return new BloomSpecification(optKPerBuckets[bucketsPerElement], bucketsPerElement);
    }

    /**
     * A wrapper class that holds two key parameters for a Bloom Filter: the
     * number of hash functions used, and the number of buckets per element used.
     */
    public static final class BloomSpecification {
        final int K; // number of hash functions.
        final int bucketsPerElement;

        public BloomSpecification(int k, int bucketsPerElement) {
            K = k;
            this.bucketsPerElement = bucketsPerElement;
        }
    }

    /**
     * Given a maximum tolerable false positive probability, compute a Bloom
     * specification which will give less than the specified false positive rate,
     * but minimize the number of buckets per element and the number of hash
     * functions used.  Because bandwidth (and therefore total bitvector size)
     * is considered more expensive than computing power, preference is given
     * to minimizing buckets per element rather than number of hash functions.
     * @param maxBucketsPerElement The maximum number of buckets available for the filter.
     * @param maxFalsePosProb The maximum tolerable false positive rate.
     * @return A Bloom Specification which would result in a false positive rate
     * less than specified by the function call
     * @throws UnsupportedOperationException if a filter satisfying the parameters cannot be met
     */
    public static BloomSpecification computeBloomSpec(int maxBucketsPerElement, double maxFalsePosProb)
    {
        assert maxBucketsPerElement >= 1;
        assert maxBucketsPerElement <= probs.length - 1;
        int maxK = probs[maxBucketsPerElement].length - 1;

        // Handle the trivial cases
        if(maxFalsePosProb >= probs[minBuckets][minK]) {
            return new BloomSpecification(2, optKPerBuckets[2]);
        }
        if (maxFalsePosProb < probs[maxBucketsPerElement][maxK]) {
            throw new UnsupportedOperationException(String.format("Unable to satisfy %s with %s buckets per element",
                                                                  maxFalsePosProb, maxBucketsPerElement));
        }

        // First find the minimal required number of buckets:
        int bucketsPerElement = 2;
        int K = optKPerBuckets[2];
        while(probs[bucketsPerElement][K] > maxFalsePosProb){
            bucketsPerElement++;
            K = optKPerBuckets[bucketsPerElement];
        }
        // Now that the number of buckets is sufficient, see if we can relax K
        // without losing too much precision.
        while(probs[bucketsPerElement][K - 1] <= maxFalsePosProb){
            K--;
        }

        return new BloomSpecification(K, bucketsPerElement);
    }
}
