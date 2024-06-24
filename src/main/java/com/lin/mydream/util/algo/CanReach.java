package com.lin.mydream.util.algo;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/5/24 15:35
 */
public class CanReach {

    /**
     * 这里有一个非负整数数组 arr，最开始位于该数组的起始下标 start 处。
     * 当位于下标 i 处时，可以跳到 i + arr[i] 或者 i - arr[i]。
     * <p>
     * 请判断自己是否能够跳到对应元素值为 0 的 任一 下标处。注意，不管是什么情况下，你都无法跳到数组之外。
     * <p>
     * 输入：arr = [4,2,3,0,3,1,2], start = 5
     * 输出：true
     */
    public boolean canReach(int[] arr, int start) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        if (start < 0 || start >= arr.length) {
            return false;
        }

        LinkedList<Integer> q = new LinkedList<>();
        q.add(start);

        Set<Integer> alreadyReached = new HashSet<>();
        alreadyReached.add(start);

        while (!q.isEmpty()) {
//            int levelSize = q.size();
            Integer currentIndex = q.poll();
            Integer currentValue = arr[currentIndex];

            if (currentValue == 0) {
                return true;
            }

            int leftIndex = currentIndex - currentValue;
            int rightIndex = currentIndex + currentValue;
            if (leftIndex >= 0 && leftIndex < arr.length && alreadyReached.add(leftIndex)) {
                q.add(leftIndex);
            }

            if (rightIndex >= 0 && rightIndex < arr.length && alreadyReached.add(rightIndex)) {
                q.add(rightIndex);
            }

        }

        return false;
    }

    /**
     * 我们定义「顺次数」为：每一位上的数字都比前一位上的数字大 1 的整数。
     *
     * 请你返回由 [low, high] 范围内所有顺次数组成的 有序 列表（从小到大排序）。
     *
     * 示例 1：
     *
     * 输出：low = 100, high = 300
     * 输出：[123,234]
     * 示例 2：
     *
     * 输出：low = 1000, high = 13000
     * 输出：[1234,2345,3456,4567,5678,6789,12345]
     */
    public List<Integer> sequentialDigits(int low, int high) {
        List<Integer> ret = new ArrayList<>();
        if (low > high) {
            return ret;
        }
        List<Integer> seqList = new ArrayList<>();
        String str = "123456789";
        for (int i = 2; i <= 9; i++) {
            for (int j = i; j <= 9; j++) {
                Integer seq = Integer.valueOf(str.substring(j - i, j));
                seqList.add(seq);
            }
        }

        for (Integer seq : seqList) {
            if (seq >= low && seq <= high) {
                ret.add(seq);
            }
        }
        return ret;

    }

    public int[] avoidFlood(int[] rains) {
        int[] ret = new int[rains.length];

        Map<Integer, Integer> fulls = new HashMap<>();
        List<Integer> sortedZeros = new ArrayList<>();
        for (int i = 0; i < rains.length; i++) {
            int lake = rains[i];
            if (lake == 0) {
                sortedZeros.add(i);
            } else {
                Integer lakeIndex = fulls.get(lake);
                if (lakeIndex != null) {
                    int zeroIndex = Collections.binarySearch(sortedZeros, lakeIndex + 1);
                    if (zeroIndex >= 0) {
                        ret[sortedZeros.get(zeroIndex)] = lake;
                        sortedZeros.remove(zeroIndex);
                        fulls.put(lake, i);
                    } else {
                        int rightIndex = -zeroIndex - 1;
                        if (rightIndex >= sortedZeros.size()) {
                            // 找不到救赎数0
                            return new int[]{};
                        }
                        ret[sortedZeros.get(rightIndex)] = lake;
                        sortedZeros.remove(rightIndex);

                        fulls.put(lake, i);
                    }

                } else {
                    fulls.put(lake, i);
                }
                ret[i] = -1;

            }

        }
        return ret;
    }

    /*
     * 1461
     * 1353
     * 1400
     * 1405
     * 1419
     * 1423
     * 1492
     * 1493
     * 1508
     * 1513
     * 1529
     * 1535
     */

    public boolean hasAllCodes(String s, int k) {
        if (s == null || k >= s.length()) {
            return false;
        }
        Set<String> kSet = new HashSet<>();
        for (int i = 0, j = k; j <= s.length(); i++, j++) {

            String one = s.substring(i, j);
            kSet.add(one);
        }
        if (kSet.size() < Math.pow(2, k)) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 输入：events= [[1,2],[2,3],[3,4],[1,2]]
     * 输出：4
     *
     * [[1,2],[1,4]]
     * [[1,2],[3,4],[2,5]]
     * [[2,3],[3,4],[2,5]]
     * [[2,2],[2,2],[2,2]]
     *
     * [[1,2],[1,1]]
     */
    public int maxEvents(int[][] events) {
        int ans = 0, max = -1;
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a));
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] e : events) {
            map.computeIfAbsent(e[0], k -> new ArrayList<>()).add(e[1]);
            max = Math.max(e[1], max);
        }
        for (int i = 1; i <= max; i++) {
            if (map.containsKey(i)) {
                queue.addAll(map.get(i));
            }
            while (!queue.isEmpty() && queue.peek() < i) {
                queue.poll();
            }
            if (!queue.isEmpty()) {
                ans++;
                queue.poll();
            }
        }
        return ans;

    }


    public void foo(int k) {

        List<String> result = new ArrayList<>();

        gen("", k, result);

        System.out.println(JSON.toJSONString(result));
    }







    public int maxEvents2(int[][] events) {

        // max 为最后一个会议结束的时间
        int ans = 0, max = -1;
        // 按会议结束时间升序排序
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a));
        // key -> 第 i 天, value -> 第 i 天开始的会议的结束时间
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] e : events) {
            map.computeIfAbsent(e[0], k -> new ArrayList<>()).add(e[1]);
            max = Math.max(e[1], max);
        }
        for (int i = 1; i <= max; i++) {
            if (map.containsKey(i)) queue.addAll(map.get(i));
            while (!queue.isEmpty() && queue.peek() < i) queue.poll();
            if (!queue.isEmpty()) {
                ans++;
                queue.poll();
            }
        }
        return ans;
    }

    public int maxEvents3(int[][] events) {
        if (events == null || events.length == 0) {
            return 0;
        }
        // 开始时间， 结束时间List
        Map<Integer, List<Integer>> map = new HashMap<>();
        int maxDay = 0;
        for (int[] event : events) {
            map.computeIfAbsent(event[0], k -> new ArrayList<>()).add(event[1]);
            maxDay = Math.max(event[1], maxDay);
        }
        int count = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.naturalOrder());
        for (int i = 1; i <= maxDay; i++) {
            List<Integer> endDays = map.get(i);
            if (endDays != null) {
                pq.addAll(endDays);
            }
            // 弹出所有结束时间在今天之前的
            while (!pq.isEmpty() && pq.peek() < i) {
                pq.poll();
            }
            if (!pq.isEmpty()) {
                pq.poll();
                count++;
            }
        }
        return count;

    }


    private void gen(String curr, Integer k, List<String> result) {
        if (k == 0) {
            result.add(curr);
            return;
        }
        gen(curr + "0", k - 1, result);
        gen(curr + "1", k - 1, result);
    }


    public boolean canConstruct(String s, int k) {
        int length = s.length();
        if (k > length) {
            return false;
        }
        if (length == k) {
            return true;
        }
        if (k > 26) {
            return true;
        }
        Map<Character, Integer> countMap = new HashMap<>();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            countMap.put(c, countMap.getOrDefault(c, 0) + 1);
        }
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() % 2 != 0) {
                k--;
            }
        }
        return k >= 0;

    }




    public static void main(String[] args) {
        new CanReach().foo(3);
//        System.out.println(new CanReach().hasAllCodes("00110", 2));
    }



}
