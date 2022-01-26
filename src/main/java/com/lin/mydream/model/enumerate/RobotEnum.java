package com.lin.mydream.model.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc 机器人枚举
 */
public class RobotEnum {

    /**
     * 机器人状态
     */
    @AllArgsConstructor
    @Getter
    public enum Stat implements BaseEnum<Integer> {
        valid(1, "正常"),
        initial(0, "初始化"),
        invalid(-1, "失效"),
        ;

        private final Integer code;
        private final String name;
    }

    /**
     * 记忆类型
     */
    @AllArgsConstructor
    @Getter
    public enum RememberType implements BaseEnum<Integer> {
        remember(0, "记忆"),
        notify(1, "提醒"),
        ;

        private final Integer code;
        private final String name;

        public static RememberType judge(String head) {
            if (head.toLowerCase().contains(RememberType.remember.name())
                    || head.contains(RememberType.remember.getName())) {
                return RememberType.remember;
            } else if (head.toLowerCase().contains(RememberType.notify.name())
                    || head.contains(RememberType.notify.getName())) {
                return RememberType.notify;
            } else {
                return RememberType.remember;
            }
        }
    }


    public static <C, T extends BaseEnum<C>> T of(C code, Class<T> enumClz) {
        Objects.requireNonNull(code);
        if (!BaseEnum.class.isAssignableFrom(enumClz) || !enumClz.isEnum()) {
            throw new IllegalArgumentException("not support type.");
        }

        T[] enumConstants = enumClz.getEnumConstants();
        for (T t : enumConstants) {
            if (code.equals(t.getCode())) {
                return t;
            }
        }
        return null;
    }

    public static <C, T extends BaseEnum<C>> String name(C code, Class<T> enumClz) {
        return Optional
                .ofNullable(of(code, enumClz))
                .map(BaseEnum::getName)
                .orElse(null);
    }

    public interface BaseEnum<C> {
        C getCode();

        String getName();

        default C code() {
            return getCode();
        }

        default boolean equalsCode(C code) {
            return code != null && code.equals(getCode());
        }

        default boolean equalsName(String name) {
            return name != null && name.equals(getName());
        }
    }
}
