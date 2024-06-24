package com.lin.mydream.util.algo.base;

import lombok.Data;

/**
 * @author xunjiang@hho-inc.com
 * @date 2024/5/22 20:07
 * @desc
 */
@Data
public class Node {
    private Node left;
    private Node right;
    private Integer value;
}
