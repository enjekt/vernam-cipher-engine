package org.enjekt.cipher.vernam.engine.internal.cipherengines;

import org.enjekt.cipher.vernam.engine.internal.functions.AddWrapLimit;
import org.enjekt.cipher.vernam.engine.internal.functions.IntCollector;
import org.enjekt.cipher.vernam.engine.internal.functions.SubtractWrapLimit;
import org.enjekt.cipher.vernam.engine.internal.util.RandomNumberGenerator;

import java.util.Arrays;

/**
 * The type Utf 8 cipher engine. This is the general purpose engine for creating encrypted values.
 */
public class UTF8CipherEngine {

    private static RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();

    private final int lowerUTF8Limit;
    private final int upperUTF8Limit;
    private final int lowerKeyRange;
    private final int upperKeyRange;

    /**
     * Instantiates a new Utf 8 cipher engine.
     *
     * @param lowerUTF8Limit the lower utf 8 limit
     * @param upperUTF8Limit the upper utf 8 limit
     */
    public UTF8CipherEngine(int lowerUTF8Limit, int upperUTF8Limit) {
        this.lowerUTF8Limit = lowerUTF8Limit;
        this.upperUTF8Limit = upperUTF8Limit;
        this.lowerKeyRange = 0;
        this.upperKeyRange = upperUTF8Limit - lowerUTF8Limit;
    }


    /**
     * Decrypt value composer.
     *
     * @param values         the toDecrypt
     * @param oneTimePad the encryption keys
     * @return the value composer
     */
    public int[] decipher(int[] values, int[] oneTimePad) {

        IntCollector collector = new IntCollector(values.length);
        Arrays.stream(values).map(new SubtractWrapLimit(oneTimePad, lowerUTF8Limit, upperKeyRange + 1)).forEach(collector);

        return collector.getValues();


    }

    /**
     * Encrypt encrypted results holder.
     *
     * @param values the value
     * @return the encrypted results holder
     */
    public EncryptedResultsHolder encipher(int[] values) {
        int[] oneTimePad = RandomNumberGenerator.ints(values.length, lowerKeyRange, upperKeyRange).toArray();
        EncryptedResultsHolder holder = new EncryptedResultsHolder(oneTimePad);
        Arrays.stream(values).map(new AddWrapLimit(oneTimePad, upperUTF8Limit, upperKeyRange + 1)).forEach(holder);

        return holder;


    }


}
