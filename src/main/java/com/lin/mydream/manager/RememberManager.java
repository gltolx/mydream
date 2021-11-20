package com.lin.mydream.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.mydream.mapper.RememberMapper;
import com.lin.mydream.model.Remember;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/20.
 * @desc Remember Manager层
 */
@Service
public class RememberManager extends ServiceImpl<RememberMapper, Remember> {


    public List<Remember> listByRobotId(Long robotId) {
        Objects.requireNonNull(robotId);

        return this.lambdaQuery()
                .eq(x -> x.getRobotId(), robotId)
                .list();
    }

    public boolean deleteLike(Long robotId, String name) {
        return this.lambdaUpdate()
                .set(x -> x.isDeleted(), true)
                .eq(x -> x.getRobotId(), robotId)
                .like(x -> x.getName(), name)
                .update();
    }
}
