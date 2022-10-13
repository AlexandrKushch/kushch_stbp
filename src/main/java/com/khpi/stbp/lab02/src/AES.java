package com.khpi.stbp.lab02.src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AES {
    private final byte[] key;
    private final byte[] input;
    private byte[] out;
    private String decodedString = "";

    private final int Nk = 4;
    private final int Nb = 4;
    private final int Nr = 10;

    byte[] integersToBytes(int[] values) throws IOException {
        byte[] bytes = new byte[values.length];
        for(int i=0; i < values.length; ++i)
        {
            bytes[i] = (byte) ((byte) values[i] & 0xFF);
        }

        return bytes;
    }
    public AES(String key, String input) throws IOException {
        this.key = key.getBytes();
        this.input = String.format("%" + (-16 * Math.ceilDiv(input.length(), 16)) + "s", input).getBytes();
        this.out = new byte[this.input.length];

        System.out.println("Key\n" + key);
        System.out.println("KeyBytes");
        for (int i = 0; i < this.key.length; i++) {
            System.out.printf("%2x ", this.key[i]);
        }
        System.out.println();

        System.out.println("Input\n" + input);
        System.out.println("InputBytes");
        for (int i = 0; i < this.input.length; i++) {
            System.out.printf("%2x ", this.input[i]);
        }
        System.out.println();
    }

    private byte[] from2dto1d(byte[][] in) {
        byte[] out = new byte[in.length * in[0].length];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[i % Nb][i / Nb];
        }
        return out;
    }

    private byte[][] from1dto2d(byte[] in) {
        byte[][] out = new byte[in.length / Nb][in.length / Nb];
        for (int i = 0; i < in.length; i++) {
            out[i % Nb][i / Nb] = in[i];
        }
        return out;
    }

    public void encode() {
        for (int i = 0; i < input.length / 16; i++) {
            byte[][] in = from1dto2d(Arrays.copyOfRange(input, i * 16, (i * 16) + 16));

            byte[][] o = Cipher.cipher(in, key);
            Cipher.printState(o);
            System.arraycopy(from2dto1d(o), 0, out, i * 16, 16);

            System.out.println("Out Bytes");
            for (int j = 0; j < out.length; j++) {
                System.out.printf("%2x ", out[j]);
            }
            System.out.println();
        }
    }

    public void decode() {
        for (int i = 0; i < out.length / 16; i++) {
            byte[][] o = from1dto2d(Arrays.copyOfRange(out, i * 16, (i * 16) + 16));
            byte[][] in = InvCipher.invCipher(o, key);
            Cipher.printState(in);
            byte[] decodedBytes = from2dto1d(in);
            System.out.println("DecodedBytes");
            for (int j = 0; j < decodedBytes.length; j++) {
                System.out.printf("%2x ", decodedBytes[j]);
            }
            System.out.println();

            decodedString += new String(decodedBytes, StandardCharsets.UTF_8);
            System.out.println(decodedString);
        }
    }

    public static void main(String[] args) throws IOException {
        String key = "secretKey AES128";
        String input = "hello im friendly fire shooting bang";

        AES aes = new AES(key, input);
        aes.encode();
        aes.decode();
    }
}
