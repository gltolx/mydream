package com.lin.mydream.model.enumerate;

import com.lin.mydream.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc 机器人枚举
 */
public class RobotEnum {


    /**
     * 操作命令枚举
     */
    @AllArgsConstructor
    @Getter
    public enum CMD implements BaseEnum<String> {
        WAKE_UP("wake up", "唤醒记忆"),
        DELETE_REMEMBER("delete remember", "删除记忆"),
        DELETE_NOTIFY("delete notify", "删除提醒"),
        CREATE_REMEMBER("create remember", "创建记忆"),
        CREATE_NOTIFY("create notify", "创建提醒"),
        CREATE_LOOP_NOTIFY("create loop notify", "创建循环提醒"),
        LIST_REMEMBERS("list remembers", "列出记忆"),
        REGISTER_PHONE("register phone", "注册手机号"),
        DELETE_ROBOT("delete robot", "删除机器人"),
        CREATE_ROBOT("create robot", "创建机器人"),
        ACQUIRE_TOKEN("acquire token", "获取令牌"),
        TEST("test", "在线测试"),
        NULL("NULL", "空指令（错误指令）"),
        ;
        /**
         * 命令多样化
         */
        public static final Map<String, CMD> DIALECT_CMD = new ConcurrentHashMap<>();

        static {
            initDialectCmd();
        }

        /**
         * 匹配命令
         *
         * @param cmd 输入
         * @return 经过方言翻译后匹配的命令
         */
        public static CMD of(String cmd) {
            if (Objects.isNull(cmd)) {
                return CMD.NULL;
            }
            return CommonUtil.or(() ->
                    DIALECT_CMD.getOrDefault(cmd, RobotEnum.of(cmd, CMD.class)), CMD.NULL);
        }

        /**
         * 初始化方言命令（多样化命令）
         */
        private static void initDialectCmd() {
            // 唤醒记忆
            DIALECT_CMD.put(CMD.WAKE_UP.code(), CMD.WAKE_UP);
            DIALECT_CMD.put("wakeup", CMD.WAKE_UP);
            DIALECT_CMD.put("wkup", CMD.WAKE_UP);
            DIALECT_CMD.put("wake up remember", CMD.WAKE_UP);
            DIALECT_CMD.put("wake up remembers", CMD.WAKE_UP);
            DIALECT_CMD.put("唤醒记忆", CMD.WAKE_UP);
            DIALECT_CMD.put("唤醒", CMD.WAKE_UP);
            // 删除记忆
            DIALECT_CMD.put(CMD.DELETE_REMEMBER.code(), CMD.DELETE_REMEMBER);
            DIALECT_CMD.put("del rem", CMD.DELETE_REMEMBER);
            DIALECT_CMD.put("删除记忆", CMD.DELETE_REMEMBER);
            DIALECT_CMD.put("dr", CMD.DELETE_REMEMBER);
            // 删除提醒
            DIALECT_CMD.put(CMD.DELETE_NOTIFY.code(), CMD.DELETE_NOTIFY);
            DIALECT_CMD.put("删除提醒", CMD.DELETE_NOTIFY);
            DIALECT_CMD.put("del notify", CMD.DELETE_NOTIFY);
            DIALECT_CMD.put("dn", CMD.DELETE_NOTIFY);
            // 创建记忆
            DIALECT_CMD.put(CMD.CREATE_REMEMBER.code(), CMD.CREATE_REMEMBER);
            DIALECT_CMD.put("create rem", CMD.CREATE_REMEMBER);
            DIALECT_CMD.put("cr", CMD.CREATE_REMEMBER);
            DIALECT_CMD.put("创建记忆", CMD.CREATE_REMEMBER);
            // 创建提醒
            DIALECT_CMD.put(CMD.CREATE_NOTIFY.code(), CMD.CREATE_NOTIFY);
            DIALECT_CMD.put("create notify", CMD.CREATE_NOTIFY);
            DIALECT_CMD.put("cn", CMD.CREATE_NOTIFY);
            DIALECT_CMD.put("创建提醒", CMD.CREATE_NOTIFY);
            // 创建循环提醒
            DIALECT_CMD.put(CMD.CREATE_LOOP_NOTIFY.code(), CMD.CREATE_LOOP_NOTIFY);
            DIALECT_CMD.put("创建循环提醒", CMD.CREATE_LOOP_NOTIFY);
            DIALECT_CMD.put("cln", CMD.CREATE_LOOP_NOTIFY);
            // 列出记忆
            DIALECT_CMD.put(CMD.LIST_REMEMBERS.code(), CMD.LIST_REMEMBERS);
            DIALECT_CMD.put("list remember", CMD.LIST_REMEMBERS);
            DIALECT_CMD.put("list rem", CMD.LIST_REMEMBERS);
            DIALECT_CMD.put("list rems", CMD.LIST_REMEMBERS);
            DIALECT_CMD.put("lr", CMD.LIST_REMEMBERS);
            DIALECT_CMD.put("列出记忆", CMD.LIST_REMEMBERS);
            // 注册手机号
            DIALECT_CMD.put(CMD.REGISTER_PHONE.code(), CMD.REGISTER_PHONE);
            DIALECT_CMD.put("reg phone", CMD.REGISTER_PHONE);
            DIALECT_CMD.put("rp", CMD.REGISTER_PHONE);
            DIALECT_CMD.put("注册手机号", CMD.REGISTER_PHONE);
            // 删除机器人
            DIALECT_CMD.put(CMD.DELETE_ROBOT.code(), CMD.DELETE_ROBOT);
            DIALECT_CMD.put("del rob", CMD.DELETE_ROBOT);
            DIALECT_CMD.put("del robot", CMD.DELETE_ROBOT);
            DIALECT_CMD.put("删除机器人", CMD.DELETE_ROBOT);
            // 创建机器人
            DIALECT_CMD.put(CMD.CREATE_ROBOT.code(), CMD.CREATE_ROBOT);
            DIALECT_CMD.put("create rob", CMD.CREATE_ROBOT);
            DIALECT_CMD.put("crt rob", CMD.CREATE_ROBOT);
            DIALECT_CMD.put("创建机器人", CMD.CREATE_ROBOT);
            // 获取token
            DIALECT_CMD.put(CMD.ACQUIRE_TOKEN.code(), CMD.ACQUIRE_TOKEN);
            DIALECT_CMD.put("获取令牌", CMD.ACQUIRE_TOKEN);
            DIALECT_CMD.put("at", CMD.ACQUIRE_TOKEN);
            DIALECT_CMD.put("aot", CMD.ACQUIRE_TOKEN);
            DIALECT_CMD.put("acq tok", CMD.ACQUIRE_TOKEN);
            DIALECT_CMD.put("acquire outgoing token", CMD.ACQUIRE_TOKEN);
            // 在线测试
            DIALECT_CMD.put(CMD.TEST.code(), CMD.TEST);

        }


        private final String code;
        private final String name;
    }

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
