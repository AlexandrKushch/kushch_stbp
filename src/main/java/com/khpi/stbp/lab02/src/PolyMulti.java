package com.khpi.stbp.lab02.src;

public class PolyMulti {

    public static byte multiply(byte a, byte b) {
        byte[] polynom = new byte[Byte.SIZE];
        byte r = 0;

        polynom[0] = a;
        for (int i = 1; i < polynom.length; i++) {
            polynom[i] = xtime(polynom[i - 1], i);
        }

        StringBuilder sb = new StringBuilder(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
        sb.reverse();
        String bits = sb.toString();

        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
                r = (byte) (r ^ polynom[i]);
            }
        }

        return r;
    }

    private static byte xtime(byte a, int time) {
        byte r = a;

        if (0 != (r & 0x80)) {
            r = (byte) (r << 1);
            r = (byte) (r ^ 0x1b);
        } else {
            r = (byte) (r << 1);
        }

        return r;
    }
}
