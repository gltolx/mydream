package com.lin.mydream.util.algo.lianbiao;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/24 16:31
 * @desc
 */
public class ReorderList_143_重排链表 {
    /**
     * 合并「前半部分」和「后半部分的逆序」
     * 1->2->3->4->5
     *  middle: 3
     *  front: 1->2  back: 3->4->5
     *  reverse: 5->4->3
     *  ret: 1->5->2->4->3
     *
     * 1->2->3->4
     *  middle: 3
     *  front: 1->2 back:3->4
     *  reverse: 4->3
     *  ret: 1->4->2->3
     *
     * 1->2->3->4->5->6
     * 6->5
     * 1->6->2->5->3->4
     */
    public void reorderList(ListNode head) {
        ListNode mid = middleNode(head);
        ListNode backReverse = reverse(mid.next);
        mid.next = null;

        ListNode frontCursor = head;
        ListNode backCursor = backReverse;

        while (frontCursor != null && backCursor != null) {
            ListNode frontNextTmp = frontCursor.next;
            ListNode backNextTmp = backCursor.next;

            frontCursor.next = backCursor;
            backCursor.next = frontNextTmp;

            frontCursor = frontNextTmp;
            backCursor = backNextTmp;
        }

    }

    public void reorderList2(ListNode head) {
        ListNode middleNode = middleNode(head);

        ListNode backReverse = reverse(middleNode);

        ListNode frontCursor = head;
        ListNode backCursor = backReverse;

        while (frontCursor != middleNode) {
            ListNode frontNextTmp = frontCursor.next;
            ListNode backNextTmp = backCursor.next;

            frontCursor.next = backCursor;
            if (backCursor.next != null) {
                backCursor.next = frontNextTmp;
            }

            frontCursor = frontNextTmp;
            backCursor = backNextTmp;
        }

    }


    /**
     * 给定一个头节点为 head 的非空单链表，返回链表的中间节点。
     *
     * 如果有两个中间节点，则返回第二个中间节点。
     * leetcode：876
     */
    public ListNode middleNode(ListNode head) {
        // 快慢指针
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;

    }

    /**
     *  1->2->3->4->null
     *  null<-1<-2<-3<-4
     */
    public ListNode reverse(ListNode head) {
        ListNode cursor = head;
        ListNode preNode = null;
        while (cursor != null) {
            ListNode tmp = cursor.next;
            cursor.next = preNode;
            preNode = cursor;

            cursor = tmp;
        }
        return preNode;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        l1.next = l2; l2.next = l3; l3.next = l4; l4.next =l5;
        new ReorderList_143_重排链表().reorderList(l1);


        while (l1 != null) {
            System.out.println(l1.val);
            l1 = l1.next;
        }
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

}
