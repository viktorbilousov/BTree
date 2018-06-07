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
        private Node<T>[] children = new Node[0];
        private T[] keys = (T[]) new Object[0];
        private Comparator<T> _comp = (Comparator<T>) comparator;

        public Node(T[] keys, Node<T> root, Node<T>[] children) {
            this.root = root;
            this.children = children;
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

        public Node<T>[] getChildren() {
            return children;
        }

        public Node<T> getChild(int index) {
            return children[index];
        }

        public Node<T> getLeftChild() {
            return children[0];
        }

        public Node<T> getRightChild() {
            return children[children.length - 1];
        }

        public void setChildren(Node<T>[] children) {
            this.children = children;
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

        public int getChildrenCnt() {
            return children.length;
        }

        public T getFirstKey() {
            return keys[0];
        }

        public T getLastKey() {
            return keys[keys.length - 1];
        }

        public int indexOfKey(T key){
            for (int i = 0; i < keys.length; i++) {
                if(_comp.compare(key, keys[i]) == 0) return i;
            }
            return -1;
        }

        public int indexOfChild(Node<T> child){
            for (int i = 0; i < this.children.length; i++) {
                if(children[i].equals(child)) return i;
            }
            return -1;
        }

        //endregion


        public boolean isLeaf() {
            return children.length == 0;
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
            Node<T>[] newChildArr = new Node[children.length + 1];
            if (children.length == 0) {
                newChildArr[0] = newChild;
                children = newChildArr;
                return;
            }
            int index = 0;

            T lastKeyNewChild = newChild.getLastKey();
            T firstKeyNewChild = newChild.getFirstKey();

            if (_comp.compare(lastKeyNewChild, children[0].getFirstKey()) <= 0) {
                newChildArr[0] = newChild;
                System.arraycopy(children, 0, newChildArr, 1, children.length);

            } else if (_comp.compare(firstKeyNewChild, children[children.length - 1].getLastKey()) >= 0) {
                System.arraycopy(children, 0, newChildArr, 0, children.length);
                newChildArr[newChildArr.length - 1] = newChild;
            } else {
                for (index = 1; index < children.length; index++) {
                    T lastKeyOldLastChild = children[index-1].getLastKey();
                    T firstKeyOldCurrChild = children[index].getFirstKey();
                    int leftComp = _comp.compare(firstKeyNewChild, lastKeyOldLastChild);
                    int rightComp = _comp.compare(lastKeyNewChild, firstKeyOldCurrChild);

                    if (leftComp >= 0 && rightComp <= 0 ) {
                        System.arraycopy(children, 0, newChildArr, 0, index);
                        newChildArr[index] = newChild;
                        System.arraycopy(children, index, newChildArr, index + 1, children.length - index);
                        break;
                    }
                }
            }
            children = newChildArr;
        }

        public boolean removeChild(Node<T> removeChild) { // remove without recursive dipping
            Node<T>[] newChild = new Node[this.children.length - 1];
            int i = 0;
            for (i = 0; i < children.length; i++) {
                if (removeChild.equals(children[i])) {
                    children[i].setRoot(null);
                    System.arraycopy(children, 0, newChild, 0, i);
                    System.arraycopy(children, i + 1, newChild, i, children.length - i - 1);
                    children = newChild;
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
            children = new Node[0];
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
       if(order < 3) throw new IllegalArgumentException("Order < 3!");
        ORDER = order;
        MAX_KEY = ORDER - 1;
        MIN_KEY = (ORDER > 3 )? ORDER / 2 - 1 : 1;
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
        int[] ds = new int[root.getChildrenCnt()];
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
        Node<T> removeNode = getNode(o);
        if(removeNode == null) return false;

        if(removeNode.isLeaf()) {
            removeNode.removeKey(o);
            balance(removeNode);
        }else {
            removeFromEdge(o, removeNode);
        }

        size--;
        reculcDepth();
        return true;
    }

    @Override
    public boolean removeAll(Collection<T> c) {
        boolean res = true;
        for (T t : c) if (!remove(t)) res = false;
        return res;
    }

    @Override
    public boolean isContain(T o) {
        return getNode(o) != null;
    }

    public Node<T> getNode(T o){
        Node<T> pointer = root;

        while (true){
            if(comparator.compare(o, pointer.getFirstKey()) < 0) {
                if(pointer.isLeaf()) return null;
                pointer = pointer.getLeftChild();
                continue;
            }
            if(comparator.compare(o, pointer.getLastKey()) > 0){
                if(pointer.isLeaf()) return null;
                pointer = pointer.getRightChild();
                continue;
            }
            if(pointer.getKeysCnt() == 1 ){
                if(comparator.compare(o, pointer.getFirstKey()) != 0) return null;
                return pointer;
            }

            for (int i = 0; i < pointer.getKeysCnt()-1; i++) {
                int c_left =  comparator.compare(o, pointer.getKey(i));
                int c_right = comparator.compare(o, pointer.getKey(i+1));

                if(c_left == 0 || c_right == 0) return pointer;

                if(c_left > 0 && c_right < 0){
                    if(pointer.isLeaf()) return null;

                    pointer = pointer.getChild(i+1);
                    break;
                }
            }
        }
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
        } else if (node.getKeysCnt() < MIN_KEY) {
            if (node == root) return;

            if (!tryToShift(node)) {
                Node<T> root = node.root;
                int indexChild = root.indexOfChild(node);
                int mergeNodeIndex = (indexChild == 0) ? indexChild + 1 : indexChild - 1; //nightbar
                T shiftKey = (indexChild == 0) ? root.getKey(indexChild) : root.getKey(indexChild - 1); // in root

                Node<T> neighbor = root.getChild(mergeNodeIndex);
                Node<T> newNode = mergeNodes(node, neighbor);
                newNode.addKey(shiftKey);
                root.removeKey(shiftKey);

                if (root.getKeysCnt() == 0 && root == this.root) {
                    this.root = newNode;
                    root = this.root;
                } else {
                    root.removeChild(neighbor);
                    root.removeChild(node);
                    root.addChild(newNode);
                }
                node.clean();
                balance(root);
            }
        }
    }


    private void removeFromEdge(T o, Node<T> node){

        int indexKey = node.indexOfKey(o);
        Node<T> l_max = node.getChild(indexKey);
        while (!l_max.isLeaf()) l_max = l_max.getRightChild();
        T max = l_max.getLastKey();

        node.removeKey(o);
        node.addKey(max);
        l_max.removeKey(max);

        balance(l_max);
    }



    private boolean tryToShift(Node<T> node){
        if(node == root) return false;
        Node<T> root = node.getRoot();
        int index = root.indexOfChild(node);
        if(index > 0  && root.getChild(index-1).getKeysCnt() > MIN_KEY){
            rightShift(root, index);
            return true;
        }else if(index < root.getChildrenCnt()-1 && root.getChild(index +1).getKeysCnt() > MIN_KEY ){
            leftShift(root, index);
            return true;
        }
        return false;

    }



    private void leftShift(Node<T> root, int indexChild){

        Node<T> neighbor = root.getChild(indexChild +1);
        Node<T> node = root.getChild(indexChild);

        T donorShiftedKey = neighbor.getFirstKey();
        T rootShiftedKey = root.getKey(indexChild);

        node.addKey(rootShiftedKey);
        root.removeKey(rootShiftedKey);

        root.addKey(donorShiftedKey);
        neighbor.removeKey(donorShiftedKey);

        if(!neighbor.isLeaf()){
            Node<T> neighborLeftChild = neighbor.getLeftChild();
            neighbor.removeChild(neighborLeftChild);
            node.addChild(neighborLeftChild);
        }

    }

    private void rightShift(Node<T>root, int indexChild){
        Node<T> neighbor = root.getChild(indexChild - 1);
        Node<T> node = root.getChild(indexChild);

        T donorShiftedKey = neighbor.getLastKey();
        T rootShiftedKey = root.getKey(indexChild - 1);


        node.addKey(rootShiftedKey);
        root.removeKey(rootShiftedKey);

        root.addKey(donorShiftedKey);
        neighbor.removeKey(donorShiftedKey);

        if(!neighbor.isLeaf()){
            Node<T> neighborRightChild = neighbor.getRightChild();
            neighbor.removeChild(neighborRightChild);
            node.addChild(neighborRightChild);
        }

    }

    private Node<T>[] separateByMedian(Node<T> node) {
        int median = node.getKeysCnt() / 2 ;

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
            Node<T>[] children = node.getChildren();
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

    private Node<T> mergeNodes(Node<T> a, Node<T> b){
        Node<T> newNode = new Node<>();
        for (T t : a.getKeys()) {
            newNode.addKey(t);
        }
        for (T t : b.getKeys()) {
            newNode.addKey(t);
        }
        for (int i = 0; i < a.getChildrenCnt(); i++) {
            newNode.addChild(a.getChild(i));
        }
        for (int i = 0; i < b.getChildrenCnt(); i++) {
            newNode.addChild(b.getChild(i));
        }

        return newNode;
    }

    public Node<T> findLeafToAdd(Node<T> node, T addedObj) {
        if (node.isLeaf()) return node;

        if (comparator.compare(addedObj, node.getFirstKey()) <= 0) {
            return findLeafToAdd(node.getLeftChild(), addedObj);
        } else if (comparator.compare(addedObj, node.getLastKey()) >= 0) {
            return findLeafToAdd(node.getRightChild(), addedObj);
        } else {
            for (int i = 0; i < node.getKeysCnt()-1; i++) {

                int left_c = comparator.compare(addedObj, node.getKey(i));
                int right_c = comparator.compare(addedObj, node.getKey(i+1));

                if (left_c >= 0 && right_c <= 0) {
                    return findLeafToAdd(node.getChild(i+1), addedObj);
                }
            }
        }
        return null;
    }

    public boolean test(){
       return new Tester().test();
    }

    private class Tester{
        public boolean test() {
            int depth = depth();
            int size = size();
            boolean res = true;
//        HashMap<Node<T>, Integer> _nodes = getNodeMap();
            ArrayList<Node<T>> nodes = getNodeList();
            double min = log(ORDER, size);
            double max = log((int)(ORDER/2), ((size-1)/2));
            if(size > 2)
                if(log(ORDER, size)  -1 > depth || log((int)(ORDER/2), ((size-1)/2)) +1  < depth ){
                    System.out.println("ERROR DEPTH (ORDER)");
                    res = false;
                }
            if(!checkDepth()){
                System.out.println("ERROR DEPTH");
                res = false;
            }

            for (Node<T> node : nodes) {
                if(node.getKeysCnt() +1 != node.getChildrenCnt() && !node.isLeaf()){
                    System.out.println("ERROR NODE Child numbers : "+ node);
                    res = false;
                }
                if(!node.checkSortKeys()){
                    System.out.println("ERROR NODE key sort : "+ node);
                    res = false;
                }
                if((node.getKeysCnt() < MIN_KEY && node != root)
                        || node.getKeysCnt() > MAX_KEY ){
                        System.out.println("ERROR NODE key cnt : "+ node);
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
            int[] ds = new int[root.getChildrenCnt()];
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
            for (int i = 0; i < root.getChildren().length; i++) {
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
            for (Node<T> tNode : node.children) {
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
                for (Node node : root.children) {
                    fillMap(map, node, depth + 1);
                }
            }
        }

        private double log(double a, double b){
            return Math.log(b)/Math.log(a);
        }
    }

}
