package com.lin.mydream.util.algo;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/13 19:22
 */
public class Croak {

    public int croakOfFrogs(String croakOfFrogs) {


        if (croakOfFrogs == null) {
            return 0;
        }
        int length = croakOfFrogs.length();
        if (length % 5 != 0) {
            return -1;
        }
        int[] arr = {0, 0, 0, 0 ,0};
        int max = 0;
        // 当前有几只还在叫
        int current = 0;
        for (int i = 0; i < length; i++) {
            char x = croakOfFrogs.charAt(i);

            if (x == 'c') {
                arr[0]++;
                current++;
            } else if (x == 'r') {
                arr[1]++;
            } else if (x == 'o') {
                arr[2]++;
            } else if (x == 'a') {
                arr[3]++;
            } else if (x == 'k') {
                arr[4]++;
                current--;
            }
            max = Math.max(max, current);
            if (!(arr[0] >= arr[1] && arr[1] >= arr[2] && arr[2] >= arr[3] && arr[3] >= arr[4])) {
                return -1;
            }
        }
        if (current > 0) {
            // 还有叫的，但卡住了
            return -1;
        }
        return max;



    }
}
