package com.lin.mydream.util.algo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/21 13:59
 * @desc 最长递增子序列 300
 */
public class LengthOfLIS_300 {

    public static void main(String[] args) {
//        System.out.println(Collections.binarySearch(Lists.newArrayList(1, 3, 4, 5, 8, 9), 7));
//        List<Integer> increasings = new ArrayList<>(5);
//        System.out.println(increasings.size());
//        int[] i = {10, 9,2,5,3,7,101,18};
        int[] i = {2,5,6,3};
        System.out.println(new LengthOfLIS_300().lengthOfLIS(i));
    }
    // 2785634
    //
    public int lengthOfLIS(int[] nums) {
        // check
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }

        int maxLength = 0;
        List<Integer> increasings = new ArrayList<>(nums.length);
        for (int num : nums) {
            addOrReplace(increasings, num);
            maxLength = Math.max(maxLength, increasings.size());
        }
        return maxLength;

    }

    private void addOrReplace(List<Integer> increasings, int num) {
        if (increasings.size() == 0) {
            increasings.add(num);
        } else {
            int last = increasings.get(increasings.size() - 1);
            if (num > last) {
                increasings.add(num);
            } else if (num == last) {
                return;
            } else {
                // 找到比当前元素大的那个index：-(insert_point) - 1
                int searchIndex = Collections.binarySearch(increasings, num);
                if (searchIndex < 0) {
                     int replaceIndex = -searchIndex - 1;
                     increasings.set(replaceIndex, num);
                }
                // 直接找到元素则忽略
            }
        }
    }




}
