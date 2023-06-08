package com.lin.mydream.util.algo;

import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class Algorithms {
    public static void main(String[] args) {

        System.out.println(findKthLargest(new int[]{3, 8, 5, 24, 64, 51, 43, 27, 44}, 1));
    }


    /**
     * 使用java查找一个数字k是否在二维数组中（这个二维数组是单调递增的，且行首大于上一行末）。
     */
    public static int binarySearch2DMatrix(int[][] matrix, int k) {
        // 行数
        int m = matrix.length;
        // 列数
        int n = matrix[0].length;
        int left = 0;
        int right = m * n - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int midVal = matrix[mid / n][mid % n];
            if (midVal == k) {
                return mid;
            } else if (midVal < k) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 利用最大堆求数组的第k大的数据
     */
    public static Integer findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int num : nums) {
            pq.offer(num);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        return pq.peek();
    }

    /**
     * 删重复出现的字符串（同字符连续出现超过 k 次）
     */
    public String removeDuplicates2(String s, int k) {

        Stack<Character> stack = new Stack<>();
        Stack<Integer> count = new Stack<>();
        for (char c : s.toCharArray()) {
            if (!stack.isEmpty() && stack.peek() == c) {
                count.push(count.pop() + 1);
            } else {
                count.push(1);
            }
            stack.push(c);
            if (count.peek() == k) {
                for (int i = 0; i < k; i++) {
                    stack.pop();
                }
                count.pop();
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.reverse().toString();
    }

    /**
     * 给定一个字符串，输出指定k单位长度的所有不存在重复字符的子字符串
     * 例子：
     * 输入：s="havefunonleetcode"，k=5
     * 输出：havef, avefu, vefun, efuno, etcod, tcode， 一共6个符合
     *
     * @return 符合的个数
     */
    private static int foo(String s, int k) {
        Set<Character> linkedSet = new LinkedHashSet<>();
        // j右游标，count总数
        int j = 0, count = 0;
        while (j < s.length()) {
            // 插入右游标的元素
            boolean suc = linkedSet.add(s.charAt(j));
            if (suc) {
                // 成功
                j++;
            } else {
                // 失败，表示存在重复
                linkedSet.remove(linkedSet.iterator().next());
            }

            if (linkedSet.size() >= k) {
                // 存在不重复的k个元素
                count++;
                linkedSet.forEach(System.out::print);
                System.out.print(", ");

                linkedSet.remove(linkedSet.iterator().next());
            }
        }
        return count;
    }

    static class BinarySearch {

        public static void main(String[] args) {
            int[] arr = {1,3,4,6,8,9,11,15,19};
            System.out.println(binSearch(arr, 4));
        }

        /**
         * 二分查找法
         *
         * @param arr    有序数组
         * @param target 目标元素
         * @return 元素所在下标
         */
        public static int binSearch(int[] arr, int target) {

            int leftIndex = 0;
            int rightIndex = arr.length - 1;
            while (leftIndex <= rightIndex) {

                int midIndex = (leftIndex + rightIndex) / 2;
                int midValue = arr[midIndex];
                if (target < midValue) {
                    rightIndex = midIndex - 1;
                } else if (target > midValue) {
                    leftIndex = midIndex + 1;
                } else {
                    // target == midValue
                    return midIndex;
                }
            }
            return -1;
        }

    }


    /**
     * 递归实现全排列
     * 给定一个数组，输出它的全排列
     * 例：[1,2,3] ,输出：123、132、231、213、321、312
     */
    public static void permutation(int[] array, int start, int end) {
        if (start == end) {
            for (int i = 0; i <= end; i++) {
                System.out.print(array[i]);
            }
            System.out.println();
        } else {
            for (int i = start; i <= end; i++) {
                swap(array, i, start);
                permutation(array, start + 1, end);
                swap(array, i, start);
            }
        }
    }

    /**
     * 输入是一个有序数组，如（1,3,8,9,2983,19203,20312,94888），
     * 给定一个数字K，请找到一个位置，当K插入数组后，数组仍然是有序的，
     * 算法输出只需要返回位置，不需要返回整个数组。
     * 二分法
     */
    public static int insertIntoSortedArray(int[] arr, int k) {
        int left = 0;
        int right = arr.length - 1;
        int mid = 0;
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (arr[mid] == k) {
                return mid;
            } else if (arr[mid] < k) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    // 交换数组中两个元素的位置
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * 给你两个字符串 word1 和 word2 。请你从 word1 开始，通过交替添加字母来合并字符串。
     * 如果一个字符串比另一个字符串长，就将多出来的字母追加到合并后字符串的末尾。
     * 返回 合并后的字符串。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/merge-strings-alternately
     */
    static class MergeAlternatelySolution {

        public static void main(String[] args) {
            System.out.println(mergeAlternately("abc", "pqr"));
            System.out.println(mergeAlternately("ab", "pqrs"));
            // apbqcd
            System.out.println(mergeAlternately("abcd", "pq"));
        }

        /**
         * 比官方更少的内存消耗
         */
        public static String mergeAlternately(String word1, String word2) {
            int len1 = word1.length();
            int len2 = word2.length();

            StringBuilder sb = new StringBuilder(len1 + len2);
            int minLen = Math.min(len1, len2);
            for (int i = 0; i < minLen; i++) {
                sb.append(word1.charAt(i));
                sb.append(word2.charAt(i));
            }
            if (len1 < len2) {
                sb.append(word2.substring(len1));
            } else {
                sb.append(word1.substring(len2));
            }
            return sb.toString();
        }

        /**
         * 官方题解
         */
        public static String mergeAlternately2(String word1, String word2) {
            int len1 = word1.length();
            int len2 = word2.length();
            int i = 0, j = 0;

            StringBuilder sb = new StringBuilder();

            while (i < len1 || j < len2) {
                if (i < len1) {
                    sb.append(word1.charAt(i++));
                }
                if (j < len2) {
                    sb.append(word2.charAt(j++));
                }
            }
            return sb.toString();
        }
    }

    /**
     * 求字符串的最大公因子
     * 对于字符串s 和t，只有在s = t + ... + t（t 自身连接 1 次或多次）时，我们才认定“t 能除尽 s”。
     * 给定两个字符串str1和str2。返回 最长字符串x，要求满足x 能除尽 str1 且 x 能除尽 str2 。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/greatest-common-divisor-of-strings
     */
    static class GcdOfStringsSolution {

        public static void main(String[] args) {
//            System.out.println(gcdOfStrings("ABCABC", "ABC"));
//            System.out.println(gcdOfStrings("ABABAB", "ABAB"));
//            System.out.println(gcdOfStrings("LEET", "CODE"));
//            System.out.println(gcdOfStrings("TAUXXTAUXXTAUXXTAUXXTAUXX", "TAUXXTAUXXTAUXXTAUXXTAUXXTAUXXTAUXXTAUXXTAUXX"));
            System.out.println(gcdOfStrings("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
        }

        public static String gcdOfStrings(String str1, String str2) {
            // 假定str1长度大于str2，反之则交换
            if (str2.length() > str1.length()) {
                String tmp = str1;
                str1 = str2;
                str2 = tmp;
            }
            // 对str2进行细分
            int sp = 2;
            String target = str2;
            while (target.length() >= 1) {
                if (isGcd(str1, target)) {
                    return target;
                }
                do {
                    if (target.length() <= 1 && !isGcd(str2, target)) {
                        return "";
                    }
                    target = str2.substring(0, str2.length() / sp++);
                } while (!isGcd(str2, target));
            }
            return "";

        }


        public static boolean isGcd(String str, String target) {
            int len = str.length();
            int targetLen = target.length();
            if (targetLen == 0 || len == 0) {
                return false;
            }
            if (targetLen > len) {
                return false;
            }
            if (targetLen == len) {
                return target.equals(str);
            } else {
                // 长度是倍数
                if (len % targetLen != 0) {
                    return false;
                }
                StringBuilder sb = new StringBuilder();
                while (sb.length() < len) {
                    sb.append(target);
                }
                return sb.toString().equals(str);
            }

        }
    }

    /**
     * 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
     * 给你一个整数数组flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。
     * 另有一个数n ，能否在不打破种植规则的情况下种入n朵花？能则返回 true ，不能则返回 false。
     */
    public static class CanPlaceFlowers {

        public boolean canPlaceFlowers(int[] flowerbed, int n) {
            if (n > flowerbed.length) {
                return false;
            }
            int count = 0;
            for (int i = 0; i < flowerbed.length; i++) {
                int pre = Math.max(i - 1, 0);
                int post = Math.min(i + 1, flowerbed.length);
                boolean empty = flowerbed[i] == 0;
                if (!empty) {
                    i++; // 跳一格
                } else if (flowerbed[pre] == 0 && flowerbed[post] == 0) {
                    flowerbed[i] = 1;
                    count++;
                }
            }
            return count >= n;

        }
    }

}
