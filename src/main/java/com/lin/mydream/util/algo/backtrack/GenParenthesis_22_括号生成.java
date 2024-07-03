package com.lin.mydream.util.algo.backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/28 13:09
 * @desc
 */
public class GenParenthesis_22_括号生成 {

    private final List<String> res = new ArrayList<>();
    private int leftRemain;
    private int rightRemain;

    public static void main(String[] args) {
        System.out.println(new GenParenthesis_22_括号生成().generateParenthesis(6));

    }
    public List<String> generateParenthesis(int n) {
        if (n ==0 || n % 2 != 0) {
            return res;
        }
        rightRemain = leftRemain = n/2;

        backtrack(new StringBuilder(), n);
        return res;
    }

    public void backtrack(StringBuilder track, int n) {

        // 结束条件
        if (track.length() >= n || (leftRemain ==0 && rightRemain ==0)) {
            res.add(track.toString());
            return;
        }
        // 选择(
        if (leftRemain > 0) {
            track.append("(");
            leftRemain--;
            backtrack(track, n);
            // 撤销(
            track.deleteCharAt(track.length() - 1);
            leftRemain++;
        }

        // 选择)
        if (rightRemain > 0 && leftRemain < rightRemain) {
            track.append(")");
            rightRemain--;
            backtrack(track, n);
            // 撤销)
            track.deleteCharAt(track.length() - 1);
            rightRemain++;
        }

    }
}
