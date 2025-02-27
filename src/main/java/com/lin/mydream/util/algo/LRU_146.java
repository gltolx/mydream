package com.lin.mydream.util.algo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/7/3 15:56
 */
public class LRU_146 {


    public static class LRUC extends LinkedHashMap<Integer, Integer> {

        private final int capacity;
        LRUC(int capacity) {
            super(capacity, 0.75F, true);
            this.capacity = capacity;
        }

        int get(int key) {
            return super.getOrDefault(key, -1);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return super.size() >= capacity;
        }
    }
}
