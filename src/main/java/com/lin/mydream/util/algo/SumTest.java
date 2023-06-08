package com.lin.mydream.util.algo;

public class SumTest {

    public static void main(String[] args) {

        System.out.println(sum(1,2));
        System.out.println(sum(11,34));
        System.out.println(sum(-9,29));
    }

    /**
     * 求a+b的和（位运算）
     */
    public static int sum(int a, int b) {
        int sum = a;
        while(b != 0) {
            sum = a ^ b;
            b = (a & b) << 1;
            a = sum;
        }
        return sum;
    }
}
