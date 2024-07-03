package com.lin.mydream.util.algo.lianbiao;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/24 17:17
 * @desc
 */
public class ListNodeDeepClone_138_复杂链表复制 {

    @NoArgsConstructor
    @Data
    public static class Node {
        private Integer val;
        private Node next;
        private Node random;


        public Node(Integer val) {
            this.val = val;
        }


        /**
         * 简单复制
         */
        public static Node simpleCopy(Node root) {
            if (root == null){
                return null;
            }
            Node virtual = new Node();
            Node pre = virtual;
            Node cursor = root;
            do {
                Node current = new Node(cursor.val);
                pre.next = current;
                cursor = cursor.next;
                pre = current;
            } while (cursor != null);

            return virtual.next;
        }

        /**
         * 复杂复制
         */
        public static Node complexCopy(Node head) {
            if (head == null) {
                return null;
            }
            // 1. 先将链表*2，由abcd变成aabbccdd
            Node cursor = head;
            do {
                Node copy = new Node(cursor.val);
                copy.random = cursor.random;
                copy.next = cursor.next;
                cursor.next = copy;
                cursor = copy.next;
            } while (cursor != null);

            // 2. 再将奇数下标节点的random节点统一后移1位，a1->c1，a2->c2，则只需知道c1后移1位即为c2
            cursor = head;

            boolean odd = false;
            while (cursor != null) {
                if (odd) {
                    if (cursor.random != null) {
                        cursor.random = cursor.random.next;
                    }
                }
                odd = !odd;
                cursor = cursor.next;
            }

            // 3. 最后将链表分离并复原原链表
            cursor = head;
            odd = false;
            Node virtual = new Node(0);  // copy数组的虚拟头节点，用于最后通过next定位到真正的头
            Node pre0 = new Node(0);     // 原数组的前节点
            Node pre1 = virtual;        // copy数组的前节点
            while (cursor != null) {
                if (odd) {
                    pre1.next = cursor;
                    pre0.next = cursor.next;
                    pre1 = cursor;
                } else {
                    pre0 = cursor;
                }
                odd = !odd;
                cursor = cursor.next;
            }

            return virtual.next;

        }

        public static void printQueue(Node root) {
            if (root == null) {
                System.out.println(" -> null");
            } else {
                System.out.print(" -> " + root.getVal());
                printQueue(root.next);
            }
        }

        public static void main(String[] args) {

            Node n1 = new Node(1);
            Node n2 = new Node(2);
            Node n3 = new Node(3);
            Node n4 = new Node(4);
            Node n5 = new Node(5);
            n1.next = n2; n1.random = n3;
            n2.next = n3; n2.random = n3;
            n3.next = n4; n3.random = n1;
            n4.next = n5; n4.random = n2;
            n5.random = n5;
            printQueue(n1);
            Node newCopy = complexCopy(n1);

            System.out.println();
            printQueue(newCopy);

            System.out.println();

        }


    }
}
