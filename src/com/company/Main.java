package com.company;

import com.company.tools.ArrayGenerator;
import com.company.tree.BTree;

import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int[] integers = ArrayGenerator.nextArrayInt(50, 100);
       // integers = new int[]{278, 600, 639, 530, 191, 941, 244, 493, 681, 117, 520, 173, 693, 960, 499, 365, 11, 612, 102, 215};
        //System.out.println(Arrays.toString(integers));
        BTree<Integer> bTree = new BTree<>(5, Integer::compareTo);

        for (int i = 0; i < integers.length; i++) {
            bTree.add(integers[i]);
        }

        BTree<Integer> newTree = bTree.copy();
        System.out.println(newTree);

        //System.out.println(bTree.test());
        System.out.println(bTree.size() + " " + bTree.depth());

        for (int i = 0; i < integers.length; i++) {
            //System.out.println((i + 1) + " "+ integers[i]);
            bTree.remove(integers[i]);

        }



    }
}
