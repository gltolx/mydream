package com.lin.mydream.model.base;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/9.
 * @desc DB column 常量类
 */
public interface DBColumns {
    /**
     * 「id」列名
     */
    String ID_COLUMN = "id";
    /**
     * 「逻辑删除」列名
     */
    String DELETED_COLUMN = "is_deleted";
    /**
     * 「创建时间」列名
     */
    String CREATE_TIME_COLUMN = "create_time";
    /**
     * 「修改时间」列名
     */
    String UPDATE_TIME_COLUMN = "update_time";
    /**
     * 「逻辑删除」未删除列值
     */
    String NOT_DELETED_VALUE = "0";
    /**
     * 「逻辑删除」已删除列值
     */
    String IS_DELETED_VALUE = "1";
}
