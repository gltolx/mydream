package com.lin.mydream.util.algo.dp;

import java.util.Arrays;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/26 11:49
 * @desc
 */
public class DpTest {

    public static void main(String[] args) {
        System.out.println(new DpTest().fib(7));
        System.out.println("-----");
        int[] coins = {2};
        int amount = 3;
        System.out.println(new DpTest().coinChange_322_2(coins, amount));
        System.out.println("-----");
        System.out.println(new DpTest().circleSteps(10, 3));
        System.out.println("-----");
        int[] arr = {10,9,2,5,3,7,101,18};
        System.out.println("lengthOfLIS: " + new DpTest().lengthOfLIS(arr));
    }
    public int fib(int n) {

        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        // 状态转移方程（递推方程）：dp[i] = dp[i - 1] + dp[i - 2]
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i -2];
        }
        return dp[n];
    }


    public int coinChange_322(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        int[] dp = new int[amount + 1];
        for (int i = 1; i < dp.length; i++) {
            dp[i] = -1;
        }
        dp[0] = 0;
        // 递推方程：dp[i + coinA] = dp[i] + 1
        // 得到：dp[i] = dp[i - coinA] + 1
        for (int i = 1; i < dp.length; i++) {

            // dp[i]：要凑出i金额对应的最小硬币数dp[i]
            int min = Integer.MAX_VALUE;
            // 多个硬币的选择
            for (int coin : coins) {
                if (i - coin < 0) {
                    continue;
                }
                if (dp[i - coin] < 0) {
                    continue;
                }
                int currentNum = dp[i - coin] + 1;
                min = Math.min(min, currentNum);
            }
            dp[i] = min == Integer.MAX_VALUE ? -1 : min;
        }
        return dp[amount];

    }


    public int coinChange_322_2(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        for (int i = 1; i < dp.length; i++) {
            dp[i] = 10001;
        }
        dp[0] = 0;
        // 递推方程：dp[i + coinA] = dp[i] + 1
        // 得到：dp[i] = dp[i - coinA] + 1
        for (int i = 1; i < dp.length; i++) {

            // 多个硬币的选择
            for (int coin : coins) {
                if (i - coin < 0) {
                    continue;
                }
                int currentNum = dp[i - coin] + 1;
                dp[i] = Math.min(dp[i], currentNum);
            }
        }
        return dp[amount] == 10001 ? -1 : dp[amount];

    }

    /**
     * 圆环回原点的方案数
     * @param k 编号数，10个点代表0～9
     * @param n 走n步
     */
    public int circleSteps(int k, int n) {
        if (n % 2 != 0) {
            return 0;
        }
        /*
         * 目标：走n步回0点
         * 结果：共有多少种方案
         * 走i步回j点递推方程：dp[i][j] = dp[i-1][(j+1) % 10] + dp[i-1][(10+j-1) % 10]
         */
        int[][] dp = new int[n + 1][k];
        // 初始化
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {  // 走i步
            for (int j = 0; j < k; j++) { // 回j点
                dp[i][j] = dp[i - 1][(j + 1) % k] + dp[i - 1][(k + j - 1) % k];
            }
        }
        return dp[n][0];

    }

    /**
     * dp求解最长递增子序列的长度
     *  [10,9,2,5,3,7,101,18]
     *
     *  [2,3,7,101]
     */
    public int lengthOfLIS(int[] nums) {
        // 以nums[i]结尾的最长递增子序列的长度：dp[i] = Max(dp[j]) + 1

        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);

        int maxLength = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }



}
