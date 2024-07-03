package com.lin.mydream.util.algo.backtrack;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/26 22:52
 */
public class Permutation_46_数组全排列 {

    private List<List<Integer>> res = new ArrayList<>();
    private LinkedList<Integer> oneTrack = new LinkedList<>();

    public List<List<Integer>> permute(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        // 构造已选择列表
        int[] used = new int[nums.length];
        backtrack(nums, used);
        return res;

    }

    public void backtrack(int[] nums, int[] used) {
        if (oneTrack.size() == nums.length) {
            // 结束条件：决策树高度已经达到num的数目，代表已经进行了一轮的决策
            res.add(new ArrayList<>(oneTrack));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i] == 1) {
                continue;
            }

            // 做选择：1、加入结果列表 2、移除选择列表
            int num = nums[i];
            oneTrack.add(num);
            used[i] = 1;
            // 进入下一层决策树
            backtrack(nums, used);
            // 撤销选择，因为使用linkedList，当返回到第一层决策节点时，oneTrack已经被删空了，后变后续其他决策节点使用
            oneTrack.removeLast();
            used[i] = 0;
        }

    }

    public static void main(String[] args) {
        int[] nums = {1,2,3};
        System.out.println(JSON.toJSONString(new Permutation_46_数组全排列().permute(nums)));
    }


}
