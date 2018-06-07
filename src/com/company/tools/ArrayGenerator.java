package com.company.tools;

import java.util.Random;

public class ArrayGenerator {

    public static int[] nextArrayInt(int size, int max, int min) {
        Random random = new Random();
        int[] out = new int[size];
        for (int i = 0; i < size; i++) {
            out[i] = min + random.nextInt(max - min);
        }
        return out;
    }

    public static int[] nextArrayInt(int size, int max){
        return nextArrayInt(size, max, 0);
    }

    public static double[] nextArrayDouble(int size, int max, int min) {
        Random random = new Random();
        double[] out = new double[size];
        for (int i = 0; i < size; i++) {
            out[i] = random.nextDouble() * (max - min) + min;
        }
        return out;
    }

    public static double[] nextArrayDouble(int size, int max){
        return nextArrayDouble(size,  max, 0);
    }

    public static void fill(double[] a, int from, int to, int max, int min){
        double[] b = nextArrayDouble(from - to, min, max);
        for (int i = from, j = 0; i <= to ; i++, j++) {
            a[i] = b[j];
        }
    }

    public static void fill(double[] a, int from, int to, int max){
        fill(a,from, to, max, 0);
    }

    public static void fill(int a[], int from, int to, int max, int min){
        int[] b = nextArrayInt(from - to, min, max);
        for (int i = from, j = 0; i <= to ; i++, j++) {
            a[i] = b[j];
        }
    }

    public static void fill(int[] a, int from, int to, int max){
        fill(a, from, to, max,0);
    }

}
