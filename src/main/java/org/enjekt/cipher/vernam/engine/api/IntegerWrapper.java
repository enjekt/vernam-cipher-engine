package org.enjekt.cipher.vernam.engine.api;

public class IntegerWrapper {


    private final Integer encryptedValue;
    private final int[] oneTimePad;

    public IntegerWrapper(Integer encryptedValue, int[] oneTimePad) {
        this.encryptedValue = encryptedValue;
        this.oneTimePad = oneTimePad;
    }

    public Integer getEncryptedValue() {
        return encryptedValue;
    }

    public int[] getOneTimePad() {
        return oneTimePad;
    }
}
