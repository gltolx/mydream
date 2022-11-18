package com.lin.mydream.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lin.mydream.model.base.BaseModel;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.util.CommonUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/19.
 * @desc 记忆
 */
@Accessors(chain = true)
@Data
@TableName("my_remember")
public class Remember extends BaseModel {

    /**
     * 所关联的机器人id
     */
    @TableField("robot_id")
    private Long robotId;

    /**
     * 名称：2022考研、xxx和xxx相识、xxx和xxx相恋，也可以是设置的提醒
     */
    @TableField("remember_name")
    private String name;

    /**
     * 事件时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField("remember_time")
    private Date rememberTime;

    /**
     * 事件事件 字符串yyyy-MM-dd
     */
    @TableField("rem_time_str")
    private String remTimeStr;

    /**
     * 记忆类型 0-日历模式 1-提醒模式
     */
    @TableField("remember_type")
    private Integer type;
    /**
     * 消息接收者，手机号码，英文逗号隔开
     */
    @TableField("remember_receiver")
    private String receiver;
    /**
     * 是否已经接收提醒 默认0
     */
    @TableField("is_notified")
    private boolean notified;

    public String toSimpleString() {
        return CommonUtil.format("('{}' '{}' '{}' '{}')"
                , name
                , DateFormatUtils.format(rememberTime, "yyyy-MM-dd HH:mm:ss")
                , RobotEnum.of(type, RobotEnum.RememberType.class)
                , receiver);
    }
}
