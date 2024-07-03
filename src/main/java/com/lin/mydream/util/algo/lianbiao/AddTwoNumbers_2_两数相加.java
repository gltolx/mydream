package com.lin.mydream.util.algo.lianbiao;

import com.lin.mydream.util.algo.base.ListNode;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/27 20:16
 * @desc
 */
public class AddTwoNumbers_2_两数相加 {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode cursor1 = l1;
        ListNode cursor2 = l2;
        ListNode virtual = new ListNode(0);
        ListNode pre = virtual;

        boolean flag = false;
        while (flag || cursor1 != null || cursor2 != null) {

            int sum = flag ? 1 : 0;
            if (cursor1 == null && cursor2 == null) {

            } else {
                if (cursor1 == null) {
                    sum += cursor2.val;
                } else if (cursor2 == null) {
                    sum += cursor1.val;
                } else {
                    sum += cursor1.val + cursor2.val;
                }
            }
            if (sum >= 10) {
                sum = sum % 10;
                flag = true;
            } else {
                flag = false;
            }
            pre.next = new ListNode(sum);

            pre = pre.next;

            cursor1 = cursor1 == null? null : cursor1.next;
            cursor2 = cursor2 == null? null : cursor2.next;
        }
        return virtual.next;


    }
}
