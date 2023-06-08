package com.lin.mydream.util.algo;

public class Test {

    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        //int a = in.nextInt();
        //System.out.println(a);
        System.out.println("Hello World!");


        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);

        for (int i = 0; i < 5; i++) {
            head.next = new Node(i);
            head = head.next;
        }

        Node reversed = reverse(head);

        while (reversed != null) {
            System.out.print(reversed.val + "   ");
            reversed = reversed.next;
        }


    }

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


}
