package com.whllow.community;


import java.util.PriorityQueue;
import java.util.Scanner;

public class glodon {
    public static void main(String[] args) {
        PriorityQueue<Integer> pQueue = new PriorityQueue<>();
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int n = sc.nextInt();
        int large = sc.nextInt();

        for (int i = 0; i < num; i++) {
            int node  = sc.nextInt();
            pQueue.add(node);

        }
       while (n > 0){
           int a = pQueue.poll();
           a += large;
           pQueue.add(a);
           n--;
       }
        System.out.println(pQueue.peek());
    }
}
