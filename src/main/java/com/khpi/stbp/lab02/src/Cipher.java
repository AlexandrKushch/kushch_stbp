package com.khpi.stbp.lab02.src;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Cipher {
    private static final int Nk = 4;
    private static final int Nb = 4;
    private static final int Nr = 10;

    private static int[] expandedKey;
    private static int[] rcon;
    private static final int[][] sbox = new int[][]{
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };

    private static final byte[][] mixColumnConstant = {
            {0x02, 0x03, 0x01, 0x01},
            {0x01, 0x02, 0x03, 0x01},
            {0x01, 0x01, 0x02, 0x03},
            {0x03, 0x01, 0x01, 0x02}
    };

    static {
        rcon = generateRcon();
    }

    public static int subWord(int word) {
        byte[] bytes = ByteBuffer.allocate(Nb).putInt(word).array();
        subBytes(bytes);
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static void subBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {

            byte low = (byte) (bytes[i] & 0x0F);
            byte high = (byte) ((bytes[i] & 0xF0) >> 4);

            bytes[i] = (byte) (sbox[high][low] & 0xFF);
        }
    }

    public static void shiftRows(byte[][] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            int word = ByteBuffer.wrap(bytes[i]).getInt();
            word = Integer.rotateLeft(word, i * Byte.SIZE);
            bytes[i] = ByteBuffer.allocate(Nb).putInt(word).array();
        }
    }


    public static byte[][] mixColumns(byte[][] bytes) {
        byte[][] mixed = new byte[bytes.length][bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                byte a = bytes[0][0];

                byte r = (byte) (
                        (PolyMulti.multiply(bytes[0][j], mixColumnConstant[i][0])
                                ^ PolyMulti.multiply(bytes[1][j], mixColumnConstant[i][1])
                                ^ PolyMulti.multiply(bytes[2][j], mixColumnConstant[i][2])
                                ^ PolyMulti.multiply(bytes[3][j], mixColumnConstant[i][3])));

                mixed[i][j] = r;
            }
        }

        return mixed;
    }

    private static void addRoundKey(byte[][] state, int[] keys) {
        byte[][] k = new byte[state.length][state[0].length];
        byte[] row;

        for (int i = 0; i < keys.length; i++) {
            row = ByteBuffer.allocate(Nk).putInt(keys[i]).array();

            for (int j = 0; j < row.length; j++) {
                k[j][i] = row[j];
            }
        }

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = (byte) (state[i][j] ^ k[i][j]);
            }
        }
    }

    private static int[] generateRcon() {
        int[] rcon = new int[Nr + 1];
        int x = 0x01;
        rcon[0] = (x & 0xFF) << 24;

        for (int i = 1; i < Nr; i++) {
            if (0 != (x & 0x80)) {
                x = x << 1;
                x = x ^ 0x1b;
            } else {
                x = x << 1;
            }

            rcon[i] = (x & 0xFF) << 24;
        }

        System.out.println("RCON");
        for (int i :
                rcon) {
            System.out.printf("%2x ", i);
        }
        System.out.println();
        return rcon;
    }

    public static int[] expansion(byte[] key) {
        int[] words = new int[Nb * (Nr + 1)];

        for (int i = 0; i < Nk; i++) {
            words[i] = ByteBuffer.wrap(key, Nk * i, Nk).getInt();
        }

        for (int i = Nk; i < Nb * (Nr + 1); i++) {
            int temp = words[i - 1];

            if (i % Nk == 0) {
                temp = subWord(Integer.rotateLeft(temp, 8)) ^ rcon[i / Nk];
            }

            words[i] = words[i - Nk] ^ temp;
        }

        return words;
    }

    public static void printState(byte[][] state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                System.out.printf("%2x ", state[i][j]);
            }
            System.out.println();
        }
    }

    public static byte[] from2dto1d(byte[][] in) {
        byte[] out = new byte[in.length * in[0].length];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[i % Nb][i / Nb];
        }
        return out;
    }

    public static void printStrFromBytes(byte[] bytes) {
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

    public static void printStrFromBytes(byte[][] bytes) {
        byte[] b = from2dto1d(bytes);
        printStrFromBytes(b);
    }

    public static byte[][] cipher(byte[][] in, byte[] key) {
        expandedKey = expansion(key);
        byte[][] state = in;
        addRoundKey(state, Arrays.copyOfRange(expandedKey, 0, Nb));

        for (int i = 1; i < Nr; i++) { // Nr - 1
            for (int j = 0; j < state.length; j++) {
                subBytes(state[j]);
            }

            shiftRows(state);

            state = mixColumns(state);

            addRoundKey(state, Arrays.copyOfRange(expandedKey, Nb * i, (Nb * i) + Nb));
        }

        for (int j = 0; j < state.length; j++) {
            subBytes(state[j]);
        }

        shiftRows(state);

        addRoundKey(state, Arrays.copyOfRange(expandedKey, Nb * Nr, (Nb * Nr) + Nb));

        return state;
    }
}
