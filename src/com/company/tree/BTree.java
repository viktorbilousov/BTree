package com.company.tree;


import java.util.*;

public class BTree<T> implements iTree<T> {


    private Node<T> root = null;
    private Comparator<T> comparator;
    private int size = 0;
    private int depth = 0;

    private final int ORDER;
    private final int MAX_KEY;
    private final int MIN_KEY;

    private class Node<T> {
        private Node<T> root = null;
        private Node<T>[] child = new Node[0];
        private T[] keys = (T[]) new Object[0];
        private Comparator<T> _comp = (Comparator<T>) comparator;

        public Node(T[] keys, Node<T> root, Node<T>[] child) {
            this.root = root;
            this.child = child;
            this.keys = keys;
        }

        public Node() {

        }

        public Node(T... keys) {
            this.keys = keys;
        }

        //region get/set
        public Node<T> getRoot() {
            return root;
        }

        public void setRoot(Node<T> root) {
            this.root = root;
        }

        public Node<T>[] getChilds() {
            return child;
        }

        public Node<T> getChild(int index) {
            return child[index];
        }

        public Node<T> getLeftChild() {
            return child[0];
        }

        public Node<T> getRightChild() {
            return child[child.length - 1];
        }

        public void setChild(Node<T>[] child) {
            this.child = child;
        }

        public T[] getKeys() {
            return keys;
        }

        public T getKey(int index){
            return keys[index];
        }

        public void setKeys(T... keys) {
            this.keys = keys;
        }

        public int getKeysCnt() {
            return keys.length;
        }

        public int getChildCnt() {
            return child.length;
        }

        public T getFirstKey() {
            return keys[0];
        }

        public T getLastKey() {
            return keys[keys.length - 1];
        }

        //endregion


        public boolean isLeaf() {
            return child.length == 0;
        }

        public void addKey(T key) {
            T[] newKeys = (T[]) new Object[keys.length + 1];

            int i = 0;

            for (i = 0; i < keys.length; i++) {
                if (_comp.compare(key, keys[i]) <= 0) {
                    newKeys[i] = key;
                    break;
                } else {
                    newKeys[i] = keys[i];
                }
            }

            if (i == keys.length) {
                newKeys[newKeys.length - 1] = key;
            } else {
                System.arraycopy(keys, i, newKeys, i + 1, keys.length - i);
            }
            keys = newKeys;
        }

        public boolean removeKey(T key) {
            T[] newKeys = (T[]) new Object[keys.length - 1];

            int i = 0;
            for (i = 0; i < keys.length; i++) {
                if (_comp.compare(keys[i], key) == 0) {
                    break;
                }
                newKeys[i] = keys[i];
            }
            if (i == keys.length) return false;

            System.arraycopy(keys, i + 1, newKeys, i, keys.length - (i + 1));

            keys = newKeys;
            return true;
        }

        public void addChild(Node<T> newChild) { // todo binserach ?
            newChild.setRoot(this);
            Node<T>[] newChildArr = new Node[child.length + 1];
            if (child.length == 0) {
                newChildArr[0] = newChild;
                child = newChildArr;
                return;
            }
            int index = 0;

            T lastKeyNewChild = newChild.getLastKey();
            T firstKeyNewChild = newChild.getFirstKey();

            if (_comp.compare(lastKeyNewChild, child[0].getFirstKey()) <= 0) {
                newChildArr[0] = newChild;
                System.arraycopy(child, 0, newChildArr, 1, child.length);

            } else if (_comp.compare(firstKeyNewChild, child[child.length - 1].getLastKey()) >= 0) {
                System.arraycopy(child, 0, newChildArr, 0, child.length);
                newChildArr[newChildArr.length - 1] = newChild;
            } else {
                for (index = 1; index < child.length; index++) {
                    T lastKeyOldLastChild = child[index-1].getLastKey();
                    T firstKeyOldCurrChild = child[index].getFirstKey();
                    int leftComp = _comp.compare(firstKeyNewChild, lastKeyOldLastChild);
                    int rightComp = _comp.compare(lastKeyNewChild, firstKeyOldCurrChild);

                    if (leftComp >= 0 && rightComp <= 0 ) {
                        System.arraycopy(child, 0, newChildArr, 0, index);
                        newChildArr[index] = newChild;
                        System.arraycopy(child, index, newChildArr, index + 1, child.length - index);
                        break;
                    }
                }
            }
            child = newChildArr;
        }

        public boolean removeChild(Node<T> removeChild) { // remove without recursive dipping
            Node<T>[] newChild = new Node[this.child.length - 1];
            int i = 0;
            for (i = 0; i < child.length; i++) {
                if (removeChild.equals(child[i])) {
                    child[i].setRoot(null);
                    System.arraycopy(child, 0, newChild, 0, i);
                    System.arraycopy(child, i + 1, newChild, i, child.length - i - 1);
                    child = newChild;
                    return true;
                }
            }
            return false;
        }

        public boolean equals(Node<T> node) {
            if (node.keys.length != this.keys.length) return false;
            for (int i = 0; i < this.keys.length; i++) {
                if (_comp.compare(node.keys[i], this.keys[i]) != 0) return false;
            }
            return true;
        }

        public void clean() {
            root = null;
            child = new Node[0];
            keys = (T[]) new Object[0];
        }

        private boolean checkSortKeys() {
            if (keys.length <= 1) return true;

            for (int i = 1; i < keys.length - 1; i++) {
                if (_comp.compare(keys[i - 1], keys[i]) == 1) return false;
            }
            return true;
        }

        @Override
        public String toString() {
            String outLine = "{";
            for (int i = 0; i < keys.length; i++) {
                outLine += keys[i];
                if (i != keys.length - 1) outLine += ", ";
            }
            outLine += "}";
            return outLine;

        }

    }

    public BTree(int order, Comparator<T> comparator) {
        this.comparator = comparator;
        ORDER = order;
        MAX_KEY = ORDER - 1;
        MIN_KEY = ORDER / 2 - 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
        return depth;
    }

    private void reculcDepth() {
        depth = culcDepthRec(root, -1);
    }

    private int culcDepthRec(Node root, int depth) { //todo without rec
        if (root == null) return depth;
        int[] ds = new int[root.getChildCnt()];
        for (int i = 0; i < ds.length; i++) {
            ds[i] = culcDepthRec(root.getChild(i), depth + 1);
        }
        if (ds.length == 0) {
            return depth + 1;
        }

        return max(ds);
    }

    private int max(int[] val) {
        int max = Integer.MIN_VALUE;
        for (int i : val) {
            if (i > max) max = i;
        }
        return max;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clean() {
        root = null;
        size = 0;
        depth = 0;
    }

    @Override
    public void add(T o) {
        if (size == 0) {
            root = new Node<T>(o);
            size = 1;
            depth = 0;
            return;
        }

        Node<T> addingLeaf = findLeafToAdd(root, o);
        addingLeaf.addKey(o);

        balance(addingLeaf);
        size++;
        reculcDepth();

    }

    @Override
    public void addAll(Collection<T> c) {
        for (T t : c) add(t);
    }

    @Override
    public boolean remove(T o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<T> c) {
        boolean res = true;
        for (T t : c) if (!remove(t)) res = false;
        return res;
    }

    @Override
    public boolean isContain(T o) {
        return false;
    }

    @Override
    public BTree<T> copy() {
        return null;
    }

    private void balance(Node<T> node) {
        if (node.getKeysCnt() > MAX_KEY) {
            Node<T>[] separ = separateByMedian(node);
            Node<T> left = separ[0];
            Node<T> right = separ[1];

            T rebalanceKey = left.getLastKey();
            left.removeKey(rebalanceKey);

            if (node == root) {
                root = new Node<>(rebalanceKey);
                root.addChild(left);
                root.addChild(right);
                return;
            }
            Node<T> root = node.getRoot();
            root.addKey(rebalanceKey);

            root.removeChild(node);
            root.addChild(left);
            root.addChild(right);

            node.clean();
            balance(root);
        }
    }

    private Node<T>[] separateByMedian(Node<T> node) {
        int median = node.getKeysCnt() / 2 - 1;

        Node<T> left = new Node<>();
        Node<T> right = new Node<>();

        left.setRoot(node.root);
        right.setRoot(node.root);
        T[] nodeKeys = node.getKeys();
        for (int i = 0; i < nodeKeys.length; i++) {
            if (i <= median) {
                left.addKey(nodeKeys[i]);
            } else {
                right.addKey(nodeKeys[i]);
            }
        }
        if (!node.isLeaf()) {
            Node[] children = node.getChilds();
            for (int i = 0; i < children.length; i++) {
                if (i < left.getKeysCnt()) {
                    left.addChild(children[i]);
                } else {
                    right.addChild(children[i]);
                }
            }
        }
        return new Node[]{left, right};
    }

    public Node<T> findLeafToAdd(Node<T> node, T addedObj) {
        if (node.isLeaf()) return node;

        if (comparator.compare(addedObj, node.getFirstKey()) <= 0) {
            return findLeafToAdd(node.getLeftChild(), addedObj);
        } else if (comparator.compare(addedObj, node.getLastKey()) >= 0) {
            return findLeafToAdd(node.getRightChild(), addedObj);
        } else {
            for (int i = 0; i < node.getChildCnt(); i++) {

                int left_c = comparator.compare(addedObj, node.getChild(i).getFirstKey());
                int right_c = comparator.compare(addedObj, node.getChild(i).getLastKey());
                int next_c = 2;
                if(i != node.getChildCnt() -1){
                    next_c = comparator.compare(addedObj, node.getChild(i+1).getFirstKey());
                }

                if (left_c >= 0 && right_c <= 0) {
                    return findLeafToAdd(node.getChild(i), addedObj);
                }else if(right_c >= 0 && next_c <= 0){
                    try{
                        int index1 =
                    }
                    return findLeafToAdd(node.getChild(i), addedObj);
                }
            }
        }
        return null;
    }


    public boolean test() {
        int depth = depth();
        int size = size();
        boolean res = true;
//        HashMap<Node<T>, Integer> _nodes = getNodeMap();
        ArrayList<Node<T>> nodes = getNodeList();
        double min = log(ORDER, size);
        double max = log((int)(ORDER/2), ((size-1)/2));

        if(log(ORDER, size)  -1 > depth || log((int)(ORDER/2), ((size-1)/2)) +1  < depth ){
            System.out.println("ERROR DEPTH (ORDER)");
            res = false;
        }
        if(!checkDepth()){
            System.out.println("ERROR DEPTH");
            res = false;
        }

        for (Node<T> node : nodes) {
            if(node.getKeysCnt() +1 != node.getChildCnt() && !node.isLeaf()){
                System.out.println("ERROR NODE Child numbers : "+ node);
                res = false;
            }
            if(!node.checkSortKeys()){
                System.out.println("ERROR NODE key sort : "+ node);
                res = false;
            }
        }

        if(!checkSort()){
            System.out.println("ERROR SORT");
            res = false;
        }

        return res;

    }

    private boolean checkDepth(){
        return getMinDepth() == depth();
    }

    private int getMinDepth(){
        return calcMinDepthRec(root, -1);
    }

    private int calcMinDepthRec(Node root, int depth) { //todo without rec
        if (root == null) return depth;
        int[] ds = new int[root.getChildCnt()];
        for (int i = 0; i < ds.length; i++) {
            ds[i] = culcDepthRec(root.getChild(i), depth + 1);
        }
        if (ds.length == 0) {
            return depth + 1;
        }

        return min(ds);
    }

    private int min(int[] val) {
        int min = Integer.MAX_VALUE;
        for (int i : val) {
            if (i < min) min = i;
        }
        return min;
    }

    private boolean checkSort(){
        T[] keys = getSortKeys();
        for (int i = 1; i < keys.length; i++) {
            if(comparator.compare(keys[i-1], keys[i]) > 0) return false;
        }
        return true;
    }

    private T[] getSortKeys(){
        ArrayList<T> arrayList = new ArrayList<>();
        fillKeys(arrayList, root);
        return (T[])arrayList.toArray();

    }

    private  void  fillKeys(ArrayList<T> keys, Node<T> root){
        if(root.isLeaf()) {
            keys.addAll(Arrays.asList(root.getKeys()));
            return;
        }
        for (int i = 0; i < root.getChilds().length; i++) {
            fillKeys(keys, root.getChild(i));
            if(i < root.getKeys().length){
                keys.add(root.getKey(i));
            }
        }

    }


    private ArrayList<Node<T>> getNodeList(){
        ArrayList<Node<T>> list = new ArrayList<>();
        fillList(list, root);
        return list;
    }

    private void fillList(ArrayList<Node<T>> list, Node<T> node){
        list.add(node);
        if(node.isLeaf()) return;
        for (Node<T> tNode : node.child) {
            fillList(list, tNode);
        }
    }

    private HashMap<Node<T>, Integer>  getNodeMap(){
        HashMap<Node<T>, Integer> nodeMap = new HashMap<>();
        fillMap(nodeMap, root, 0);
        return nodeMap;
    }

    private void fillMap(HashMap<Node<T>, Integer> map, Node<T> root, int depth){
        map.put(root,depth);
        if(!root.isLeaf()) {
            for (Node node : root.child) {
                fillMap(map, node, depth + 1);
            }
        }
    }

    private double log(double a, double b){
        return Math.log(b)/Math.log(a);
    }
}
