package com.company.tree;

public class TreeNode<T> {
    private T obj;
    private int depth;

    public TreeNode(T obj, int depth) {
        this.obj = obj;
        this.depth = depth;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode<?> treeNode = (TreeNode<?>) o;

        if (depth != treeNode.depth) return false;
        return obj.equals(treeNode.obj);
    }

    @Override
    public String toString() {

        if(obj == null) return  "null="+depth;

        return obj.toString() +"="+depth;
    }
}
