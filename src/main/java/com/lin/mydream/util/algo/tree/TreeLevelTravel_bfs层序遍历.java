package com.lin.mydream.util.algo.tree;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lin.mydream.util.algo.base.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/5/22 20:08
 * @desc 二叉树的层序遍历
 */
public class TreeLevelTravel_bfs层序遍历 {



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


    public List<Integer> dfs(TreeNode root) {
        List<Integer> ret = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            ret.add(pop.val);

            if (pop.left != null) {
                stack.push(pop.left);
            }
            if (pop.right != null) {
                stack.push(pop.right);
            }
        }
        return ret;
    }


}
