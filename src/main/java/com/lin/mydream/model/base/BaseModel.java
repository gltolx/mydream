package com.lin.mydream.model.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/9.
 * @desc 基础Model
 */
@Getter
@Setter
public class BaseModel {

    /**
     * 自增id
     */
    @TableId(value = DBColumns.ID_COLUMN, type = IdType.AUTO)
    private Long id;
    /**
     * 逻辑删除标志位
     */
    @TableLogic(value = DBColumns.NOT_DELETED_VALUE, delval = DBColumns.IS_DELETED_VALUE)
    @TableField(DBColumns.DELETED_COLUMN)
    private boolean deleted;
    /**
     * 创建时间
     */
    @TableField(value = DBColumns.CREATE_TIME_COLUMN, fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = DBColumns.UPDATE_TIME_COLUMN, fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
