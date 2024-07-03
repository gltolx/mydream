package com.lin.mydream.util.algo.backtrack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/27 17:48
 */
public class Subsets_78_子集 {

    private List<List<Integer>> res = new ArrayList<>();
    private LinkedList<Integer> track = new LinkedList<>();

    int count;

    public static void main(String[] args) {
        int[] arr = {1,2,3};
        System.out.println(new Subsets_78_子集().subsets(arr));
    }
    public List<List<Integer>> subsets(int[] nums) {

//        backtrack(nums, 0);
        backtrack2(nums, 0);
        System.out.println("count: " + count);
        return res;
    }

    public void backtrack(int[] nums, int i) {
        count++;
        // 终止条件
        if (i == nums.length) {
            res.add(new ArrayList<>(track));
            return;
        }
        // 不选
        backtrack(nums, i + 1);
        // 选num[i]
        track.add(nums[i]);
        backtrack(nums, i + 1);
        track.removeLast();
    }


    public void backtrack2(int[] nums, int i) {
        count++;
        res.add(new ArrayList<>(track));
//        // 终止条件
//        if (i == nums.length) {
//            return;
//        }
        for (int j = i; j <nums.length; j++) {
            // 选num[j]
            track.add(nums[j]);
            backtrack2(nums, j + 1);
            track.removeLast();
        }
    }
}
