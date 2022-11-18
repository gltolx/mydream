package com.lin.mydream.manager;

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
public class RememberManager extends BaseManager<RememberMapper, Remember> {


    public List<Remember> listByRobotId(Long robotId) {
        Objects.requireNonNull(robotId);

        return this.list(
                qw().eq("robot_id", robotId)
        );
    }

    public List<Remember> listByRobotId(Long robotId, Integer remType) {
        Objects.requireNonNull(robotId);
        Objects.requireNonNull(remType);

        return this.list(
                qw().eq("robot_id", robotId).eq("remember_type", remType)
        );
    }

    public boolean deleteLike(Long robotId, String name) {
        return this.update(
                uw().set("is_deleted", true)
                .eq("robot_id", robotId)
                .like("remember_name", name)
        );

//        return this.lambdaUpdate()
//                .set(x -> x.isDeleted(), true)
//                .eq(x -> x.getRobotId(), robotId)
//                .like(x -> x.getName(), name)
//                .update();
    }
}
