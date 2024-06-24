package com.lin.mydream.util.algo;

import com.alibaba.fastjson.JSON;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/20 17:11
 * @desc 下一个较大排列
 */
public class NextLargeArray {

    public static void main(String[] args) {
//        int[] arr = {5,4,7,5,3,2};
//        int[] arr = {5,1,1};
//        int[] arr = {1,3,2};
//        int[] arr = {2,3,1};
        int[] arr = {2,0,3,4,3,2};
//        int[] arr = {4,3,2,1};
        nextLargeArr(arr);
        System.out.println(JSON.toJSONString(arr));
    }

    // 1,2,2,3
    // 2,0,1,4,3,2  2,0,2,1,3,4
    // 2,0,3,【4】,3,2  2,0,2,3,3,4
    // 2,0,2,【4】,3, (3)
    // 7,6,5,4,3,2  2,3,4,5,6,7
    // 2,3,1  3,1,2
    // 1,3,2  2,3,1
    public static void nextLargeArr(int[] nums) {
        if (nums.length <= 1) {
            return;
        }
        int endIndex = nums.length - 1;
        for (int i = endIndex; i >= 0; i--) {
            if (i == 0) {
                reverseArr(nums, 0, endIndex);
                break;
            }

            if (nums[i - 1] < nums[i]) {
                // 1. 到达高位时，应该高位和「后面最小的但比最高位大的」交换
                int tmp = nums[i - 1];
                int minSwap = i;
                for (int j = endIndex; j >= i; j--) {
                    if (nums[j] > tmp) {
                        minSwap = j;
                        break;
                    }
                }

                nums[i - 1] = nums[minSwap];
                nums[minSwap] = tmp;
                // 2. 逆序
                reverseArr(nums, i, endIndex);
                break;
            }
        }
    }

    public static void reverseArr(int[] nums, int l, int r) {

        while (l < r) {
            int tmp = nums[l];
            nums[l] = nums[r];
            nums[r] = tmp;
            l++;
            r--;
        }
    }
}
