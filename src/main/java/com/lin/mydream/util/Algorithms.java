package com.lin.mydream.util;

import java.util.LinkedHashSet;
import java.util.Set;

public class Algorithms {


    public static void main(String[] args) {
        String s = "havefunonleetcode";
        int k = 5;
        System.out.println(foo(s, k));

//        int[] arr = {1,2,3};
//        permutation(arr, 0, 2);
    }

    /**
     * 给定一个字符串，输出指定k单位长度的所有不存在重复字符的子字符串
     * 例子：
     * 输入：s="havefunonleetcode"，k=5
     * 输出：havef, avefu, vefun, efuno, etcod, tcode， 一共6个符合
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

    // 交换数组中两个元素的位置
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


}
