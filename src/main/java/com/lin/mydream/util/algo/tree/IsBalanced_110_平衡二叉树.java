package com.lin.mydream.util.algo.tree;

//import com.lin.mydream.util.algo.base.TreeNode;

import com.lin.mydream.util.algo.base.TreeNode;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/28 11:31
 * @desc
 */
public class IsBalanced_110_平衡二叉树 {

    private boolean notBalanced;

    public boolean isBalanced(TreeNode root) {

        current_height_traverse(root, 0);
        return !notBalanced;
    }

    public int current_height_traverse(TreeNode root, int h) {
        if (root == null) {
            return h;
        }

        int leftHeight = current_height_traverse(root.left, h + 1);
        int rightHeight = current_height_traverse(root.right, h + 1);

        if (Math.abs(leftHeight - rightHeight) > 1) {
            notBalanced = true;
        }
        return Math.max(leftHeight, rightHeight);
    }

    public int current_height_traverse2(TreeNode root) {
        if (root == null || notBalanced) {
            return 0;
        }

        int leftHeight = current_height_traverse2(root.left);
        int rightHeight = current_height_traverse2(root.right);

        if (Math.abs(leftHeight - rightHeight) > 1) {
            notBalanced = true;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }
}
