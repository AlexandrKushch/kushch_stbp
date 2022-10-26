package com.khpi.stbp.lab03.src.rsa;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Key {

    private int n;
    private int e;
    private int d;

    public int getN() {
        return n;
    }

    public int getE() {
        return e;
    }

    public int getD() {
        return d;
    }

    public Key() {
        List<Integer> primes = IntStream.rangeClosed(2, 2000)
                .filter(this::isPrime).boxed().toList();

        int p = primes.get(new Random().nextInt(0, primes.size()));
        int q = primes.get(new Random().nextInt(0, primes.size()));

        n = p * q;
        int l = lcm(p - 1, q - 1);
        e = 2;

        while (e < l) {
            if (gcd(e, l) == 1 && isPrime(gcd(e, l))) {
                break;
            } else {
                e++;
            }
        }

        d = mulInverse(e, l);
    }

    public Key(int e, int n) {
        this.e = e;
        this.n = n;
    }

    private boolean isPrime(int num) {
        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

    private int mulInverse(int x, int n) {
        int d = 1;
        while ((x * d) % n != 1) {
            d++;
        }

        return d;
    }

    private int lcm(int a, int b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public BigInteger encrypt(BigInteger m) {
        return m.pow(e).mod(BigInteger.valueOf(n));
    }

    public BigInteger decrypt(BigInteger encM) {
        return encM.pow(d).mod(BigInteger.valueOf(n));
    }

    @Override
    public String toString() {
        return "N: " + n + "\nE: " + e + "\nD: " + d;
    }
}
