package com.lin.mydream.util.algo.tanxin;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/7/1 11:25
 * @desc
 */
public class Candy2 {

    public static void main(String[] args) {
        // Scanner input=new Scanner(System.in);
        // String str=input.next();
        // System.out.println("hello world");
        int[] arr = {0,1,2,3,3,3,2,2,2,2,2,1,1};
        System.out.println(need(13, arr));
    }
    public int candy(int[] ratings) {
        // dp[i] = dp[i-1] + ??arr[i]
        // 到arr[i]的时候，需要dp[i]个糖果
        int[] dp = new int[ratings.length];

        for (int i = 0; i < ratings.length; i++) {
            dp[i] = 1;
        }
        // 从左向右
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i-1]) {
                dp[i] = dp[i-1] + 1;
            } else if (ratings[i] == ratings[i-1]) {
//                dp[i] = dp[i-1];
            } else {
                // do nothing
            }
        }
        // 从右向左
        for (int i = ratings.length - 1; i >= 1; i--) {
            if (ratings[i - 1] > ratings[i]) {
                if (dp[i - 1] <= dp[i]) {
                    dp[i - 1] = dp[i] + 1;
                }

            } else if (ratings[i] == ratings[i - 1]) {
//                if (dp[i - 1] != dp[i]) {
//                    dp[i - 1] = Math.max(dp[i - 1], dp[i]);
//                    dp[i] = dp[i - 1];
//                }

            } else {
                // do nothing
            }
        }


        int sum = 0;
        for (int j = 0; j < dp.length; j++) {
            System.out.println(dp[j]);
            sum+=dp[j];
        }
        return sum;
    }



    public static int need(int n, int[] arr) {

        // dp[i] = dp[i-1] + ??arr[i]
        // 到arr[i]的时候，需要dp[i]个糖果
        int[] dp = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            dp[i] = 1;
        }
        // 从左向右
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[i-1]) {
                dp[i] = dp[i-1] + 1;
            } else if (arr[i] == arr[i-1]) {
                dp[i] = dp[i-1];
            } else {
                // do nothing
            }
        }
        // 从右向左
        for (int i = arr.length -1; i >=1 ; i--) {
            if (arr[i-1] > arr[i]) {
                if(dp[i -1]<=dp[i]) {
                    dp[i-1] = dp[i] + 1;
                }

            } else if (arr[i] == arr[i-1]) {
                if (dp[i - 1] != dp[i]) {
                    dp[i-1] = Math.max(dp[i-1], dp[i]);
                    dp[i] = dp[i-1];
                }

            } else {
                // do nothing
            }
        }


        int sum = 0;
        for (int j = 0; j < dp.length; j++) {
            System.out.println(dp[j]);
            sum+=dp[j];
        }
        return sum;
    }

}
