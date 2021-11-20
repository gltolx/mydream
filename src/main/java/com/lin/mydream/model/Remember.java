package com.lin.mydream.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lin.mydream.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/19.
 * @desc 纪念日/倒计时
 */
@Data
@TableName("my_remember")
public class Remember extends BaseModel {

    /**
     * 所关联的机器人id
     */
    @TableField("robot_id")
    private Long robotId;

    /**
     * 名称：2022考研、xxx和xxx相识、xxx和xxx相恋
     */
    @TableField("remember_name")
    private String name;

    /**
     * 事件时间 yyyy-MM-dd
     */
    @TableField("remember_time")
    private Date rememberTime;


}
