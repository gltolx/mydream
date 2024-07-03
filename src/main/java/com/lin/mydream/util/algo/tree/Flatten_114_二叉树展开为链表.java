package com.lin.mydream.util.algo.tree;

import com.lin.mydream.util.algo.base.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/7/1 20:37
 * @desc
 */
public class Flatten_114_二叉树展开为链表 {

    public static void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n2 = new TreeNode(2);
        TreeNode n3 = new TreeNode(3);
        TreeNode n4 = new TreeNode(4);
        TreeNode n5 = new TreeNode(5);
        TreeNode n6 = new TreeNode(6);
        n1.left = n2; n1.right = n5;
        n2.left = n3; n2.right = n4;
        n5.left = null; n5.right = n6;

        // 先序
        Stack<TreeNode> stack = new Stack<>();
        stack.push(n1);
        System.out.print("pre：");
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            if (pop != null) {
                // 收集输出
                System.out.print(pop.val + " -> ");
                stack.push(pop.right);
                stack.push(pop.left);
            }
        }
        System.out.println();


        // 中序
        Stack<TreeNode> stack2 = new Stack<>();
        stack2.push(n1);
        System.out.print("mid：");
        while (!stack2.isEmpty()) {
            TreeNode pop = stack2.pop();
            if (pop != null) {
                stack2.push(pop.right);
                // 收集输出
                System.out.print(pop.val + " -> ");
                stack2.push(pop.left);

            }
        }

        System.out.println();

        // 后序
        Stack<TreeNode> stack4 = new Stack<>();
        LinkedList<Integer> res4 = new LinkedList<>();
        stack4.push(n1);
        System.out.print("post2：");
        while (!stack4.isEmpty()) {
            TreeNode pop = stack4.pop();
            if (pop != null) {
                // 收集输出
                res4.addFirst(pop.val);
                stack4.push(pop.left);
                stack4.push(pop.right);
            }
        }

        System.out.println(res4);

//        TreeNode resNode = new Flatten_114_二叉树展开为链表().traverse_flatten(n1);
//        System.out.println();
//        while (resNode != null) {
//            System.out.print(resNode.val + " -> ");
//            resNode = resNode.right;
//        }
    }

    public static List<Integer> postorderTraversal(TreeNode root) {
        LinkedList<Integer> result = new LinkedList<>();
        if (root == null) return result;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.addFirst(node.val);

            // 先压入右子树，再压入左子树，这样出栈时就是左子树先处理
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }

        return result;
    }


    private TreeNode prev;
    public void traverse_flatten2(TreeNode root) {
        if (root == null) {
            return;
        }

        TreeNode left = root.left;
        TreeNode right = root.right;
        root.left = null;
        if (prev != null) {
            prev.right = root;
        }
        prev = root;
        traverse_flatten(left);
        traverse_flatten(right);

    }

    public TreeNode traverse_flatten(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode flattenedLeft = traverse_flatten(root.left);
        TreeNode flattenedRight = traverse_flatten(root.right);

        root.left = null;
        if (flattenedLeft != null && flattenedRight != null) {
            flattenedLeft.left = null;
            flattenedRight.left = null;
            root.right = flattenedLeft;
            TreeNode rightest = rightest(flattenedLeft);
            rightest.right = flattenedRight;
        } else if (flattenedLeft != null) {
            flattenedLeft.left = null;
            root.right = flattenedLeft;
        }
        return root;
    }

    public TreeNode rightest(TreeNode cursor) {
        while (true) {
            if (cursor.right == null) {
                return cursor;
            }
            cursor = cursor.right;
        }
    }


    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        flatten(root.left);
        flatten(root.right);
        TreeNode flattenedLeft = root.left;
        TreeNode flattenedRight = root.right;
        if (flattenedLeft != null && flattenedRight != null) {
            root.right = flattenedLeft;
            TreeNode rightest = rightest(flattenedLeft);
            rightest.right = flattenedRight;
        } else if (flattenedLeft != null) {
            root.right = flattenedLeft;
        }
    }

}
