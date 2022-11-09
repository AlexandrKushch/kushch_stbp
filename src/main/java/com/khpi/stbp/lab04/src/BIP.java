package com.khpi.stbp.lab04.src;

import com.khpi.stbp.lab02.src.AES;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BIP {

    public String generateEntropy() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[16];

        random.nextBytes(entropy);

        System.out.println(Arrays.toString(entropy));

        for (int i = 0; i < entropy.length; i++) {
            sb.append(String.format("%8s", Integer.toBinaryString(entropy[i] & 0xFF)).replace(' ', '0'));
        }

        int checksum = sb.length() / Integer.SIZE;

        sb.append(String.format("%4s", Integer.toBinaryString(checksum & 0xF)).replace(' ', '0'));

        System.out.println(sb);
        return sb.toString();
    }

    public int[] parseBitsToDecimals(String entropy) {
        int countOfBits = 11;
        int size = entropy.length() / countOfBits;
        int[] decimals = new int[size];

        for (int i = 0; i < size; i++) {
            String line = entropy.substring(i * countOfBits, (i * countOfBits) + countOfBits);
            decimals[i] = Integer.parseInt(line, 2);
        }

        return decimals;
    }

    public byte[] parseDecimalsToBytes(int[] decimals) {
        StringBuilder sb = new StringBuilder();

        int size = decimals.length;

        for (int i = 0; i < size; i++) {
            String s = String.format("%11s", Integer.toBinaryString(decimals[i])).replace(' ', '0');
            sb.append(s);
        }

        String bits = sb.toString();
        int countOfBits = 8;
        byte[] bytes = new byte[16];

        for (int i = 0; i < 16; i++) {
            String line = bits.substring(i * countOfBits, (i * countOfBits) + countOfBits);
            int word = Integer.parseInt(line, 2);
            bytes[i] = (byte) (word & 0xFF);
        }

        return bytes;
    }

    public String getKeyString() {
        String entropy = generateEntropy();
        int[] words = parseBitsToDecimals(entropy);

        File file = new File("src\\main\\resources\\bip39\\english.txt");
        StringBuilder sb = new StringBuilder();

        try {
            List<String> fileStream = Files.readAllLines(file.toPath());

            for (int i = 0; i < words.length; i++) {
                sb.append(fileStream.get(words[i] + 1)).append(" ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public byte[] getKeyBytes(String sKey) {
        File file = new File("src\\main\\resources\\bip39\\english.txt");

        String[] splitSKey = sKey.split(" ");
        int l = splitSKey.length;
        int[] words = new int[l];

        try {
            List<String> fileStream = Files.readAllLines(file.toPath());

            for (int i = 0; i < l; i++) {
                words[i] = fileStream.indexOf(splitSKey[i]) - 1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] sBytes = parseDecimalsToBytes(words);

        return sBytes;
    }

    public static void main(String[] args) {
        BIP bip = new BIP();
        String key = bip.getKeyString();
        System.out.println(key);
        System.out.println(key.length());

        byte[] b = bip.getKeyBytes(key);
        System.out.println(Arrays.toString(b));

        AES aes = new AES(b, "hello im friendly fire shooting bang");
        aes.encode();
        aes.decode();
    }
}
