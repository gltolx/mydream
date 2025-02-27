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

//    public static int partition_wk(int[] arr, int l, int r) {
//        if (l >= r) {
//            return l; // 当区间只有一个元素或为空时，直接返回区间的起点
//        }
//        int base = arr[l];
//        int wk = l;
//        int left = l + 1;
//        int right = r;
//
//        while (left <= right) {
//            // 从右向左找第一个小于base的元素
//            while (left <= right && arr[right] >= base) {
//                right--;
//            }
//            // 从左向右找第一个大于base的元素
//            while (left <= right && arr[left] <= base) {
//                left++;
//            }
//            // 发现不满足排序条件的元素后交换它们
//            if (left < right) {
//                int temp = arr[left];
//                arr[left] = arr[right];
//                arr[right] = temp;
//                // 交换后需检查是否需要调整wk的位置
//                if (arr[left] == base) {
//                    wk = left;
//                } else if (arr[right] == base) {
//                    wk = right;
//                }
//            }
//        }
//        // 将基准元素放到最终位置
//        arr[wk] = base;
//        arr[l] = arr[right];
//        arr[right] = base;
//        return right;
//    }

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



    public static void qSort2(int[] arr, int i, int j) {

        int mid = partition(arr, i, j);

        if (mid == -1) {
            return;
        }
        qSort2(arr, i, mid);
        qSort2(arr, mid + 1, j);
    }

    // 2,5,4,3,6,0,7,4 -> 0,[2],5,4,3,6,7,4
    // 0,[2],4,3,4,[5],6,7
    public static int partition(int[] arr, int i, int j) {
        // 以i为基准
        int pivot =  arr[i];

        while (i < j) {
            while (arr[j] >= pivot & i < j) {
                j--; // 从右边找到第一个比基准值小的
            }
            arr[i] = arr[j];
            while (arr[i] <= pivot && i < j) {
                i++; // 从左边找到第一个比基准值大的
            }
            arr[j] = arr[i];
        }
        arr[i] = pivot;
        return i;
    }

}
