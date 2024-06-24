package com.lin.mydream.util.algo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

//    public static void main(String[] args) {
//        //Scanner in = new Scanner(System.in);
//        //int a = in.nextInt();
//        //System.out.println(a);
//        System.out.println("Hello World!");
//
//
//        Node head = new Node(1);
//        head.next = new Node(2);
//        head.next.next = new Node(3);
//        head.next.next.next = new Node(4);
//        head.next.next.next.next = new Node(5);
//
//        for (int i = 0; i < 5; i++) {
//            head.next = new Node(i);
//            head = head.next;
//        }
//
//        Node reversed = reverse(head);
//
//        while (reversed != null) {
//            System.out.print(reversed.val + "   ");
//            reversed = reversed.next;
//        }
//
//
//    }

    /**
     * 链表反转
     */
    public static Node reverse(Node head) {
        Node prev = null;
        Node curr = head;

        while (curr != null) {
            Node nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }

    public static class Node {
        private int val;
        private Node next;

        public Node() {
        }

        public Node(int val) {
            this.val = val;
        }

    }


    public static int lengthOfLongestNotRepeatSubString(String s) {

        int left = -1, maxLength = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Integer pos = map.get(c);
            if (pos != null) {
                left = Math.max(left, pos);
            }
            maxLength = Math.max(i - left, maxLength);
            map.put(c, i);
        }
        return maxLength;

    }

    public static int[] luoxuan(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[]{};
        }
        int m = matrix.length; // 行
        int n = matrix[0].length; // 列
        int top = 0;
        int down = m - 1;
        int left = 0;
        int right = n - 1;

        int[] ret = new int[m * n];
        int cursor = 0;

        while (top <= down && left <= right) {
            for (int i = left; i <= right; i++) {
                ret[cursor++] = matrix[top][i];
            }
            top++;

            if (top > down) {
                break;
            }
            for (int i = top; i <= down; i++) {
                ret[cursor++] = matrix[i][right];
            }
            right--;

            if (right < left) {
                break;
            }
            for (int i = right; i >= left; i--) {
                ret[cursor++] = matrix[down][i];
            }
            down--;

            if (down < top) {
                break;
            }
            for (int i = down; i >= top; i--) {
                ret[cursor++] = matrix[i][left];
            }
            left++;

        }
        return ret;

    }


    public static void main(String[] args) {
        int val = 1;
        int[][] arr = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                arr[i][j] = val++;
            }
        }

        int[] luoxuan = luoxuan(arr);
        System.out.println(JSON.toJSONString(luoxuan));
//        System.out.println(lengthOfLongestNotRepeatSubString("aabbac"));
    }



}
