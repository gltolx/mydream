package com.lin.mydream.consts;

import com.lin.mydream.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/15.
 * @desc 定义业务异常
 */
public class MydreamException extends RuntimeException{
    private static final long serialVersionUID = -2931417477709015435L;

    private String message;
    private String code;

    public static MydreamException of(String message, String... format) {
        return new MydreamException("1001", CommonUtil.format(message, format));
    }

    public MydreamException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public MydreamException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCode() {
        return code;
    }

    public MydreamException setCode(String code) {
        this.code = code;
        return this;
    }
}
