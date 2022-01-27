package com.lin.mydream.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lin.mydream.model.base.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2022/1/27.
 * @desc 钉id和个人手机号绑定
 */
@Slf4j
@Data
@Accessors(chain = true)
@TableName("my_ding_phone_rel")
public class DingPhoneRel extends BaseModel {

    /**
     * 钉id
     */
    @TableField("ding_id")
    private String dingId;
    /**
     * 个人手机号
     */
    @TableField("phone_num")
    private String phoneNum;

}
