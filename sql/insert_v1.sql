-- `id`
-- int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '机器人表主键',
--     `is_deleted`      tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除,0未删除,1删除',
--     `create_time`     datetime              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--     `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON
-- UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
--     `robot_name` varchar (32) NOT NULL COMMENT '机器人名字',
--     `access_token` varchar (130) NOT NULL COMMENT '钉钉access_token',
--     `robot_sign` varchar (64) NOT NULL COMMENT '钉钉验签',
--     `outgoing_enable` tinyint NOT NULL DEFAULT 0 COMMENT '是否启用outgoing机制 1启用 0不开启',
--     `outgoing_token` varchar (32) DEFAULT NULL COMMENT 'outgoing机制的token',
--     `is_admin` tinyint NOT NULL DEFAULT 0 COMMENT '是否是管理者机器人 1是 0不是',
--     `robot_stat` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 1正常 0失效',

insert into my_robot(`access_token`, `robot_sign`, `outgoing_enable`, `outgoing_token`, `is_admin`)
values ('f0676f432ae1da77af9a6219296982c23741ae23ea6f7638bc4bfebfb8d8771a',
        'SECc99725f7915d0314e7c77eed43f51376c47736116ae567374f3fb3f4544a1a6c', true, 'e49f5b', true);


insert into my_robot(`access_token`, `robot_sign`, `outgoing_enable`, `outgoing_token`, `is_admin`)
values ('bf8625341582063e835661a1dd664a59bc32547e0e964e09fb2469b0ce671282',
        'SEC7fd4798ab65fabf250b2b48e27edc4a167d093201fdd4f42b1f9a5e2e0cc11a2', true, 'R53pvt4', true);
