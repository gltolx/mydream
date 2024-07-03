package com.lin.mydream.util.algo;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/24 15:33
 * @desc 买卖股票的最佳时机
 */
public class MaxProfit_121 {

    public static void main(String[] args) {
        int[] arr = {7, 1, 5, 3, 6, 4};
        System.out.println(new MaxProfit_121().maxProfit(arr));
    }

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int buy = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            if (price < buy) {
                buy = price;
            }
            maxProfit = Math.max(maxProfit, price - buy);
        }

        return maxProfit;
    }

    public int maxProfit_2(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int buy = Integer.MAX_VALUE;
        int sale = -1;
        int maxProfit = sale - buy;
        for (int price : prices) {
            if (price < buy) {
                // 重置买入点和卖出点
                buy = price;
                sale = -1;
            } else {
                sale = price;
            }
            maxProfit = Math.max(maxProfit, sale - buy);
        }

        if (maxProfit < 0) {
            return 0;
        }
        return maxProfit;
    }
}
