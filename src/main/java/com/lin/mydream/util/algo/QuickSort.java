package com.lin.mydream.util.algo;

import com.alibaba.fastjson.JSON;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/19 19:10
 * @desc
 */
public class QuickSort {


    public static void quickSort(int[] arr, int l, int r) {
        if (arr.length <= 1) {
            return;
        }
        // 取第一个为mid
        int mid = partition_wk(arr, l, r);
        if (mid != -1) {
            quickSort(arr, l, mid - 1);
            quickSort(arr, mid + 1, r);
        }
    }

    /**
     * 快排partition 挖坑法
     */
    public static int partition_wk(int[] arr, int l, int r) {
        if (l >= r) {
            return -1;
        }
        int base = arr[l];
        int wk = l;
        int left = l + 1;
        int right = r;
        while (left <= right) {
            while (right >= l && left <= right) {
                if (arr[right] < base) {
                    arr[wk] = arr[right];
                    wk = right--;
                    break;
                }
                right--;
            }
            while (left <= r && left <= right) {
                if (arr[left] > base) {
                    arr[wk] = arr[left];
                    wk = left++;
                    break;
                }
                left++;
            }

        }
        arr[wk] = base;
        return wk;

    }

    /**
     * 快排partition 左右指针
     */
    public static void partition_zy(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        int mid = l;
        for (int i = l + 1; i <= r; i++) {
            if (arr[i] < arr[mid]) {
                int tmp = arr[i];
                arr[i] = arr[mid];
                arr[mid] = tmp;
                mid = i;
            }
        }

    }



    public static void main(String[] args) {
        int[] arr = {3,5,0,4,1,2,1,7,4,2};

//        System.out.println(partition_wk(arr, 0, arr.length - 1));
        quickSort(arr, 0, arr.length - 1);

        System.out.println(JSON.toJSONString(arr));
    }
}
