package com.lin.mydream.util.algo.tree;

import com.lin.mydream.util.algo.base.TreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/25 10:30
 */
public class LowestCommonAncestor_236_dfs最近公共祖先 {

    Map<Integer, TreeNode> parents = new HashMap<>();
    Set<Integer> visited = new HashSet<>();

    /**
     * 自己也可以是祖先
     * @param root
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        dfs(root);
        while (p != null) {
            visited.add(p.val);
            p = parents.get(p.val);
        }
        while (q != null) {
            if (visited.contains(q.val)) {
                return q;
            }
            q = parents.get(q.val);
        }
        return null;
    }

    public void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            parents.put(root.left.val, root);
            dfs(root.left);
        }
        if (root.right != null) {
            parents.put(root.right.val, root);
            dfs(root.right);
        }
    }


}
