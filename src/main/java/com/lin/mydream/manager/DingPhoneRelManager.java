package com.lin.mydream.manager;

import com.lin.mydream.mapper.DingPhoneRelMapper;
import com.lin.mydream.model.DingPhoneRel;
import org.springframework.stereotype.Service;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2022/1/27.
 * @desc 钉id绑定手机号Manager
 */
@Service
public class DingPhoneRelManager extends BaseManager<DingPhoneRelMapper, DingPhoneRel> {

    /**
     * 注册手机号
     */
    public boolean register(String senderId, String phone) {
        if (this.existBy(qw().eq("ding_id", senderId))) {
            return this.update(uw().set("phone_num", phone).eq("ding_id", senderId));
        }
        DingPhoneRel entity = new DingPhoneRel().setDingId(senderId).setPhoneNum(phone);
        return this.save(entity);
    }

    /**
     * 获取手机号
     * @param senderId dingID
     */
    public String phone(String senderId) {
        return this.selectObjString(qw().select("phone_num").eq("ding_id", senderId));
    }
}
