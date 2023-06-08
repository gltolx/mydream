package com.lin.mydream.util.algo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ViewOfTree {

    /**
     * 获取该树的左视图，
     *
     * @param root 根结点
     * @return 按顺序输出左视图列表集合
     */
    public static List<Node> leftView(Node root) {
        List<Node> result = new LinkedList<>();
        Queue<Node> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size();

            for (int i = 0; i < size; i++) {
                Node node = q.poll();
                if (i == 0) {
                    // 第一个元素
                    result.add(node);
                }

                if (node.left != null) {
                    q.offer(node.left);
                }
                if (node.right != null) {
                    q.offer(node.right);
                }
            }

        }
        return result;
    }

    /**
     * 获取该树的右视图
     *
     * @param root 根结点
     * @return 按顺序输出右视图列表集合
     */
    public static List<Node> rightView(Node root) {
        List<Node> result = new LinkedList<>();
        Queue<Node> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size();

            for (int i = 0; i < size; i++) {
                Node node = q.poll();
                if (i == size - 1) {
                    // 最后一个元素
                    result.add(node);
                }

                if (node.left != null) {
                    q.offer(node.left);
                }
                if (node.right != null) {
                    q.offer(node.right);
                }
            }

        }
        return result;
    }

    /**
     * 获取该树的顶视图
     * <p>
     * 1
     * 2     3
     * 5     6
     * 7     8
     * <p>
     * ==> [2,1,3,6]
     *
     * @param root 根结点
     * @return 按顺序输出顶视图列表集合
     */
    public static List<Node> topView(Node root) {
        // 左边：优先取最左最高；右边：优先取最右最高
        TreeMap<Integer, Pair<Node, Integer>> treemap = new TreeMap<>();
        topView(root, 0, 0, treemap);
        return treemap.values().stream().map(Pair::getLeft).collect(Collectors.toList());
    }

    /**
     * 二叉树的顶视图
     *
     * @param root    结点
     * @param dist    当前与根结点的水平距离，设定根结点左边为负，右边为正，从左到右
     * @param lv      当前的层级
     * @param treemap 结果map，key是水平距离，lvalue是结点，rvalue是层级lv
     */
    public static void topView(Node root, Integer dist, Integer lv, Map<Integer, Pair<Node, Integer>> treemap) {

        if (root == null) {
            return;
        }
        Pair<Node, Integer> p = treemap.get(dist);
        if (p == null || lv < p.getRight()) {
            // 如果当前水平距离对应的数据不存在，或当前相同水平距离下层级更低，则插入或覆盖
            treemap.put(dist, Pair.of(root, lv));
        }
        topView(root.left, dist - 1, lv + 1, treemap);
        topView(root.right, dist + 1, lv + 1, treemap);

    }

    /**
     * 二叉树的底视图（递归子方法）
     *
     * @param root    当前结点
     * @param dist    当前结点与根结点的水平距离
     * @param lv      当前结点的层级
     * @param treemap 自然散列map
     */
    public static void bottomView(Node root, Integer dist, Integer lv, Map<Integer, Pair<Node, Integer>> treemap) {
        if (root == null) {
            return;
        }
        Pair<Node, Integer> p = treemap.get(dist);
        if (p == null || lv > p.getRight()) {
            // 如果当前dist在map中不存在，或当前dist所对应的lv小于当前lv，就插入或覆盖当前node
            treemap.put(dist, Pair.of(root, lv));
        }
        // 子结点递归：
        // 左子结点，水平距离-1，层级+1；右子结点，水平距离+1，层级+1；
        bottomView(root.left, dist - 1, lv + 1, treemap);
        bottomView(root.right, dist + 1, lv + 1, treemap);
    }

    /**
     * 二叉树的底视图
     *
     * @param root 根结点
     * @return 有序的底视图集合
     */
    public static List<Node> bottomView(Node root) {

        TreeMap<Integer, Pair<Node, Integer>> treemap = new TreeMap<>();
        bottomView(root, 0, 0, treemap);

        return treemap.values().stream().map(Pair::getLeft).collect(Collectors.toList());
    }


    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.left.right = new Node(9);
        /*
         *      1
         *    2    3
         *  4   56
         *         9
         */
        // 左视图
        leftView(root).stream().map(Node::getVal).forEach(System.out::print);
        System.out.println();
        // 右视图
        rightView(root).stream().map(Node::getVal).forEach(System.out::print);
        System.out.println();
        // 顶视图
        topView(root).stream().map(Node::getVal).forEach(System.out::print);
        System.out.println();
        // 底视图
        bottomView(root).stream().map(Node::getVal).forEach(System.out::print);
        System.out.println();

    }

    @Getter
    @Setter
    public static class Node {
        int val;
        public Node left;
        Node right;

        public Node(int val) {
            this.val = val;
        }


        public boolean hasNoChild() {
            return this.left == null && this.right == null;
        }

        public boolean hasOneLeftChild() {
            return this.left != null && this.right == null;
        }

        public boolean hasOneRightChild() {
            return this.left == null && this.right != null;
        }

        public boolean hasTwoChild() {
            return this.left != null && this.right != null;
        }
    }

}


