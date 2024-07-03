package com.lin.mydream.util.algo.tree;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lin.mydream.util.algo.base.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/25 17:50
 * @desc
 */
public class FrontInBack_二叉树前中后序遍历 {

    private List<Integer> res = new LinkedList<>();

    public static void main(String[] args) {
        TreeNode left2 = new TreeNode();
        left2.val = 7;
        TreeNode right2 = new TreeNode();
        right2.val = 15;

        TreeNode left2_2 = new TreeNode();
        left2_2.val = 18;
        TreeNode right2_2 = new TreeNode();
        right2_2.val = 23;

        TreeNode left1 = new TreeNode();
        left1.val = 9;
        left1.left = left2;
        left1.right = right2;

        TreeNode right1 = new TreeNode();
        right1.val = 20;
        right1.left = left2_2;
        right1.right = right2_2;

        TreeNode root = new TreeNode();
        root.val = 3;
        root.left = left1;
        root.right = right1;

        List<List<Integer>> ret = new TreeLevelTravel_bfs层序遍历().levelOrder(root);
        System.out.println(JSON.toJSONString(ret, SerializerFeature.PrettyFormat));

        System.out.println(JSON.toJSONString(new TreeLevelTravel_bfs层序遍历().dfs(root)));
        System.out.println(JSON.toJSONString(new FrontInBack_二叉树前中后序遍历().preOrderTraverse_while(root)));
//        preOrderTraverse_while()
        System.out.println("-------");
        System.out.println(new FrontInBack_二叉树前中后序遍历().maxHeight(root));
        System.out.println(new FrontInBack_二叉树前中后序遍历().maxHeight_break(root));
        System.out.println("-------");
        System.out.println(new FrontInBack_二叉树前中后序遍历().maxDiameter(root));
//        System.out.println(new FrontInBack_二叉树前中后序遍历().maxDiameter_break(root));
        System.out.println("----->");
        System.out.println(new FrontInBack_二叉树前中后序遍历().maxPathSum(root));
    }
    public List<Integer> preOrderTraverse_while(TreeNode root) {

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode pop = queue.poll();
            if (pop == null) {
                continue;
            }
            res.add(pop.val);
            queue.offer(pop.left);
            queue.offer(pop.right);
        }
        return res;
    }


    /**
     * 遍历思路解题 - 不带有返回值
     */
    public List<Integer> preOrderTraverse_travel(TreeNode root) {

        traverse(root);
        return res;
    }

    /**
     * 遍历思路解题 - 不带有返回值
     */
    public void traverse(TreeNode root) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        preOrderTraverse_travel(root.left);
        preOrderTraverse_travel(root.right);
    }


    /**
     * 分解思路解题 - 带有返回值
     * 1. 把问题分解为子问题
     */
    public List<Integer> preOrderTraverse_break(TreeNode root) {
        List<Integer> res = new LinkedList<>();
        if (root == null) {
            return res;
        }
        res.add(root.val);
        res.addAll(preOrderTraverse_break(root.left));
        res.addAll(preOrderTraverse_break(root.right));
        return res;
    }


    int currentHeight = 0;
    int maxHeight = 0;

    public int maxHeight(TreeNode root) {
        maxHeight_traverse(root);
        return maxHeight;
    }
    public void maxHeight_traverse(TreeNode root) {
        if (root == null) {
            return;
        }
        currentHeight++;
        maxHeight = Math.max(maxHeight, currentHeight);
        maxHeight_traverse(root.left);
        maxHeight_traverse(root.right);
        currentHeight--;
    }
    // 分解思路：当前root的高度 = Max(左子树的高度, 右子树的高度) + 1
    public int maxHeight_break(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxHeight_break(root.left), maxHeight_break(root.right)) + 1;
    }


    private int maxHeight2;
    public int maxDiameter(TreeNode root) {
        maxDiameter_travel(root);
        return maxHeight2;

    }

    // 求当前根节点的最大深度 = 1 + Max(左子树最大深度, 右子树最大深度)
    public int maxDiameter_travel(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int l = maxDiameter_travel(root.left);
        int r = maxDiameter_travel(root.right);
        // 后序位置：计算最大直径
        maxHeight2 = Math.max(maxHeight2, l + r);
        return 1 + Math.max(l, r);

    }



    private int maxPathSum;

    /**
     * LeetCode：124
     * 求最大路径和
     */
    public int maxPathSum(TreeNode root) {
        maxPathContribute(root);
        return maxPathSum;
    }
    // 后序位置
    // 计算当前节点的最大贡献
    public int maxPathContribute(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int l = maxPathContribute(root.left);
        int r = maxPathContribute(root.right);

        int currentMaxPathSum = root.val + Math.max(0, l) + Math.max(0, r);

        System.out.println("---current: " + root.val + "---currentMaxPathSum: " + currentMaxPathSum);
        // 求某个节点为根的最大路径和 = 当前节点值 + Max(左子树的路径和 + 右子树的路径和)
        maxPathSum = Math.max(maxPathSum, currentMaxPathSum);
        // 计算当前节点的最大贡献
        int maxProfit = root.val + Math.max(0, Math.max(l, r));
        return maxProfit;
    }






}
