package com.lin.mydream.util.algo;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class UncompressTest {
    public static void main(String[] args) {
//        System.out.println(findConsistLongestString("abcd123.4567.890.123"));
        System.out.println(uncompress("a1b2c3"));
        System.out.println(uncompress("abc3"));
    }

    /**
     * 简单的数据解压缩, 并排序
     */
    public static String uncompress3(String in) {
        Map<Character, Integer> countMap = new HashMap<>();
        char[] chars = in.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i++];
            int count = 1;
            if (Character.isDigit(c)) {
                int j = i;
                while (j < chars.length && Character.isDigit(chars[j])) {
                    j++;
                }
                count = Integer.parseInt(in.substring(i- 1,j));
                i=j;
            }
            countMap.merge(c, count, Integer::sum);
        }

        List<Map.Entry<Character, Integer>> entries = new ArrayList<>(countMap.entrySet());
        entries.sort(Comparator.comparingInt(Map.Entry::getValue));

        // 根据频次还原字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : entries) {
            char c = entry.getKey();
            int count = entry.getValue();
            if (!Character.isDigit(c)) {
                for (int j = 0; j < count; j++) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();

    }


    public static String findConsistLongestString(String str) {
        int len = str.length();
        int start = -1, end = -1;
        int maxLen = 0, currLen = 0;

        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                if (start == -1) {
                    start = i;
                }
                end = i;
                currLen++;
            } else {
                if (start != -1
                        && currLen > maxLen
                        && (start == 0 || Character.isDigit(str.charAt(start - 1)))
                        && (end == len - 1 || Character.isDigit(str.charAt(end + 1)))
                ) {
                    start = -1;
                    end = -1;
                    currLen = 0;
                }
            }
        }

        // 处理剩余的字符串
        if (start != -1
                && currLen > maxLen
                && (start == 0 || Character.isDigit(str.charAt(start - 1)))
                && (end == len - 1 || Character.isDigit(str.charAt(end + 1)))
        ) {
            maxLen = currLen;
        }
        if (maxLen == 0) {
            return StringUtils.EMPTY;
        } else {
            return str.substring(start, start + maxLen);
        }

    }

    public static String uncompress2(String in) {
        int[] count = new int[128];
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (Character.isDigit(c)) {
                int j = i + 1;
                while (j < in.length() && Character.isDigit(in.charAt(j))) {
                    j++;
                }
                // 计算频次
                int freq = Integer.parseInt(in.substring(i, j));
                i = j - 1;
                count[in.charAt(i - 1)] += freq;
            } else {
                count[c]++;
            }
        }
        StringBuilder[] sbArr = new StringBuilder[in.length()];
        for (int i = 0; i < 128; i++) {
            if (count[i] > 0) {
                if (sbArr[count[i]] == null) {
                    sbArr[count[i]] = new StringBuilder();
                }
                sbArr[count[i]].append((char) i);
            }
        }
        // 根据频次还原字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sbArr.length; i++) {
            if (sbArr[i] != null) {
                char[] chars = sbArr[i].toString().toCharArray();
                Arrays.sort(chars);
                for (char c : chars) {
                    int f = count[c];
                    for (int j = 0; j < f; j++) {
                        sb.append(c);
                    }
                }
            }
        }


        return sb.toString();

    }

    public static String uncompress(String in) {
        StringBuilder curr = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (Character.isDigit(c)) {
                String currString = curr.toString();
                if (!currString.equals("")) {
                    int count = Integer.parseInt(String.valueOf(c));

                    for (int j = 0; j < count; j++) {
                        result.append(currString);
                    }
                    curr = new StringBuilder();
                }
            } else {
                curr.append(c);
            }
        }
        return result.toString();

    }

    // 简单的数据解压缩
    public static String uncompress0(String in) {
        // 记录次数
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < in.length(); i += 2) {
            String s = in.substring(i, i + 1);
            int count = Integer.parseInt(in.substring(i + 1, i + 2));
            map.put(s, map.getOrDefault(s, 0) + count);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> Objects.equals(o1.getValue(), o2.getValue())
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue() - o2.getValue());

        // 还原字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : list) {
            String s = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                sb.append(s);
            }
        }
        return sb.toString();

    }
}
