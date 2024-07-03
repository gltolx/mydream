package com.lin.mydream.util.algo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/5/21 19:10
 * @desc
 */
public class MajorityElement {

    /**
     * 数组中占比超过一半的元素称之为主要元素。给你一个 整数 数组，找出其中的主要元素。若没有，返回 -1 。请设计时间复杂度为 O(N) 、空间复杂度为 O(1) 的解决方案。
     *
     * 示例 1：
     *
     * 输入：[1,2,5,9,5,9,5,5,5]
     * 输出：5
     * 示例 2：
     *
     * 输入：[3,2]
     * 输出：-1
     * 示例 3：
     *
     * 输入：[2,2,1,1,1,2,2]
     * 输出：2
     *
     * 15345699955
     */
    public int majorityElement(int[] nums) {

        int majority = -1;
        int mergeCount = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (mergeCount == 0) {
                majority = num;
            }
            if (majority == num) {
                mergeCount++;
            } else {
                mergeCount--;
            }
        }
        //
        if (mergeCount > 0) {
            final int finalMajority = majority;
            if (Arrays.stream(nums).filter(n -> n == finalMajority).count() > (nums.length / 2)) {
                return majority;
            }
        }
        return -1;
    }


    /**
     * 给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素。
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums = [3,2,3]
     * 输出：[3]
     *
     * 示例 2：
     * <p>
     * 输入：nums = [1]
     * 输出：[1]
     *
     * 示例 3：
     * <p>
     * 输入：nums = [1,2]
     * 输出：[1,2]
     * 提示：
     * <p>
     * 1 <= nums.length <= 5 * 104
     * -109 <= nums[i] <= 109
     * 进阶：尝试设计时间复杂度为 O(n)、空间复杂度为 O(1)的算法解决此问题。
     * <p>
     * Related Topics
     * 数组
     * 哈希表
     * 计数
     * 排序
     */
    public List<Integer> majorityElement2(int[] nums) {
        List<Integer> ret = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return ret;
        }
        if (nums.length == 1) {
            ret.add(nums[0]);
            return ret;
        }
        // majority
        int majority1 = -1000000001;
        int majority2 = -1000000001;
        int count1 = 0;
        int count2 = 0;

        for (int num : nums) {

            if (num == majority1) {
                count1++;
            } else if (num == majority2) {
                count2++;
            } else if (count1 == 0) {
                majority1 = num;
                count1 = 1;
            } else if (count2 == 0) {
                majority2 = num;
                count2 = 1;
            } else {
                count1--;
                count2--;
            }

        }
        if (majority1 == -1000000001 && majority2 == -1000000001) {
            return ret;
        }
        int realCount1 = 0;
        int realCount2 = 0;
        for (int num : nums) {
            if (num == majority1) {
                realCount1++;
            } else if (num == majority2) {
                realCount2++;
            }
        }
        if (realCount1 > nums.length / 3) {
            ret.add(majority1);
        }
        if (realCount2 > nums.length / 3) {
            ret.add(majority2);
        }
        return ret;

    }

    /**
     * 将num2合并到nums1中（两个数组都是递增顺序）
     *
     * @param nums1 nums1数组
     * @param m     nums1中应合并的元素个数
     * @param nums2 nums2数组
     * @param n     nums2中应合并的元素个数
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (m == 0) {
//            nums1[]
        }

    }





    public static void main(String[] args) {
//        int[] nums = {1,2,5,9,5,9,5,5,5};
//        int[] nums = {3,2};
//        int[] nums = {2,2,1,1,1,2,2};
//        int[] nums = {2,1,1,1,2,2};
//        int[] nums = {5,4,5,4,5,4,3};
        int[] nums = {-1, -1, -1};

        System.out.println(JSON.toJSONString(new MajorityElement().majorityElement2(nums)));
    }
}
