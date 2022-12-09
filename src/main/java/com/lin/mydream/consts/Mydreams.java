package com.lin.mydream.consts;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc mydream常量
 */
public class Mydreams {

    public static final String BASE_API = "md/api/v1/";

    public static final String CT_JSON = "application/json; charset=utf-8";
    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    /**
     * 管理员手机号
     */
    public static final String[] ADMIN_PHONES = {"17826833386"};
    /**
     * 庄周说
     */
    private static final String[] zzs = {"子非我，安知我不知鱼之乐?", "相濡以沫，不如相忘于江湖", "不知周之梦为蝴蝶与?",
            "蝴蝶之梦为周与?", "彼出于是，是亦因彼", "人生天地之间", "若白驹之过隙", "天地与我并生", "万物与我合一", "不以物挫志",
            "日出而作日入而息", "逍遥于天地之间", "临大难而不惧", "鉴明则尘垢不止", "形莫若就心莫若和", "意有所至爱有所亡", "不知其所以然",
            "天地有大美而不言", "莫鉴流水，而鉴止水", "逐万物而不还", "化腐朽为神奇", "天地一指，万物一马", "不忘其所始，不求其所终",
            "盖之如天，容之如地", "存己而后存人", "天机不可泄露", "通于一而万事毕", "一气之变，所适万形", "朴素而天下莫能与争美",
            "浮生若梦，若梦非梦", "浮生何如？如梦之梦", "往者谏！来者追！"};

    public static String getZzs() {
        if (ArrayUtils.isEmpty(zzs)) {
            return "往者谏，来者追";
        }
        return zzs[RandomUtils.nextInt(0, zzs.length)];
    }
}
