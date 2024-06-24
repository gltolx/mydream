package com.lin.mydream.util.algo;

import java.util.Stack;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/20 23:43
 * @desc 直方图的最大矩形
 */
public class LargestRectangleArea_84 {



    /*
     * 23416
     *
     * 215623
     */
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        if (heights.length == 1) {
            return heights[0];
        }
        int[] h = new int[heights.length + 2];
        h[0] = 0;
        h[h.length - 1] = 0;
        System.arraycopy(heights, 0, h, 1, heights.length);

        int largestArea = 0;
        Stack<Integer> ddStack = new Stack<>();
        for (int i = 0; i < h.length; i++) {

            while (!ddStack.isEmpty() && h[i] < h[ddStack.peek()]) {
                Integer pop = ddStack.pop();
                int leftMin = ddStack.isEmpty() ? 0 : ddStack.peek();
                int wide = i - leftMin - 1;
                int height = h[pop];

                largestArea = Math.max(largestArea, wide * height);
            }
            ddStack.push(i);

        }
        return largestArea;

    }

    public static void main(String[] args) {
//        int[] arr = {2,3,4,1,6};
        int[] arr = {2,1,5,6,2,3};
        System.out.println(new LargestRectangleArea_84().largestRectangleArea(arr));
    }
}
