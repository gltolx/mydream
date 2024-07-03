package com.lin.mydream.util.algo.lianbiao;

import com.lin.mydream.util.algo.base.ListNode;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/25 12:02
 */
public class GetIntersectionNode_160_相交链表 {

    public static void main(String[] args) {
        // [4,1,8,4,5]
        //			[5,6,1,8,4,5]
        ListNode a1 = new ListNode(6);
        ListNode a2 = new ListNode(4);
        ListNode a3 = new ListNode(5);
        a1.next = a2; a2.next = a3;

        ListNode b1 = new ListNode(1);
        ListNode b2 = new ListNode(2);
        ListNode b3 = new ListNode(3);
        b1.next = b2; b2.next = b3; b3.next = a2;
        ListNode intersectionNode = new GetIntersectionNode_160_相交链表().getIntersectionNode(a1, b1);
        System.out.println(intersectionNode == null? "null" : intersectionNode.val);
    }
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {

        int aLength = getLength(headA);
        int bLength = getLength(headB);
        int aStep = Math.abs(aLength - bLength);

        ListNode cursorLong = aLength - bLength >= 0 ? headA : headB;
        ListNode cursorShort = aLength - bLength >= 0 ? headB : headA;

        int i = 1;
        while (cursorLong != null) {
            if (i > aStep) {
                if (cursorShort == cursorLong) {
                    return cursorShort;
                }
                cursorShort = cursorShort.next;
            }
            cursorLong = cursorLong.next;
            i++;
        }
        return null;

    }

    public int getLength(ListNode headA) {
        int length = 0;
        ListNode cursor = headA;
        while (cursor != null) {
            length++;
            cursor = cursor.next;
        }
        return length;
    }

}
