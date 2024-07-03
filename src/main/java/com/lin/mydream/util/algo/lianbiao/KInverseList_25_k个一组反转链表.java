package com.lin.mydream.util.algo.lianbiao;

import com.lin.mydream.util.algo.base.ListNode;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/7/1 13:45
 * @desc K个一组反转链表
 */
public class KInverseList_25_k个一组反转链表 {


    public ListNode reverseKGroup(ListNode head, int k) {
        // 1->2->3->4->5，k=2
        // 2->1 3
        // 5<-4<-1<-2<-3
        // 3->2->1->4->5
        ListNode virtual = new ListNode(-1);
        virtual.next = head;
        ListNode prev = virtual;
        ListNode cursor = head;
        while (true) {

            for (int i = 0; i < k; i++) {
                if (cursor == null) {
                    return virtual.next;
                }
                cursor = cursor.next;
            }

            // k个
            ListNode old_head = prev.next;
            prev.next = reverseK(old_head, k);

            old_head.next = cursor;
            prev = old_head;

        }

    }

    public ListNode reverseKGroup2(ListNode head, int k) {
        // 1->2->3->4->5，k=2
        // 2->1->4->3->5
        ListNode virtual = new ListNode(-1);
        ListNode pre = virtual;
        virtual.next = head;
        ListNode cursor = head;

        while (cursor != null) {

            int count = 0;
            while (cursor != null) {
                count++;
                cursor = cursor.next;
                if (count >= k) {
                    break;
                }
            }
            if (count < k) {
                return virtual.next;
            } else {
                ListNode curr_before_reverse_head = pre.next;
                ListNode curr_after_reverse_head = reverseK(curr_before_reverse_head, k);
                pre.next = curr_after_reverse_head;
                curr_before_reverse_head.next = cursor;
                pre = curr_before_reverse_head;

            }

        }
        return virtual.next;

    }

    public ListNode kBeside(ListNode cursor, int k) {
        while (cursor != null && k > 0) {
            k--;
            cursor = cursor.next;
        }
        return k == 0 ? cursor : null;
    }

    /**
     * @return tail-node
     */
    public ListNode reverseK(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        ListNode next = null;

        ListNode cursor = head;
        while (cursor != null && k-- >= 1) {
            ListNode tmp = cursor.next;
            cursor.next = next;
            next = cursor;
            cursor = tmp;
        }
//        return head;
        return next;

    }

    public ListNode reverse(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode next = null;

        ListNode cursor = head;

        while (cursor != null) {
            ListNode tmp = cursor.next;
            cursor.next = next;
            next = cursor;
            cursor = tmp;
        }
        return next;

    }

    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        n1.next = n2; n2.next = n3;n3.next = n4;n4.next=n5;

        ListNode listNode = new KInverseList_25_k个一组反转链表().reverseKGroup2(n1, 2);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }

//        ListNode listNode = new KInverseList_25_k个一组反转链表().kBeside(n1, 3);
//        System.out.println(listNode.val);
//        ListNode reverse = new KInverseList_25_k个一组反转链表().reverse(n1);
//        while (reverse !=null) {
//            System.out.println(reverse.val);
//            reverse = reverse.next;
//        }

    }


}
