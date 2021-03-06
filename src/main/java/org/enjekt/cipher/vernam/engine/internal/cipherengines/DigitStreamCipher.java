package org.enjekt.cipher.vernam.engine.internal.cipherengines;

import org.enjekt.cipher.vernam.engine.internal.functions.Digit;
import org.enjekt.cipher.vernam.engine.internal.functions.DigitDecryptor;
import org.enjekt.cipher.vernam.engine.internal.functions.DigitEncryptor;
import org.enjekt.cipher.vernam.engine.internal.util.RandomNumberGenerator;
import org.enjekt.cipher.vernam.engine.internal.util.RandomNumberGeneratorFactory;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 * The type Digit stream cipher works for all digit streams created from long, Integer, Short.
 * The Encryptor/Decryptors skip any non 0-9 digits during processing so that - and . are simply
 * ignored.
 * <p>
 * The cipher engine using this will pass in an array of the maximum digits legal for a maximum length
 * number of its type. For example, the maximum size of an Integer is 2,147,483,647. The array would thus
 * be {2,1,4,7,4,8,3,6,4,7} or 10 digits long. Any integer to be enciphered which has a length of 10 must then
 * ensure that the first digit is 2 or less but not 0. Each cipher engine keeps a static final array of the
 * valid array of int for its type and passes it into the DigitStreamCipher when it instantiates it for processing.
 * <p>
 * The DigitStreamCipher is stateless and thread safe.
 */
public class DigitStreamCipher {

    private final int[] maximumDigitsForDataType;
    private RandomNumberGenerator randomNumberGenerator = RandomNumberGeneratorFactory.getGenerator();

    /**
     * Instantiates a new Digit stream cipher.
     *
     * @param maxDigits the max digits
     */
    public DigitStreamCipher(int[] maxDigits) {
        this.maximumDigitsForDataType = maxDigits;
    }


    public int[] encipher(String value, IntConsumer composer) {

        int[] oneTimePad = randomNumberGenerator.ints(value.length()).toArray();
        //We do a number of numeric operations on the digit for wrapping, leading zero, max value and so on
        //and it is simpler and faster to strip the UTF8 values off and manipulate the digits as 0..9 int and
        //then simply add the lower value of 0 in UTF8 back as the last step.
        value.chars().map(Digit::toInt).map(new DigitEncryptor(maximumDigitsForDataType, oneTimePad)).map(Digit::toUTF).forEach(composer);
        return oneTimePad;
    }


    public void decipher(int[] values, int[] oneTimePad, IntConsumer composer) {

        //We don't subtract UTF8 as in the encipher operation because we are not doing calculations of valid pad
        //values for leading zero or max values in this case. We are simply subtracting the oneTimePad and then
        //doing a modulo operation. So we avoid two extra operations per digit this way.
        Arrays.stream(values).map(new DigitDecryptor(oneTimePad)).forEach(composer);
    }

}
