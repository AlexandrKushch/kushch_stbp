package com.khpi.stbp.lab01.src;

public class SHA1 {
    private final String message;

    private String M;
    private String[] Ms;
    private int[] H;
    private int[] W;

    private final int[] K = new int[]{
            0x5a827999,
            0x6ed9eba1,
            0x8f1bbcdc,
            0xca62c1d6
    };

    public SHA1(String message) {
        this.message = message;
        calculateM();
        setH();
        W = new int[80];
    }

    public void calculateM() {
        M = "";

        for (int j = 0; j < message.length(); j++) {
            String binary = Integer.toBinaryString(message.charAt(j));
            M += String.format("%8s", binary).replace(' ', '0');
        }

        M += "1";
        int k = 0;
        int mLength = M.length();

        if (mLength % 512 < 448) {
            k = 448 - (mLength % 512);
        } else {
            k = 512 - (mLength % 448);
        }

        M += String.format("%" + k + "s", "").replace(' ', '0');

        String binaryL = Integer.toBinaryString(message.length() * 8);

        String l = String.format("%" + 64 + "s", binaryL).replace(' ', '0');

        M += l;

        System.out.println(M);
        System.out.println(M.length());

        Ms = new String[M.length() / 512];

        for (int i = 0; i < Ms.length; i++) {
            Ms[i] = M.substring(i * 512, (i * 512) + 512);
        }
    }

    public void setH() {
        H = new int[5];

        H[0] = 0x67452301;
        H[1] = 0xefcdab89;
        H[2] = 0x98badcfe;
        H[3] = 0x10325476;
        H[4] = 0xc3d2e1f0;
    }

    public void calculateW(int i, int t) {
        if (t < 16) {
            String m = Ms[i].substring(t * Integer.SIZE, (t * Integer.SIZE) + Integer.SIZE);

            try {
                W[t] = Integer.parseInt(m, 2);
            } catch (Exception e) {
                W[t] = Integer.parseInt("-" + m, 2);

            }
            System.out.println(String.format("W[%d]:\t%8s", t, Integer.toHexString(W[t])).replace(' ', '0'));
        } else {
            W[t] = Integer.rotateLeft(W[t - 3] ^ W[t - 8] ^ W[t - 14] ^ W[t - 16], 1);
        }
    }

    public int getK(int t) {
        if (t < 20) {
            return K[0];
        } else if (t < 40) {
            return K[1];
        } else if (t < 60) {
            return K[2];
        } else {
            return K[3];
        }
    }

    public int ft(int x, int y, int z, int t) {
        if (t < 20) {
            return (x & y) ^ (~x & z);
        } else if (t < 40) {
            return x ^ y ^ z;
        } else if (t < 60) {
            return (x & y) ^ (x & z) ^ (y & z);
        } else {
            return x ^ y ^ z;
        }
    }

    public String sha1() {
        int a = 0, b = 0, c = 0, d = 0, e = 0;
        for (int i = 0; i < Ms.length; i++) {

            for (int t = 0; t < 80; t++) {
                calculateW(i, t);
            }

            a = H[0];
            b = H[1];
            c = H[2];
            d = H[3];
            e = H[4];

            for (int t = 0; t < 80; t++) {
                int T = Integer.rotateLeft(a, 5) + ft(b, c, d, t) + e + getK(t) + W[t];
                e = d;
                d = c;
                c = Integer.rotateLeft(b, 30);
                b = a;
                a = T;

                System.out.format("t = %d:\t%8s\t%8s\t%8s\t%8s\t%8s%n", t, Integer.toHexString(a), Integer.toHexString(b), Integer.toHexString(c), Integer.toHexString(d), Integer.toHexString(e));
            }

            H[0] = a + H[0];
            H[1] = b + H[1];
            H[2] = c + H[2];
            H[3] = d + H[3];
            H[4] = e + H[4];

            System.out.format("%8s%n", Integer.toHexString(H[0]));
            System.out.format("%8s%n", Integer.toHexString(H[1]));
            System.out.format("%8s%n", Integer.toHexString(H[2]));
            System.out.format("%8s%n", Integer.toHexString(H[3]));
            System.out.format("%8s%n", Integer.toHexString(H[4]));
        }

        return String.format("%40s", Integer.toHexString(H[0]) + Integer.toHexString(H[1]) + Integer.toHexString(H[2]) + Integer.toHexString(H[3]) + Integer.toHexString(H[4])).replace(' ', '0');
    }

    public static void main(String[] args) {
        String aLotOfA = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer luctus ante magna, id maximus libero vestibulum eget. Curabitur ullamcorper arcu quis felis fringilla congue. Morbi vitae semper odio. Sed egestas mauris iaculis orci consectetur commodo. Quisque vulputate, velit nec semper congue, elit urna hendrerit dui, pulvinar iaculis ligula dui id ligula. Cras malesuada tristique mauris, id convallis purus varius et. Phasellus dignissim ante auctor, ullamcorper quam sit amet, sagittis nulla. Donec sed lacinia nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce fringilla tortor eu velit malesuada mattis. Nam quis velit consequat, convallis urna et, lobortis ante. Nam a augue eu metus ullamcorper lacinia. Quisque sit amet ligula felis. Nullam sagittis arcu ut molestie fringilla. In ac ex porta, dictum purus a.";

        System.out.println(aLotOfA);
        SHA1 s = new SHA1(aLotOfA);

        System.out.println(s.sha1());
    }
}
