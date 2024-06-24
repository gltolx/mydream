package com.lin.mydream.util.algo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lin.mydream.util.algo.base.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/5/22 20:08
 * @desc 二叉树的层序遍历
 */
public class TreeLevelTravel {



    public List<Integer> treeLevelTravel(Node root) {
        PriorityQueue<Node> q = new PriorityQueue<>();
        q.add(root);
        List<Integer> ret = new ArrayList<>();
        while (!q.isEmpty()) {
            Node current = q.poll();
            ret.add(current.getValue());
            q.add(current.getLeft());
            q.add(current.getRight());
        }
        return ret;
    }





    public List<List<Integer>> treeLevelTravel(TreeNode root) {
        PriorityQueue<TreeNode> q = new PriorityQueue<>();
        q.add(root);
        List<List<Integer>> ret = new ArrayList<>();
        while (!q.isEmpty()) {
            int levelSize = q.size();
            List<Integer> levelList = new ArrayList<>();
            while (levelSize > 0) {
                TreeNode current = q.poll();
                levelList.add(current.val);
                q.add(current.left);
                q.add(current.right);
                levelSize--;
            }
        }
        return ret;
    }

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

        List<List<Integer>> ret = new TreeLevelTravel().levelOrder(root);

        System.out.println(JSON.toJSONString(ret, SerializerFeature.PrettyFormat));
    }


    public List<List<Integer>> levelOrder(TreeNode root) {
        LinkedList<TreeNode> q = new LinkedList<>();
        q.add(root);
        List<List<Integer>> ret = new ArrayList<>();
        while (!q.isEmpty()) {
            int levelSize = q.size();
            List<Integer> levelList = new ArrayList<>();
            while (levelSize > 0) {
                TreeNode current = q.poll();
                levelList.add(current.val);
                if (current.left != null) {
                    q.add(current.left);
                }
                if (current.right != null) {
                    q.add(current.right);
                }
                levelSize--;
            }
            ret.add(levelList);
        }
        return ret;
    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
