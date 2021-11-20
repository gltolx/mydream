CREATE TABLE `my_robot`
(
    `id`              int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '机器人表主键',
    `is_deleted`      tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除,0未删除,1删除',
    `create_time`     datetime              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `access_token`    varchar(128) NOT NULL COMMENT '钉钉access_token',
    `robot_sign`      varchar(128) NOT NULL COMMENT '钉钉验签',
    `outgoing_enable` tinyint      NOT NULL DEFAULT 0 COMMENT '是否启用outgoing机制 1启用 0不开启',
    `outgoing_token`  varchar(32)  NOT NULL COMMENT 'outgoing机制的token',
    `is_admin`        tinyint      NOT NULL DEFAULT 0 COMMENT '是否是管理者机器人 1是 0不是',
    `robot_stat`      tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态  1正常 0初始化 -1失效',
    UNIQUE KEY (`outgoing_token`),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机器人表';