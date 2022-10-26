package com.khpi.stbp.lab03.src.rsa;

import java.math.BigInteger;

public class RSA {
    public static BigInteger[] encryption(String message, Key k) {
        byte[]  bytes = message.getBytes();
        BigInteger[] bigs = new BigInteger[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            bigs[i] = k.encrypt(BigInteger.valueOf(bytes[i]));
        }

        return bigs;
    }

    public static BigInteger[] decryption(BigInteger[] encryptedMessage, Key k) {
        BigInteger[] decrypted = new BigInteger[encryptedMessage.length];

        for (int i = 0; i < encryptedMessage.length; i++) {
            decrypted[i] = k.decrypt(encryptedMessage[i]);
        }

        return decrypted;
    }
}
