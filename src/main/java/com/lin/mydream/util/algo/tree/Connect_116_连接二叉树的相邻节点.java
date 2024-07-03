package com.lin.mydream.util.algo.tree;

import com.lin.mydream.util.algo.base.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/29 16:02
 * @desc
 */
public class Connect_116_连接二叉树的相邻节点 {

    public static void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n2 = new TreeNode(2);
        TreeNode n3 = new TreeNode(3);
        TreeNode n4 = new TreeNode(4);
        TreeNode n5 = new TreeNode(5);
        TreeNode n6 = new TreeNode(6);
        TreeNode n7 = new TreeNode(7);
        n1.left = n2; n1.right = n3;
        n2.left = n4; n2.right = n5;
        n3.left = n6; n3.right = n7;

        TreeNode afterConnect = new Connect_116_连接二叉树的相邻节点().connect(n1);

        List<List<Integer>> res = new ArrayList<>();
        LinkedList<TreeNode> q = new LinkedList<>();
        q.add(n1);
        while (!q.isEmpty()) {

            int size = q.size();
            List<Integer> list = new ArrayList<>();
            List<Integer> nextList = new ArrayList<>();
            TreeNode peek = q.peek();
            while (peek != null) {
                nextList.add(peek.val);
                peek = peek.next;
            }

            System.out.println(nextList);
            while (size-- > 0) {
                TreeNode poll = q.poll();
                list.add(poll.val);
                if (poll.left != null) {
                    q.add(poll.left);
                }
                if (poll.right != null) {
                    q.add(poll.right);
                }
            }
            res.add(list);
        }
        System.out.println(res);
    }
    public TreeNode connect(TreeNode root) {
        if (root == null) {
            return null;
        }

        traverse(root.left, root.right);

        return root;
    }

    public void traverse(TreeNode l, TreeNode r) {
//        if (l != null) {
//            l.next = r;
//            if (l.left != null) {
//                l.left.next = l.right;
//            }
//            if (l.right != null) {
//                l.right.next = r.left;
//            }
//
//        }
//        if (r != null) {
//            if (r.left != null) {
//                r.left.next = r.right;
//            }
//        }
        if (l == null || r == null) {
            return;
        }
        l.next = r;
            traverse(l.left, l.right);
            traverse(l.right, r.left);
            traverse(r.left, r.right);

    }

}
