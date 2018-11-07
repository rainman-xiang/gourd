CREATE TABLE IF NOT EXISTS `gourd_producer_log` (
  `id` varchar(128) NOT NULL COMMENT 'id',
  `service_id` varchar(32) NOT NULL COMMENT '服务id',
  `payload` json NOT NULL COMMENT '消息载体',
  `destination` json NOT NULL COMMENT '消息目的地',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `send_count` int(4) NOT NULL DEFAULT '0' COMMENT '发送次数',
  `last_handle_time` timestamp NULL DEFAULT NULL COMMENT '最后处理时间',
  `version` int(4) NOT NULL DEFAULT '0' COMMENT '数据版本',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_service_send` (`service_id`,`send_count`) USING BTREE,
  KEY `idx_handle_time` (`last_handle_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `gourd_consumer_log` (
  `id` varchar(128) NOT NULL COMMENT 'id',
  `service_id` varchar(32) NOT NULL COMMENT '服务id',
  `payload` json NOT NULL COMMENT '消息载体',
  `queue` varchar(64) NOT NULL COMMENT '消息queue',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `handle_count` int(4) NOT NULL DEFAULT '0' COMMENT '处理次数',
  `last_handle_time` timestamp NULL DEFAULT NULL COMMENT '最后处理时间',
  `version` int(4) NOT NULL DEFAULT '0' COMMENT '数据版本',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息接收日志';