package com.lin.mydream.util.algo.backtrack;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/6/26 23:54
 */
public class NQueens_51_N皇后 {

    private List<List<String>> res = new ArrayList<>();
    private List<String> track = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(new NQueens_51_N皇后().solveNQueens(4)));

    }
    public List<List<String>> solveNQueens(int n) {
        if (n <=0) {
            return new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(".");
            }
            track.add(sb.toString());
        }

        backtrack(0);

        return res;
    }

    public boolean isInvalid(int row, int col) {
        int n = track.size();
        // 检查列是否有冲突
        for (int i = 0; i < n; i++) {
            if (track.get(i).charAt(col) == 'Q') {
                return true;
            }
        }
        // 检查左上是否有冲突
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (track.get(i).charAt(j) == 'Q') {
                return true;
            }
        }
        // 检查右上是否有冲突
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if (track.get(i).charAt(j) == 'Q') {
                return true;
            }
        }
        return false;
    }


    public void backtrack(int row) {
        // 结束条件：决策树高度达到n，说明n个皇后都放进去了
       if (row > track.size() - 1) {
           res.add(new ArrayList<>(track));
           return;
       }
        // 选择列表，皇后从一行一行往下放，天然规避行内和下方的冲突
        int n = track.get(row).length();
        for (int i = 0; i < n; i++) {
            // TODO 排除不合法的
            if (isInvalid(row, i)) {
                continue;
            }

            // 做选择
            StringBuilder sb = new StringBuilder(track.get(row));
            sb.setCharAt(i, 'Q');
            track.set(row, sb.toString());
            // 进入子决策树
            backtrack(row + 1);
            // 撤销选择
            sb.setCharAt(i, '.');
            track.set(row, sb.toString());

        }

    }


    public List<List<String>> solveNQueens2(int n) {
        if (n <=0) {
            return new ArrayList<>();
        }
        // 构建可选择列表（棋盘）
        int[][] selects = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                selects[i][j] = 0;
            }
        }
        int[][] track = new int[n][n];
        boolean[][] selected = new boolean[n][n];

        return res;


    }
//    public void backtrack2(int n, int[][] selects, boolean[][] selected, int[][] track, int heights) {
//        // 结束条件：决策树高度达到n，说明n个皇后都放进去了
//        if (heights == n) {
//            List<String> singleList = new ArrayList<>();
//            for (int[] ts : track) {
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < ts.length; i++) {
//                    sb.append(ts[i] == 0 ? "." : "Q");
//                }
//                singleList.add(sb.toString());
//            }
//            res.add(singleList);
//            return;
//        }
//
//        // 选择列表
//        for (int i = 0; i < selects.length; i++) {
//            int[] selectRows = selects[i];
//            for (int j = 0; j < selectRows.length; j++) {
//
//                if (selected[i][j]) {
//                    continue;
//                }
//                // TODO 检查上下左右和斜线是否有皇后冲突
//                if ()
//
//
//                // 选择：1、加入结果列表 2、剔除可选择列表
//                track[i][j] = 1;
//                selects[i][j] = 1;
//                selected[i][j] = true;
//
//                // 进入下一决策树
//                backtrack(n, selects, selected, track, ++heights);
//
//                // 撤销选择：1、剔除结果列表 2、加入可选择列表
//                track[i][j] = 0;
//                selects[i][j] = 0;
//                selected[i][j] = false;
//
//            }
//        }
//
//    }
}
