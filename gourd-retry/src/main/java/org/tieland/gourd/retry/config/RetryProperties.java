package org.tieland.gourd.retry.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhouxiang
 * @date 2018/10/31 9:33
 */
@Data
@ConfigurationProperties("gourd.retry")
public class RetryProperties {

    /**
     * 默认每次获取补偿的最大数据集大小
     */
    private static final Integer DEFAULT_MAX_SIZE = 100;

    /**
     * 默认消息最大尝试次数
     */
    private static final Integer DEFAULT_MAX_RETRY = 10;

    /**
     * 默认间隔120s补偿一次
     */
    private static final Integer DEFAULT_RETRY_INTERVAL = 120;

    /**
     * 每次获取补偿的最大数据集大小
     */
    private Integer maxSize = DEFAULT_MAX_SIZE;

    /**
     * 消息最大尝试次数
     */
    private Integer maxRetry = DEFAULT_MAX_RETRY;

    /**
     * 补偿间隔时间
     */
    private Integer retryInterval = DEFAULT_RETRY_INTERVAL;
}
