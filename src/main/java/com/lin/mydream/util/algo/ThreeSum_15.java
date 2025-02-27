package com.lin.mydream.util.algo;

import java.util.*;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/7/4 15:37
 * @desc
 */
public class ThreeSum_15 {

    public static void main(String[] args) {
        int[] nums = {0,0,0,0};
//        int[] nums = {-1,0,1,2,-1,-4};
        System.out.println(new ThreeSum_15().threeSum(nums));
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            // a为指向最左且最小的指针，
            if (nums[i] > 0) {
                break;
            }
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            // j为中指针
            int j = i + 1;
            // k为右指针
            int k = nums.length - 1;
            while (j < k) {
                if (nums[i] == 0 && nums[j] > 0) {
                    break;
                }
                int sum = nums[i] + nums[j] + nums[k];
                if (sum == 0) {
                    List<Integer> ret = new ArrayList<>();ret.add(nums[i]);ret.add(nums[j]);ret.add(nums[k]);res.add(ret);
                    do {
                        j++;
                    } while (j < k && nums[j] == nums[j - 1]);
                    do {
                        k--;
                    } while (k > j && nums[k] == nums[k + 1]);
                } else if (sum < 0) {
                    do {
                        j++;
                    } while (j < k && nums[j] == nums[j - 1]);
                } else {
                    // sum > 0
                    do {
                        k--;
                    } while (k > j && nums[k] == nums[k + 1]);
                }
            }
        }

        return res;
    }
    public List<List<Integer>> threeSum2(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.compute(num, (k, v) -> v == null ? 1 : v + 1);
        }
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        Set<String> repeated = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {

                int twoSum = nums[i] + nums[j];
                Integer count = map.get(-twoSum);
                if (-twoSum == nums[i]) {
                    --count;
                }
                if (-twoSum == nums[j]) {
                    --count;
                }
                if (count == null || count <= 0) {
                    continue;
                }
                List<Integer> ret = new ArrayList<>();ret.add(nums[i]);ret.add(nums[j]);ret.add(-twoSum);
//                Collections.sort(ret);
                String joinString = ret.get(0) + "_" + ret.get(1) + "_" + ret.get(2);
                if (repeated.contains(joinString)) {
                    continue;
                }
                res.add(ret);
                repeated.add(joinString);
            }
        }

        return res;
    }
}
