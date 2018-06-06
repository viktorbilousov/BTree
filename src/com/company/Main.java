package com.company;

import com.company.tools.ArrayGenerator;
import com.company.tree.BTree;

import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int[] integers = ArrayGenerator.nextArrayInt(20, 1000);
        integers = new int[]{75, 195, 187, 962, 715, 0, 841, 304, 407, 711, 716, 216, 753, 997, 308, 706, 280, 613, 929, 42};
        System.out.println(Arrays.toString(integers));
        BTree<Integer> bTree = new BTree<>(4, Integer::compareTo);
        for (int i = 0; i < integers.length; i++) {
            System.out.println((i+1) + " " +integers[i]);
            bTree.add(integers[i]);
            System.out.println(bTree.test());

        }

        System.out.println(bTree.test());
        System.out.println(bTree.size() + " " + bTree.depth());

    }
}
