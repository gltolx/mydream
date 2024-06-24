package com.lin.mydream.util.algo;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/5/16 20:00
 * @desc
 */
public class AlgTest {

    public static void main(String[] args) {

        System.out.println(compactSize("aabbccc".toCharArray()));
//        System.out.println(compactSize("abbbbbbbbbbbbbb".toCharArray()));
//        System.out.println(compactSize("abbbccccdddddddddd".toCharArray()));
    }

    /**
     * 输入压缩数组
     * @return 被压缩后的数组长度
     */
    public static int compactSize(char[] chars) {
        if (chars == null || chars.length == 0) {
            return 0;
        }
        if (chars.length == 1) {
            return 1;
        }
        int currentLength = 1;
        int pos = 0;
        for (int i = 1; i <= chars.length; i++) {
            char preChar = chars[i - 1];
            if (i == chars.length || chars[i] != preChar) {
                chars[pos] = preChar;
                // 拆分currentLength
                pos = divideCurrentLength(chars, currentLength, pos);

                currentLength = 1;
            } else {
                currentLength++;
            }

        }
        System.out.println(chars);
        return pos;
    }

    private static int divideCurrentLength(char[] chars, int currentLength, int pos) {
        if (currentLength == 1) {
            pos++;
        } else {
            int length = String.valueOf(currentLength).length();
            int countLength = length;
            while (currentLength > 0) {
                chars[pos + countLength--] = String.valueOf(currentLength % 10).toCharArray()[0];
                currentLength /= 10;
            }
            pos = pos + length + 1;
        }
        return pos;
    }
}
