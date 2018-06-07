package com.company;

import com.company.tools.ArrayGenerator;
import com.company.tree.BTree;


public class Main {

    public static void main(String[] args) {
        int[] integers = ArrayGenerator.nextArrayInt(500, 500, -500);
        integers = new int[]{13, 16, 10, 11, 24, 4, 12, 2, 15, 18, 22, 26, 17, 14, 25, 1, 7, 3, 21, 8, 19, 5, 23, 6, 20, 9};
        BTree<Integer> bTree = new BTree<>(5, Integer::compareTo);

        for (int i = 0; i < integers.length; i++) {
            bTree.add(integers[i]);
        }

        BTree<Integer> newTree = bTree.copy();
        System.out.println(newTree);

        System.out.println(bTree.size() + " " + bTree.depth());

        for (int i = integers.length - 1; i >= 0; i--) {
            bTree.remove(integers[i]);
            System.out.println(bTree);
            System.out.println();
        }



    }
}
