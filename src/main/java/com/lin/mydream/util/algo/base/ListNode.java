package com.lin.mydream.util.algo.base;

import com.lin.mydream.util.algo.lianbiao.ReorderList_143_重排链表;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/25 12:03
 * @desc
 */
public class ListNode {
    public int val;
    public ListNode next;

    ListNode() {}

    public ListNode(int x) {
        val = x;
        next = null;
    }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
